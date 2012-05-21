package com.dci.intellij.dbn.common.ui.tree;

import com.dci.intellij.dbn.common.LoggerFactory;
import com.intellij.openapi.diagnostic.Logger;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TreeUtil {
    private static final Logger LOGGER = LoggerFactory.createLogger();
    public static final int NODES_ADDED = 0;
    public static final int NODES_REMOVED = 1;
    public static final int NODES_CHANGED = 2;
    public static final int STRUCTURE_CHANGED = 3;

    public static TreePath createTreePath(TreeNode treeNode) {
        List<TreeNode> list =  new ArrayList<TreeNode>();
        list.add(treeNode);
        TreeNode parent = treeNode.getParent();
        while (parent != null) {
            list.add(0, parent);
            parent = parent.getParent();
        }
        return new TreePath(list.toArray());
    }

    public static void collapseAll(JTree tree) {
        int row = tree.getRowCount() - 1;
        while (row >= 0) {
            tree.collapseRow(row);
            row--;
        }
    }

    public static void expandAll(JTree tree) {
        tree.expandPath(new TreePath(tree.getModel().getRoot()));
        int oldRowCount = 0;
        do {
            int rowCount = tree.getRowCount();
            if (rowCount == oldRowCount) break;
            oldRowCount = rowCount;
            for (int i = 0; i < rowCount; i++) {
                tree.expandRow(i);
            }
        }
        while (true);
    }

    public static void notifyTreeModelListeners(Object source, Set<TreeModelListener> treeModelListeners, TreeNode node, int eventType) {
        TreePath treePath = createTreePath(node);
        notifyTreeModelListeners(source, treeModelListeners, treePath, eventType);
    }

    public static void notifyTreeModelListeners(Object source, Set<TreeModelListener> treeModelListeners, TreePath path, int eventType) {
        TreeModelEvent event = new TreeModelEvent(source, path);
        notifyTreeModelListeners(treeModelListeners, eventType, event);
    }

    private static void notifyTreeModelListeners(final Set<TreeModelListener> treeModelListeners, final int eventType, final TreeModelEvent event) {
        try {
            for (TreeModelListener treeModelListener : treeModelListeners) {
                switch (eventType) {
                    case TreeUtil.NODES_ADDED:       treeModelListener.treeNodesInserted(event);    break;
                    case TreeUtil.NODES_REMOVED:     treeModelListener.treeNodesRemoved(event);     break;
                    case TreeUtil.NODES_CHANGED:     treeModelListener.treeNodesChanged(event);     break;
                    case TreeUtil.STRUCTURE_CHANGED: treeModelListener.treeStructureChanged(event); break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error notifying tree model listeners", e);
        }

    }
}
