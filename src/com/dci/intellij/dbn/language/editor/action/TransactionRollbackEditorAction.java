package com.dci.intellij.dbn.language.editor.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.thread.ModalTask;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.mapping.FileConnectionMappingManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class TransactionRollbackEditorAction extends TransactionEditorAction {
    public TransactionRollbackEditorAction() {
        super("Rollback", "Rollback changes", Icons.CONNECTION_ROLLBACK);
    }

    public void actionPerformed(AnActionEvent e) {
        final Project project = ActionUtil.getProject(e);
        FileConnectionMappingManager connectionMappingManager = FileConnectionMappingManager.getInstance(project);
        final ConnectionHandler activeConnection = connectionMappingManager.lookupActiveConnectionForEditor();

        new ModalTask(project, "Performing rollback on connection " + activeConnection.getName(), false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    indicator.setIndeterminate(true);
                    activeConnection.rollback();
                } catch (SQLException ex) {
                    MessageUtil.showErrorDialog("Could not perform rollback operation.", ex);
                }
            }
        }.start();
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        e.getPresentation().setText("Rollback");
    }

}
