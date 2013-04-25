package com.dci.intellij.dbn.language.editor.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.transaction.DatabaseTransactionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public class TransactionRollbackEditorAction extends TransactionEditorAction {
    public TransactionRollbackEditorAction() {
        super("Rollback", "Rollback changes", Icons.CONNECTION_ROLLBACK);
    }

    public void actionPerformed(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        DatabaseTransactionManager transactionManager = DatabaseTransactionManager.getInstance(project);
        ConnectionHandler activeConnection = getConnectionHandler(project, e.getPlace());
        transactionManager.rollback(activeConnection, true, false);
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        e.getPresentation().setText("Rollback");

        Project project = ActionUtil.getProject(e);
        ConnectionHandler connectionHandler = getConnectionHandler(project, e.getPlace());
        e.getPresentation().setVisible(connectionHandler != null && !connectionHandler.isAutoCommit());
    }

}
