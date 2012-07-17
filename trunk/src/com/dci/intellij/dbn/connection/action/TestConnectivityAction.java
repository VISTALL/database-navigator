package com.dci.intellij.dbn.connection.action;

import com.dci.intellij.dbn.common.thread.BackgroundTask;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class TestConnectivityAction extends DumbAwareAction {
    private ConnectionHandler connectionHandler;

    public TestConnectivityAction(ConnectionHandler connectionHandler) {
        super("Test connectivity", "Test connectivity of " + connectionHandler.getName(), null);
        this.connectionHandler = connectionHandler;
        getTemplatePresentation().setEnabled(!connectionHandler.getConnectionStatus().isConnected());
    }

    public void actionPerformed(AnActionEvent anActionEvent) {
        new BackgroundTask(connectionHandler.getProject(), "Trying to connect to " + connectionHandler.getName(), false) {
            @Override
            public void execute(@NotNull ProgressIndicator progressIndicator) {
                initProgressIndicator(progressIndicator, true);
                connectionHandler.getConnectionManager().testConnection(connectionHandler, true);
            }
        }.start();

    }
}
