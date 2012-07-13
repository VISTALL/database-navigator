package com.dci.intellij.dbn.connection.transaction.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.thread.ModalTask;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class TransactionCommitAction extends DumbAwareAction {
    private ConnectionHandler connectionHandler;

    public TransactionCommitAction(ConnectionHandler connectionHandler) {
        super("Commit", "Commit connection", Icons.CONNECTION_COMMIT);
        this.connectionHandler = connectionHandler;

    }

    public void actionPerformed(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        new ModalTask(project, "Performing commit on connection " + connectionHandler.getName(), false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    indicator.setIndeterminate(true);
                    connectionHandler.rollback();
                } catch (SQLException ex) {
                    MessageUtil.showErrorDialog("Could not perform commit operation.", ex);
                }
            }
        }.start();
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(connectionHandler.hasUncommittedChanges());
        e.getPresentation().setVisible(!connectionHandler.isAutoCommit());
    }
}
