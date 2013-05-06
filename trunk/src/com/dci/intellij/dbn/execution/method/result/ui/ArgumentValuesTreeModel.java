package com.dci.intellij.dbn.execution.method.result.ui;

import com.dci.intellij.dbn.execution.method.ArgumentValue;
import com.dci.intellij.dbn.object.DBArgument;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.DBTypeAttribute;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentValuesTreeModel implements TreeModel {
    private ArgumentValuesTreeNode root;
    public ArgumentValuesTreeModel(DBMethod method, List<ArgumentValue> inputArgumentValues, List<ArgumentValue> outputArgumentValues) {
        root = new ArgumentValuesTreeNode(null, method);
        ArgumentValuesTreeNode inputNode = new ArgumentValuesTreeNode(root, "Input");
        ArgumentValuesTreeNode outputNode = new ArgumentValuesTreeNode(root, "Output");

        createArgumentValueNodes(inputNode, inputArgumentValues);
        createArgumentValueNodes(outputNode, outputArgumentValues);
    }

    private void createArgumentValueNodes(ArgumentValuesTreeNode parentNode, List<ArgumentValue> inputArgumentValues) {
        Map<DBArgument, ArgumentValuesTreeNode> nodeMap = new HashMap<DBArgument, ArgumentValuesTreeNode>();
        for (ArgumentValue argumentValue : inputArgumentValues) {
            DBArgument argument = argumentValue.getArgument();
            DBTypeAttribute attribute = argumentValue.getAttribute();

            if (attribute == null) {
                // single value
                new ArgumentValuesTreeNode(parentNode, argumentValue);
            } else {
                // multiple attribute values
                ArgumentValuesTreeNode treeNode = nodeMap.get(argument);
                if (treeNode == null) {
                    treeNode = new ArgumentValuesTreeNode(parentNode, argument);
                    nodeMap.put(argument, treeNode);
                }
                new ArgumentValuesTreeNode(treeNode, argumentValue);
            }
        }
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        TreeNode treeNode = (TreeNode) parent;
        return treeNode.getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        TreeNode treeNode = (TreeNode) parent;
        return treeNode.getChildCount();
    }

    @Override
    public boolean isLeaf(Object node) {
        TreeNode treeNode = (TreeNode) node;
        return treeNode.isLeaf();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        TreeNode treeNode = (TreeNode) parent;
        TreeNode childNode = (TreeNode) child;
        return treeNode.getIndex(childNode);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
    }
}
