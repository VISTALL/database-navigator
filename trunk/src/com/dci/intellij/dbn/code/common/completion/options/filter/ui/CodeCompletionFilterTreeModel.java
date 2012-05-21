package com.dci.intellij.dbn.code.common.completion.options.filter.ui;

import com.dci.intellij.dbn.code.common.completion.options.filter.CodeCompletionFilterSettings;
import com.intellij.ui.CheckedTreeNode;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class CodeCompletionFilterTreeModel implements TreeModel {
    private CodeCompletionFilterTreeNode root;

    public CodeCompletionFilterTreeModel(CodeCompletionFilterSettings setup) {
        root = (CodeCompletionFilterTreeNode) setup.createCheckedTreeNode();
    }

    public void applyChanges() {
        root.applyChanges();
    }

    public void resetChanges() {
        root.resetChanges();        
    }

    public Object getRoot() {
        return root;
    }

    public Object getChild(Object o, int i) {
        CheckedTreeNode node = (CheckedTreeNode) o;
        return node.getChildAt(i);
    }

    public int getChildCount(Object o) {
        CheckedTreeNode node = (CheckedTreeNode) o;
        return node.getChildCount();
    }

    public boolean isLeaf(Object o) {
        CheckedTreeNode node = (CheckedTreeNode) o;
        return node.isLeaf();
    }


    public int getIndexOfChild(Object o, Object o1) {
        CheckedTreeNode node = (CheckedTreeNode) o;
        return node.getIndex((TreeNode) o1);
    }

    public void addTreeModelListener(TreeModelListener treeModelListener) {}
    public void removeTreeModelListener(TreeModelListener treeModelListener) {}
    public void valueForPathChanged(TreePath treePath, Object o) {}

}
