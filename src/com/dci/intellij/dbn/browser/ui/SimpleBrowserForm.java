package com.dci.intellij.dbn.browser.ui;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.browser.model.BrowserTreeModel;
import com.dci.intellij.dbn.browser.model.SimpleBrowserTreeModel;
import com.dci.intellij.dbn.browser.model.TabbedBrowserTreeModel;
import com.dci.intellij.dbn.common.ui.UIFormImpl;
import com.dci.intellij.dbn.common.ui.tree.TreeUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.util.IncorrectOperationException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;

public class SimpleBrowserForm extends UIFormImpl implements DatabaseBrowserForm {
    private JPanel mainPanel;
    private JScrollPane browserScrollPane;
    private DatabaseBrowserTree browserTree;

    public SimpleBrowserForm(DatabaseBrowserManager browserManager) {
        this(new SimpleBrowserTreeModel(
                browserManager.getProject(),
                browserManager.getConnectionManagers()));
    }

    public SimpleBrowserForm(BrowserTreeModel treeModel) {
        browserTree = new DatabaseBrowserTree(treeModel);
        browserScrollPane.setViewportView(browserTree);
        browserScrollPane.setBorder(new EmptyBorder(1,0,0,0));
        ToolTipManager.sharedInstance().registerComponent(browserTree);
    }
    
    public ConnectionHandler getConnectionHandler(){
        if (browserTree.getModel() instanceof TabbedBrowserTreeModel) {
            TabbedBrowserTreeModel treeModel = (TabbedBrowserTreeModel) browserTree.getModel();
            return treeModel.getConnectionHandler();
        }
        throw new IncorrectOperationException("Multiple connection tabs can not return one connection.");
    }

    public JComponent getComponent() {
        return mainPanel;
    }

    public DatabaseBrowserTree getBrowserTree() {
        return browserTree;
    }

    public void selectElement(BrowserTreeElement treeElement, boolean requestFocus) {
        browserTree.selectElement(treeElement, requestFocus);
    }

    public void updateTreeNode(BrowserTreeElement treeElement, int eventType) {
        if (browserTree != null) {
            BrowserTreeModel treeModel = browserTree.getModel();
            treeModel.notifyTreeModelListeners(treeElement, eventType);
        }
    }

    public void updateTree() {
        browserTree.getModel().getRoot().rebuildTreeChildren();
    }

    public void repaintTree() {
        browserTree.repaint();
    }

    public void rebuild() {
        BrowserTreeModel treeModel = browserTree.getModel();
        BrowserTreeElement treeElement = treeModel.getRoot();
        treeModel.notifyTreeModelListeners(treeElement, TreeUtil.STRUCTURE_CHANGED);
    }

    public void dispose() {
        super.dispose();
        browserTree.dispose();
        browserTree = null;
    }

}
