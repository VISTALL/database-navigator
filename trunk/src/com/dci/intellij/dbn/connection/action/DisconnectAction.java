package com.dci.intellij.dbn.connection.action;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;

public class DisconnectAction extends DumbAwareAction {
    private ConnectionHandler connectionHandler;

    public DisconnectAction(ConnectionHandler connectionHandler) {
        super("Disconnect", "Disconnect from " + connectionHandler.getName(), null);
        this.connectionHandler = connectionHandler;
        getTemplatePresentation().setEnabled(connectionHandler.getConnectionStatus().isConnected());
    }

    public void actionPerformed(AnActionEvent anActionEvent) {
        connectionHandler.disconnect();
    }
}
