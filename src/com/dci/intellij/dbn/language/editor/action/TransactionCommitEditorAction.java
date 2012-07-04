package com.dci.intellij.dbn.language.editor.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.thread.ModalTask;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class TransactionCommitEditorAction extends TransactionEditorAction {
    public TransactionCommitEditorAction() {
        super("Commit", "Commit changes", Icons.CONNECTION_COMMIT);
    }

    public void actionPerformed(AnActionEvent e) {
        final Project project = ActionUtil.getProject(e);
        final ConnectionHandler activeConnection = getConnectionHandler(project);

        new ModalTask(project, "Performing commit on connection " + activeConnection.getName(), false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    indicator.setIndeterminate(true);
                    activeConnection.commit();
                } catch (SQLException ex) {
                    MessageUtil.showErrorDialog("Could not perform commit operation.", ex);
                }
            }
        }.start();
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        e.getPresentation().setText("Commit");

        Project project = ActionUtil.getProject(e);
        ConnectionHandler connectionHandler = getConnectionHandler(project);
        e.getPresentation().setVisible(connectionHandler != null && !connectionHandler.isAutoCommit());
    }

}