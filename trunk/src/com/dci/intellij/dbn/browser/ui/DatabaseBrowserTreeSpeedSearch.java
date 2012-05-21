package com.dci.intellij.dbn.browser.ui;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectBundle;
import com.intellij.ui.SpeedSearchBase;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

public class DatabaseBrowserTreeSpeedSearch extends SpeedSearchBase<JTree> {
    private static final Object[] EMPTY_ARRAY = new Object[0];
    private DatabaseBrowserTree tree;
    private Object[] elements = null;

    public DatabaseBrowserTreeSpeedSearch(DatabaseBrowserTree tree) {
        super(tree);
        this.tree = tree;
        this.tree.getModel().addTreeModelListener(treeModelListener);
    }

    protected int getSelectedIndex() {
        Object[] elements = getAllElements();
        BrowserTreeElement treeElement = getSelectedTreeElement();
        if (treeElement != null) {
            for (int i=0; i<elements.length; i++) {
                if (treeElement == elements[i]) {
                    return i;
                }
            }
        }
        return -1;
    }

    private BrowserTreeElement getSelectedTreeElement() {
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath != null) {
            return (BrowserTreeElement) selectionPath.getLastPathComponent();
        }
        return null;
    }

    protected Object[] getAllElements() {
        if (elements == null) {
            List<BrowserTreeElement> elements = new ArrayList<BrowserTreeElement>();
            BrowserTreeElement root = tree.getModel().getRoot();
            loadElements(elements, root);
            this.elements = elements.toArray();
        }
        return elements;
    }

    private void loadElements(List<BrowserTreeElement> elements, BrowserTreeElement browserTreeElement) {
        if (browserTreeElement.isTreeStructureLoaded()) {
            if (browserTreeElement instanceof ConnectionManager) {
                ConnectionManager connectionManager = (ConnectionManager) browserTreeElement;
                for (ConnectionHandler connectionHandler : connectionManager.getConnectionHandlers()){
                    DBObjectBundle objectBundle = connectionHandler.getObjectBundle();
                    loadElements(elements, objectBundle);
                }
            }
            else {
                for (BrowserTreeElement treeElement : browserTreeElement.getTreeChildren()) {
                    if (treeElement instanceof DBObject) {
                        elements.add(treeElement);
                    }
                    loadElements(elements, treeElement);
                }
            }
        }
    }

    protected String getElementText(Object o) {
        BrowserTreeElement treeElement = (BrowserTreeElement) o;
        return treeElement.getPresentableText();
    }

    protected void selectElement(Object o, String s) {
        BrowserTreeElement treeElement = (BrowserTreeElement) o;
        tree.selectElement(treeElement, false);

/*
        TreePath treePath = DatabaseBrowserUtils.createTreePath(treeElement);
        tree.setSelectionPath(treePath);
        tree.scrollPathToVisible(treePath);
*/
    }

    TreeModelListener treeModelListener = new TreeModelListener() {

        public void treeNodesChanged(TreeModelEvent e) {
            elements = null;
        }

        public void treeNodesInserted(TreeModelEvent e) {
            elements = null;
        }

        public void treeNodesRemoved(TreeModelEvent e) {
            elements = null;
        }

        public void treeStructureChanged(TreeModelEvent e) {
            elements = null;
        }
    };
}
