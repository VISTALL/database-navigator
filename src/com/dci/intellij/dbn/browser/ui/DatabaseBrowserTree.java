package com.dci.intellij.dbn.browser.ui;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.browser.DatabaseBrowserUtils;
import com.dci.intellij.dbn.browser.TreeNavigationHistory;
import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.browser.model.BrowserTreeModel;
import com.dci.intellij.dbn.browser.model.TabbedBrowserTreeModel;
import com.dci.intellij.dbn.common.event.EventManager;
import com.dci.intellij.dbn.common.filter.Filter;
import com.dci.intellij.dbn.common.thread.ModalTask;
import com.dci.intellij.dbn.common.thread.SimpleLaterInvocator;
import com.dci.intellij.dbn.common.ui.tree.DBNTree;
import com.dci.intellij.dbn.common.util.UIUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.action.ConnectionActionGroup;
import com.dci.intellij.dbn.object.action.ObjectActionGroup;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectBundle;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import com.dci.intellij.dbn.object.common.list.action.ObjectListActionGroup;
import com.dci.intellij.dbn.object.common.property.DBObjectProperty;
import com.dci.intellij.dbn.vfs.DatabaseFileSystem;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.JPopupMenu;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class DatabaseBrowserTree extends DBNTree implements Disposable {
    private BrowserTreeElement targetSelection;
    private BrowserTreeModel treeModel;
    private JPopupMenu popupMenu;
    private TreeNavigationHistory navigationHistory = new TreeNavigationHistory();
    private boolean isDisposed = false;

    public DatabaseBrowserTree(BrowserTreeModel treeModel) {
        super(treeModel);
        this.treeModel = treeModel;

        addKeyListener(keyListener);
        addMouseListener(mouseListener);
        addTreeSelectionListener(treeSelectionListener);

        setToggleClickCount(0);
        setRootVisible(treeModel instanceof TabbedBrowserTreeModel);
        setShowsRootHandles(true);
        setAutoscrolls(true);
        DatabaseBrowserTreeCellRenderer browserTreeCellRenderer = new DatabaseBrowserTreeCellRenderer(treeModel.getProject());
        setCellRenderer(browserTreeCellRenderer);
        //setExpandedState(DatabaseBrowserUtils.createTreePath(treeModel.getRoot()), false);

        new DatabaseBrowserTreeSpeedSearch(this);
    }

    public Project getProject() {
        return treeModel.getProject();
    }

    @Override
    public BrowserTreeModel getModel() {
        return (BrowserTreeModel) super.getModel();
    }

    public TreeNavigationHistory getNavigationHistory() {
        return navigationHistory;
    }

    public void expandConnectionManagers() {
        new SimpleLaterInvocator() {
            public void run() {
                List<ConnectionManager> connectionManagers = DatabaseBrowserManager.getInstance(getProject()).getConnectionManagers();
                for (ConnectionManager connectionManager : connectionManagers) {
                    TreePath treePath = DatabaseBrowserUtils.createTreePath(connectionManager);
                    setExpandedState(treePath, true);
                }
            }
        }.start();
    }

    public void selectElement(BrowserTreeElement treeElement, boolean requestFocus) {
        ConnectionHandler connectionHandler = treeElement.getConnectionHandler();
        Filter<BrowserTreeElement> filter = connectionHandler == null ?
                DatabaseBrowserManager.getInstance(getProject()).getObjectFilter() :
                connectionHandler.getObjectFilter();

        if (filter.accepts(treeElement)) {
            targetSelection = treeElement;
            scrollToSelectedElement();
            if (requestFocus) requestFocus();
        }

    }

    public synchronized void scrollToSelectedElement() {
        if (getProject().isOpen() && targetSelection != null) {
            targetSelection = (BrowserTreeElement) targetSelection.getUndisposedElement();
            TreePath treePath = DatabaseBrowserUtils.createTreePath(targetSelection);
            for (Object object : treePath.getPath()) {
                if (object == null) {
                    targetSelection = null;
                    return;
                }

                BrowserTreeElement treeElement = (BrowserTreeElement) object;
                if (treeElement.equals(targetSelection)) {
                    break;
                }

                if (!treeElement.isLeafTreeElement() && !treeElement.isTreeStructureLoaded()) {
                    selectPath(DatabaseBrowserUtils.createTreePath(treeElement));
                    treeElement.getTreeChildren();
                    return;
                }
            }

            targetSelection = null;
            selectPath(treePath);
        }
    }



    public BrowserTreeElement getSelectedElement() {
        TreePath selectionPath = getSelectionPath();
        return selectionPath == null ? null : (BrowserTreeElement) selectionPath.getLastPathComponent();
    }

    public BrowserTreeElement getTargetSelection() {
        return targetSelection;
    }

    private void selectPath(final TreePath treePath) {
        new SimpleLaterInvocator() {
            public void run() {
                TreeUtil.selectPath(DatabaseBrowserTree.this, treePath, true);
            }
        }.start();
    }


    public String getToolTipText(MouseEvent event) {
        TreePath path = getClosestPathForLocation(event.getX(), event.getY());
        if (path != null) {
            Rectangle pathBounds = getPathBounds(path);

            if (pathBounds != null) {
                Point mouseLocation = UIUtil.getRelativeMouseLocation(event.getComponent());
                if (pathBounds.contains(mouseLocation)) {
                    Object object = path.getLastPathComponent();
                    if (object instanceof ToolTipProvider) {
                        ToolTipProvider toolTipProvider = (ToolTipProvider) object;
                        return toolTipProvider.getToolTip();
                    }
                }
            }
        }
        return null;
    }

    public void navigateBack() {
        BrowserTreeElement treeElement = navigationHistory.previous();
        selectPathSilently(DatabaseBrowserUtils.createTreePath(treeElement));
    }

    public void navigateForward() {
        BrowserTreeElement treeElement = navigationHistory.next();
        selectPathSilently(DatabaseBrowserUtils.createTreePath(treeElement));
    }


    public void selectPathSilently(TreePath treePath) {
        listenersEnabled = false;
        selectionModel.setSelectionPath(treePath);
        TreeUtil.selectPath(DatabaseBrowserTree.this, treePath, true);
        listenersEnabled = true;
    }

    private boolean listenersEnabled = true;

    public void expandAll() {
        BrowserTreeElement root = getModel().getRoot();
        expand(root);
    }

    public void expand(BrowserTreeElement treeElement) {
        if (treeElement.canExpand()) {
            expandPath(DatabaseBrowserUtils.createTreePath(treeElement));
            for (int i = 0; i < treeElement.getTreeChildCount(); i++) {
                BrowserTreeElement childTreeElement = treeElement.getTreeChild(i);
                expand(childTreeElement);
            }
        }
    }

    public void collapseAll() {
        BrowserTreeElement root = getModel().getRoot();
        collapse(root);
    }

    public void collapse(BrowserTreeElement treeElement) {
        if (!treeElement.isLeafTreeElement() && treeElement.isTreeStructureLoaded()) {
            for (int i = 0; i < treeElement.getTreeChildCount(); i++) {
                BrowserTreeElement childTreeElement = treeElement.getTreeChild(i);
                collapse(childTreeElement);
                collapsePath(DatabaseBrowserUtils.createTreePath(childTreeElement));
            }
        }
    }

    private void processSelectEvent(InputEvent event, TreePath path, boolean deliberate) {
        if (path != null) {
            Object lastPathEntity = path.getLastPathComponent();
            if (lastPathEntity instanceof DBObject) {
                DBObject object = (DBObject) lastPathEntity;
                if (object.getProperties().is(DBObjectProperty.EDITABLE)) {
                    DBSchemaObject schemaObject = (DBSchemaObject) object;
                    DatabaseFileSystem.getInstance().openEditor(schemaObject);
                    event.consume();
                } else if (object.getProperties().is(DBObjectProperty.NAVIGABLE)) {
                    DatabaseFileSystem.getInstance().openEditor(object);
                    event.consume();
                } else if (deliberate) {
                    DBObject navigationObject = object.getDefaultNavigationObject();
                    if (navigationObject != null) navigationObject.navigate(true);
                }
            } else if (lastPathEntity instanceof DBObjectBundle) {
                DBObjectBundle objectBundle = (DBObjectBundle) lastPathEntity;
                ConnectionHandler connectionHandler = objectBundle.getConnectionHandler();
                FileEditorManager fileEditorManager = FileEditorManager.getInstance(connectionHandler.getProject());
                fileEditorManager.openFile(connectionHandler.getSQLConsoleFile(), true);
            }
        }
    }
    
/*    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        boolean navigable = false;
        if (e.isControlDown() && e.getID() != MouseEvent.MOUSE_DRAGGED && !e.isConsumed()) {
            TreePath path = getPathForLocation(e.getX(), e.getY());
            Object lastPathEntity = path == null ? null : path.getLastPathComponent();
            if (lastPathEntity instanceof DBObject) {
                DBObject object = (DBObject) lastPathEntity;
                DBObject navigationObject = object.getDefaultNavigationObject();
                navigable = navigationObject != null;
            }
            
        }

        if (navigable) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            super.processMouseMotionEvent(e);
            setCursor(Cursor.getDefaultCursor());
        }
    }  */

    public void dispose() {
        isDisposed = true;
        targetSelection = null;
        treeModel.dispose();
        treeModel = null;
        navigationHistory.clear();
        navigationHistory = null;
    }

    /********************************************************
     *                 TreeSelectionListener                *
     ********************************************************/
    private TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
        public void valueChanged(TreeSelectionEvent e) {
            if (!isDisposed && listenersEnabled) {
                Object object = e.getPath().getLastPathComponent();
                if (object != null && object instanceof BrowserTreeElement) {
                    BrowserTreeElement treeElement = (BrowserTreeElement) object;
                    if (targetSelection == null || treeElement.equals(targetSelection)) {
                        navigationHistory.add(treeElement);
                    }
                }

                BrowserSelectionChangeListener listener = EventManager.syncPublisher(getProject(), BrowserSelectionChangeListener.TOPIC);
                listener.browserSelectionChanged();

            }
        }
    };

    /********************************************************
     *                      MouseListener                   *
     ********************************************************/
    private MouseListener mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent event) {
            if (event.getButton() == MouseEvent.BUTTON1) {
                DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(getProject());
                if (browserManager.getAutoscrollToEditor().value() || event.getClickCount() > 1) {
                    TreePath path = getPathForLocation(event.getX(), event.getY());
                    processSelectEvent(event, path, event.getClickCount() > 1);
                }
            }
        }

        public void mouseReleased(final MouseEvent event) {
            if (event.getButton() == MouseEvent.BUTTON3) {
                final TreePath path = getPathForLocation(event.getX(), event.getY());
                if (path != null) {
                    final BrowserTreeElement lastPathEntity = (BrowserTreeElement) path.getLastPathComponent();
                    if (lastPathEntity.isDisposed()) return;

                    new ModalTask(lastPathEntity.getProject(), "Loading object information", true) {
                        public void run(@NotNull ProgressIndicator progressIndicator) {
                            progressIndicator.setIndeterminate(true);
                            ActionGroup actionGroup = null;
                            if (lastPathEntity instanceof DBObjectList) {
                                DBObjectList objectList = (DBObjectList) lastPathEntity;
                                actionGroup = new ObjectListActionGroup(objectList);
                            } else if (lastPathEntity instanceof DBObject) {
                                DBObject object = (DBObject) lastPathEntity;
                                actionGroup = new ObjectActionGroup(object);
                            } else if (lastPathEntity instanceof DBObjectBundle) {
                                DBObjectBundle objectsBundle = (DBObjectBundle) lastPathEntity;
                                ConnectionHandler connectionHandler = objectsBundle.getConnectionHandler();
                                actionGroup = new ConnectionActionGroup(connectionHandler);
                            }

                            if (actionGroup != null && !progressIndicator.isCanceled()) {
                                ActionPopupMenu actionPopupMenu = ActionManager.getInstance().createActionPopupMenu("", actionGroup);
                                popupMenu = actionPopupMenu.getComponent();
                                new SimpleLaterInvocator() {
                                    public void run() {
                                        popupMenu.show(DatabaseBrowserTree.this, event.getX(), event.getY());
                                    }
                                }.start();
                            } else {
                                popupMenu = null;
                            }
                        }
                    }.start();
                }
            }
        }
    };

    /********************************************************
     *                      KeyListener                     *
     ********************************************************/
    private KeyListener keyListener = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {  // ENTER
                TreePath path = getSelectionPath();
                processSelectEvent(e, path, true);
            }
        }
    };
}
