package com.dci.intellij.dbn.browser;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.browser.model.BrowserTreeModel;
import com.dci.intellij.dbn.browser.model.TabbedBrowserTreeModel;
import com.dci.intellij.dbn.browser.options.BrowserDisplayMode;
import com.dci.intellij.dbn.browser.options.DatabaseBrowserSettings;
import com.dci.intellij.dbn.browser.options.ObjectDisplaySettingsChangeListener;
import com.dci.intellij.dbn.browser.options.ObjectFilterChangeListener;
import com.dci.intellij.dbn.browser.ui.BrowserToolWindowForm;
import com.dci.intellij.dbn.browser.ui.DatabaseBrowserTree;
import com.dci.intellij.dbn.common.AbstractProjectComponent;
import com.dci.intellij.dbn.common.event.EventManager;
import com.dci.intellij.dbn.common.filter.Filter;
import com.dci.intellij.dbn.common.options.setting.BooleanSetting;
import com.dci.intellij.dbn.common.thread.ConditionalLaterInvocator;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.ConnectionSetupChangeListener;
import com.dci.intellij.dbn.connection.ModuleConnectionManager;
import com.dci.intellij.dbn.connection.ProjectConnectionManager;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.vfs.DatabaseEditableObjectFile;
import com.dci.intellij.dbn.vfs.SQLConsoleFile;
import com.intellij.ProjectTopics;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerAdapter;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.ModuleAdapter;
import com.intellij.openapi.project.ModuleListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseBrowserManager extends AbstractProjectComponent implements JDOMExternalizable, Disposable {
    private static final String TOOL_WINDOW_ID = "DB Browser";

    private List<ConnectionManager> connectionManagers = new ArrayList<ConnectionManager>();
    private BooleanSetting autoscrollFromEditor = new BooleanSetting("autoscroll-from-editor", true);
    private BooleanSetting autoscrollToEditor   = new BooleanSetting("autoscroll-to-editor", false);
    private BooleanSetting showObjectProperties = new BooleanSetting("show-object-properties", true);
    public static final ThreadLocal<Boolean> AUTOSCROLL_FROM_EDITOR = new ThreadLocal<Boolean>();
    private boolean isRebuilding;
    private BrowserToolWindowForm toolWindowForm;

    private DatabaseBrowserManager(Project project) {
        super(project);
        //FileEditorManager.getInstance(project).addFileEditorManagerListener(fileEditorManagerListener);
        //ModuleManager.getInstance(project).addModuleListener(moduleListener);

    }

    private void rebuildConnectionLists() {
        connectionManagers.clear();
        ProjectConnectionManager projectConnectionManager = ProjectConnectionManager.getInstance(getProject());
        if (projectConnectionManager.getConnectionHandlers().size() > 0) {
            connectionManagers.add(projectConnectionManager);
        }
        Module[] modules = ModuleManager.getInstance(getProject()).getModules();
        for (Module module : modules) {
            ModuleConnectionManager moduleConnectionManager = ModuleConnectionManager.getInstance(module);
            if (moduleConnectionManager.getConnectionHandlers().size() > 0) {
                connectionManagers.add(moduleConnectionManager);
            }
        }
        Collections.sort(connectionManagers);
        if (toolWindowForm != null) {
            toolWindowForm.getBrowserForm().rebuild();
        }
    }

    public DatabaseBrowserTree getActiveBrowserTree() {
        return getToolWindowForm().getActiveBrowserTree();
    }

    public ConnectionHandler getActiveConnection() {
        DatabaseBrowserTree activeBrowserTree = getActiveBrowserTree();
        if (activeBrowserTree != null) {
            BrowserTreeModel browserTreeModel = activeBrowserTree.getModel();
            if (browserTreeModel instanceof TabbedBrowserTreeModel) {
                TabbedBrowserTreeModel tabbedBrowserTreeModel = (TabbedBrowserTreeModel) browserTreeModel;
                return tabbedBrowserTreeModel.getConnectionHandler();
            }

            BrowserTreeElement browserTreeElement = activeBrowserTree.getSelectedElement();
            if (browserTreeElement != null) {
                return browserTreeElement.getConnectionHandler();
            }
        }

        return null;
    }

    @NotNull
    public ToolWindow getBrowserToolWindow() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(getProject());
        return toolWindowManager.getToolWindow(TOOL_WINDOW_ID);
    }

    public synchronized BrowserToolWindowForm getToolWindowForm() {
        if (toolWindowForm == null) {
            toolWindowForm = new BrowserToolWindowForm(getProject());
        }
        return toolWindowForm;
    }

    public List<ConnectionManager> getConnectionManagers() {
        return connectionManagers;
    }

    public BooleanSetting getAutoscrollFromEditor() {
        return autoscrollFromEditor;
    }

    public BooleanSetting getAutoscrollToEditor() {
        return autoscrollToEditor;
    }

    public BooleanSetting getShowObjectProperties() {
        return showObjectProperties;
    }

    public boolean isDisposed() {
        return false;
    }

    public String toString() {
        return "DB Browser";
    }

    public synchronized void navigateToElement(BrowserTreeElement treeElement, boolean requestFocus) {
        ToolWindow toolWindow = getBrowserToolWindow();

        toolWindow.show(null);
        if (treeElement != null) {
            getToolWindowForm().getBrowserForm().selectElement(treeElement, requestFocus);
        }
    }

    public synchronized void navigateToElement(BrowserTreeElement treeElement) {
        if (treeElement != null) {
            getToolWindowForm().getBrowserForm().selectElement(treeElement, false);
        }
    }

    public boolean isVisible() {
        ToolWindow toolWindow = getBrowserToolWindow();
        return toolWindow.isVisible();
    }

    /***************************************
     *     FileEditorManagerListener       *
     ***************************************/

    private boolean scroll() {
        return autoscrollFromEditor.value() && (AUTOSCROLL_FROM_EDITOR.get() == null || AUTOSCROLL_FROM_EDITOR.get());
    }

    /***************************************
     *            ProjectComponent         *
     ***************************************/
    public static DatabaseBrowserManager getInstance(Project project) {
        return project.getComponent(DatabaseBrowserManager.class);
    }

    @NonNls @NotNull
    public String getComponentName() {
        return "DBNavigator.Project.DatabaseBrowserManager";
    }

    public void projectOpened() {
        rebuildConnectionLists();
    }
    
    public void initComponent() {
        EventManager eventManager = getEventManager();
        eventManager.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, fileEditorManagerListener);
        eventManager.subscribe(ConnectionSetupChangeListener.TOPIC, connectionSetupListener);
        eventManager.subscribe(ObjectFilterChangeListener.TOPIC, filterChangeListener);
        eventManager.subscribe(ObjectDisplaySettingsChangeListener.TOPIC, objectDisplaySettingsChangeListener);
        eventManager.subscribe(ProjectTopics.MODULES, moduleListener);
    }

    public void disposeComponent() {
        connectionManagers.clear();
        if (toolWindowForm != null) {
            toolWindowForm.dispose();
            toolWindowForm = null;
        }
        super.disposeComponent();
    }

    /**
     *
     * @deprecated
     */
    public static void updateTree(final BrowserTreeElement treeElement, final int eventType) {
        new ConditionalLaterInvocator() {
            public void run() {
                DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(treeElement.getProject());
                BrowserToolWindowForm toolWindowForm = browserManager.getToolWindowForm();
                if (toolWindowForm != null) {
                    toolWindowForm.getBrowserForm().updateTreeNode(treeElement, eventType);
                }
            }
        }.start();
    }

    public static void scrollToSelectedElement(final ConnectionHandler connectionHandler) {
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(connectionHandler.getProject());
        BrowserToolWindowForm toolWindowForm = browserManager.getToolWindowForm();
        if (toolWindowForm != null) {
            final DatabaseBrowserTree browserTree = toolWindowForm.getBrowserTree(connectionHandler);
            if (browserTree != null && browserTree.getTargetSelection() != null) {
                new ConditionalLaterInvocator() {
                    public void run() {
                        browserTree.scrollToSelectedElement();
                    }
                }.start();
            }
        }
    }

    public void dispose() {
        connectionManagers.clear();
        EventManager eventManager = getEventManager();
        eventManager.unsubscribe(
                fileEditorManagerListener,
                connectionSetupListener,
                filterChangeListener,
                objectDisplaySettingsChangeListener,
                moduleListener);
    }

    public boolean isTabbedMode() {
        DatabaseBrowserSettings browserSettings = DatabaseBrowserSettings.getInstance(getProject());
        return browserSettings.getGeneralSettings().getDisplayMode() == BrowserDisplayMode.TABBED;
    }


    /***************************************
     *         JDOMExternalizable          *
     ***************************************/
    public void readExternal(Element element) throws InvalidDataException {
        autoscrollToEditor.readConfiguration(element);
        autoscrollFromEditor.readConfiguration(element);
        showObjectProperties.readConfiguration(element);
    }

    public void writeExternal(Element element) throws WriteExternalException {
        autoscrollToEditor.writeConfiguration(element);
        autoscrollFromEditor.writeConfiguration(element);
        showObjectProperties.writeConfiguration(element);
    }

    /***************************************
     *           ModuleListener            *
     ***************************************/

    /**********************************************************
     *                       Listeners                        *
     **********************************************************/
    private ModuleListener moduleListener = new ModuleAdapter() {
        public void moduleAdded(Project project, Module module) {
            ModuleConnectionManager connectionManager = ModuleConnectionManager.getInstance(module);
            if (connectionManager.getConnectionHandlers().size() > 0) {
                connectionManagers.add(connectionManager);
                Collections.sort(connectionManagers);
                rebuildConnectionLists();
            }
        }

        public void moduleRemoved(Project project, Module module) {
            ModuleConnectionManager connectionManager = ModuleConnectionManager.getInstance(module);
            if (connectionManagers.remove(connectionManager)) {
                rebuildConnectionLists();
            }
        }

        public void modulesRenamed(Project project, List<Module> modules) {
            boolean rebuild = false;
            for (Module module : modules) {
                ModuleConnectionManager connectionManager = ModuleConnectionManager.getInstance(module);
                if (connectionManager.getConnectionHandlers().size() > 0) {
                    rebuild = true;
                    break;
                }
            }

            if (rebuild) rebuildConnectionLists();
        }
    };

    private ConnectionSetupChangeListener connectionSetupListener = new ConnectionSetupChangeListener() {
        public void connectionSetupChanged() {
            if (!isRebuilding) {
                isRebuilding = true;
                rebuildConnectionLists();
                isRebuilding = false;
            }
        }
    };

    private ObjectFilterChangeListener filterChangeListener = new ObjectFilterChangeListener() {
        public void filterChanged(Filter<BrowserTreeElement> filter) {
            if (filter == getObjectFilter()) {
                getToolWindowForm().getBrowserForm().updateTree();
            } else {
                ConnectionHandler connectionHandler = getConnectionHandler(filter);
                if (connectionHandler != null) {
                    connectionHandler.getObjectBundle().rebuildTreeChildren();
                }
            }
        }

        private ConnectionHandler getConnectionHandler(Filter<BrowserTreeElement> filter) {
            for (ConnectionManager connectionManager : getConnectionManagers()) {
                for (ConnectionHandler connectionHandler : connectionManager.getConnectionHandlers()) {
                    if (filter == connectionHandler.getObjectFilter()) {
                        return connectionHandler;
                    }
                }
            }
            return null;
        }
    };

    public Filter<BrowserTreeElement> getObjectFilter() {
        DatabaseBrowserSettings browserSettings = DatabaseBrowserSettings.getInstance(getProject());
        return browserSettings.getFilterSettings().getObjectTypeFilterSettings().getElementFilter();
    }

    private ObjectDisplaySettingsChangeListener objectDisplaySettingsChangeListener = new ObjectDisplaySettingsChangeListener() {
        public void displayDetailsChanged() {
            getToolWindowForm().getBrowserForm().repaintTree();
        }
    };

    private FileEditorManagerListener fileEditorManagerListener = new FileEditorManagerAdapter() {
        public void fileOpened(FileEditorManager source, VirtualFile file) {
            if (scroll()) {
                if (file instanceof DatabaseEditableObjectFile) {
                    DatabaseEditableObjectFile databaseFile = (DatabaseEditableObjectFile) file;
                    navigateToElement(databaseFile.getObject());
                }
            }
        }

        public void selectionChanged(FileEditorManagerEvent event) {
            if (scroll()) {
                VirtualFile newFile = event.getNewFile();
                VirtualFile oldFile = event.getOldFile();

                if (newFile != oldFile) {
                    if (newFile instanceof DatabaseEditableObjectFile) {
                        DatabaseEditableObjectFile databaseFile = (DatabaseEditableObjectFile) newFile;
                        navigateToElement(databaseFile.getObject());
                    }

                    if (newFile instanceof SQLConsoleFile) {
                        SQLConsoleFile sqlConsoleFile = (SQLConsoleFile) newFile;
                        navigateToElement(sqlConsoleFile.getConnectionHandler().getObjectBundle());
                    }
                }
            }
        }
    };

    public void showObjectProperties(boolean visible) {
        BrowserToolWindowForm toolWindowForm = getToolWindowForm();
        if (visible)
            toolWindowForm.showObjectProperties(); else
            toolWindowForm.hideObjectProperties();
        showObjectProperties.setValue(visible);
    }

    public List<DBObject> getSelectedObjects() {
        List<DBObject> selectedObjects = new ArrayList<DBObject>();
        TreePath[] selectionPaths = getActiveBrowserTree().getSelectionPaths();
        if (selectionPaths != null) {
            for (TreePath treePath : selectionPaths) {
                Object lastPathComponent = treePath.getLastPathComponent();
                if (lastPathComponent instanceof DBObject) {
                    DBObject object = (DBObject) lastPathComponent;
                    selectedObjects.add(object);
                }
            }
        }
        return selectedObjects;
    }
}
