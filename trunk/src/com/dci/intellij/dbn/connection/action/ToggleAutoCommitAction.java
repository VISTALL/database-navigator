package com.dci.intellij.dbn.connection.action;

import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;

import java.sql.SQLException;

public class ToggleAutoCommitAction extends DumbAwareAction {
    private ConnectionHandler connectionHandler;

    public ToggleAutoCommitAction(ConnectionHandler connectionHandler) {
        super("Turn Auto-Commit ON/OFF");
        this.connectionHandler = connectionHandler;

    }

    public void actionPerformed(AnActionEvent e) {
        boolean autoCommit = connectionHandler.isAutoCommit();
        try {
            connectionHandler.setAutoCommit(!autoCommit);
        } catch (SQLException e1) {
            MessageUtil.showErrorDialog("Could not change Auto-Commit property", e1);
        }
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setText(
                connectionHandler.isAutoCommit() ?
                        "Turn Auto-Commit OFF" :
                        "Turn Auto-Commit ON");
    }
}
