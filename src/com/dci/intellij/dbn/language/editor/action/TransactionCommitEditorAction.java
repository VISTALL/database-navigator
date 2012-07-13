package com.dci.intellij.dbn.language.editor.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.transaction.DatabaseTransactionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public class TransactionCommitEditorAction extends TransactionEditorAction {
    public TransactionCommitEditorAction() {
        super("Commit", "Commit changes", Icons.CONNECTION_COMMIT);
    }

    public void actionPerformed(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        DatabaseTransactionManager transactionManager = DatabaseTransactionManager.getInstance(project);
        ConnectionHandler activeConnection = getConnectionHandler(project);
        transactionManager.commit(activeConnection);
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