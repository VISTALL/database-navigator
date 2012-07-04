package com.dci.intellij.dbn.connection.action;

import com.dci.intellij.dbn.common.thread.BackgroundTask;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class ShowDatabaseInformationAction extends DumbAwareAction {
    private ConnectionHandler connectionHandler;

    public ShowDatabaseInformationAction(ConnectionHandler connectionHandler) {
        super("Connection Information", null, null);
        this.connectionHandler = connectionHandler;
        //getTemplatePresentation().setEnabled(connectionHandler.getConnectionStatus().isConnected());
    }

    public void actionPerformed(AnActionEvent anActionEvent) {
        new BackgroundTask(connectionHandler.getProject(), "Loading database information for " + connectionHandler.getName(), false) {
            @Override
            public void execute(@NotNull ProgressIndicator progressIndicator) {
                initProgressIndicator(progressIndicator, true);
                ConnectionManager connectionManager = connectionHandler.getConnectionManager();
                connectionManager.showConnectionInfo(connectionHandler.getSettings());
            }
        }.start();

    }
}
