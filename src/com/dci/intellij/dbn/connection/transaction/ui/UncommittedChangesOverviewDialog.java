package com.dci.intellij.dbn.connection.transaction.ui;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.ui.DBNDialog;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionOperation;
import com.dci.intellij.dbn.connection.transaction.DatabaseTransactionManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import java.awt.event.ActionEvent;
import java.util.List;

public class UncommittedChangesOverviewDialog extends DBNDialog{
    private UncommittedChangesOverviewForm mainComponent;
    private ConnectionOperation additionalOperation;

    public UncommittedChangesOverviewDialog(Project project, ConnectionOperation additionalOperation) {
        super(project, "Uncommitted changes overview", true);
        this.additionalOperation = additionalOperation;
        mainComponent = new UncommittedChangesOverviewForm(project);
        setModal(false);
        setResizable(true);
        init();
    }

    protected String getDimensionServiceKey() {
        return "DBNavigator.UncommittedChangesOverview";
    }

    protected final Action[] createActions() {
        return new Action[]{
                new CommitAllAction(),
                new RollbackAllAction(),
                getCancelAction(),
                getHelpAction()
        };
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }

    private class CommitAllAction extends AbstractAction {
        public CommitAllAction() {
            super("Commit all", Icons.CONNECTION_COMMIT);
        }

        public void actionPerformed(ActionEvent e) {
            DatabaseTransactionManager transactionManager = getTransactionManager();
            List<ConnectionHandler> connectionHandlers = mainComponent.getConnectionHandlers();

            doOKAction();
            for (ConnectionHandler connectionHandler : connectionHandlers) {
                transactionManager.execute(connectionHandler, true, ConnectionOperation.COMMIT, additionalOperation);
            }
        }

        @Override
        public boolean isEnabled() {
            return mainComponent.hasUncommittedChanges();
        }
    }

    private class RollbackAllAction extends AbstractAction {
        public RollbackAllAction() {
            super("Rollback all", Icons.CONNECTION_ROLLBACK);
        }

        public void actionPerformed(ActionEvent e) {
            DatabaseTransactionManager transactionManager = getTransactionManager();
            List<ConnectionHandler> connectionHandlers = mainComponent.getConnectionHandlers();

            doOKAction();
            for (ConnectionHandler connectionHandler : connectionHandlers) {
                transactionManager.execute(connectionHandler, true, ConnectionOperation.ROLLBACK, additionalOperation);
            }
        }

        @Override
        public boolean isEnabled() {
            return mainComponent.hasUncommittedChanges();
        }
    }

    private DatabaseTransactionManager getTransactionManager() {
        return DatabaseTransactionManager.getInstance(getProject());
    }


    @Nullable
    protected JComponent createCenterPanel() {
        return mainComponent.getComponent();
    }

    @Override
    protected void dispose() {
        super.dispose();
        mainComponent.dispose();
        mainComponent = null;
    }
}
