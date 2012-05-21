package com.dci.intellij.dbn.common.ui.tree;

import com.intellij.util.ui.Tree;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

public class DBNTree extends Tree {
    public DBNTree() {
        setTransferHandler(new DBNTreeTransferHandler());
    }

    public DBNTree(TreeModel treemodel) {
        super(treemodel);
        setTransferHandler(new DBNTreeTransferHandler());
    }

    public DBNTree(TreeNode root) {
        super(root);
        setTransferHandler(new DBNTreeTransferHandler());
    }
}
