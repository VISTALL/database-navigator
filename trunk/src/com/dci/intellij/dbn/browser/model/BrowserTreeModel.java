package com.dci.intellij.dbn.browser.model;

import com.dci.intellij.dbn.browser.DatabaseBrowserUtils;
import com.dci.intellij.dbn.common.ui.tree.TreeUtil;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.HashSet;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.Set;

public abstract class BrowserTreeModel implements TreeModel, Disposable {
    private Set<TreeModelListener> treeModelListeners = new HashSet<TreeModelListener>();
    private BrowserTreeElement root;
    private boolean isDisposed  = false;

    protected BrowserTreeModel(BrowserTreeElement root) {
        this.root = root;
    }

    public void addTreeModelListener(TreeModelListener listener) {
        treeModelListeners.add(listener);
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        treeModelListeners.remove(listener);
    }

    public void notifyTreeModelListeners(final BrowserTreeElement treeElement, final int eventType) {
        if (!isDisposed && !treeElement.isDisposed()) {
            TreePath treePath = DatabaseBrowserUtils.createTreePath(treeElement);
            TreeUtil.notifyTreeModelListeners(this, treeModelListeners, treePath, eventType);
        }
    }

    public Project getProject() {
        return root.getProject();
    }



    /***************************************
     *              TreeModel              *
     ***************************************/
    public BrowserTreeElement getRoot() {
        return root;
    }

    public Object getChild(Object parent, int index) {
        return ((BrowserTreeElement) parent).getTreeChild(index);
    }

    public int getChildCount(Object parent) {
        return ((BrowserTreeElement) parent).getTreeChildCount();
    }

    public boolean isLeaf(Object node) {
        return ((BrowserTreeElement) node).isLeafTreeElement();
    }

    public int getIndexOfChild(Object parent, Object child) {
        return ((BrowserTreeElement) parent).getIndexOfTreeChild((BrowserTreeElement) child);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {

    }

    public void dispose() {
        isDisposed = true;
        treeModelListeners.clear();
        treeModelListeners = null;
        root = null;
    }


}
