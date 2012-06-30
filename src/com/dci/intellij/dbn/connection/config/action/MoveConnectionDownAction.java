package com.dci.intellij.dbn.connection.config.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.ui.ListUtil;

import javax.swing.JList;

public class MoveConnectionDownAction extends DumbAwareAction {
    private JList list;
    private ConnectionManager connectionManager;

    public MoveConnectionDownAction(JList list, ConnectionManager connectionManager) {
        super("Move selection down", null, Icons.ACTION_MOVE_DOWN);
        this.list = list;
        this.connectionManager = connectionManager;
    }

    public void actionPerformed(AnActionEvent anActionEvent) {
        connectionManager.setModified(true);
        ListUtil.moveSelectedItemsDown(list);
    }

    public void update(AnActionEvent e) {
        int length = list.getSelectedValues().length;
        boolean enabled = length > 0 && list.getMaxSelectionIndex() < list.getModel().getSize() - 1;
        e.getPresentation().setEnabled(enabled);
    }
}
