package com.dci.intellij.dbn.connection.transaction;

import com.dci.intellij.dbn.common.AbstractProjectComponent;
import com.dci.intellij.dbn.common.event.EventManager;
import com.dci.intellij.dbn.common.notification.NotificationUtil;
import com.dci.intellij.dbn.common.option.InteractiveOptionHandler;
import com.dci.intellij.dbn.common.thread.ModalTask;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionStatusListener;
import com.dci.intellij.dbn.connection.transaction.ui.UncommittedChangesDialog;
import com.dci.intellij.dbn.connection.transaction.ui.UncommittedChangesOverviewDialog;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class DatabaseTransactionManager extends AbstractProjectComponent implements ProjectManagerListener{
    private InteractiveOptionHandler toggleAutoCommitOptionHandler = new InteractiveOptionHandler(
            "Uncommitted changes",
            "You have uncommitted changes on the connection \"{0}\". \n" +
            "Please specify whether to commit or rollback these changes before switching Auto-Commit ON.",
            2, "Commit", "Rollback", "Review Changes", "Cancel");

    private InteractiveOptionHandler disconnectOptionHandler = new InteractiveOptionHandler(
            "Uncommitted changes",
            "You have uncommitted changes on the connection \"{0}\". \n" +
            "Please specify whether to commit or rollback these changes before disconnecting",
            2, "Commit", "Rollback", "Review Changes", "Cancel");


    private DatabaseTransactionManager(Project project) {
        super(project);
        ProjectManager projectManager = ProjectManager.getInstance();
        projectManager.addProjectManagerListener(project, this);
    }

    public static DatabaseTransactionManager getInstance(Project project) {
        return project.getComponent(DatabaseTransactionManager.class);
    }

    public void execute(final ConnectionHandler connectionHandler, boolean inBackground, final TransactionAction... actions) {
        final Project project = connectionHandler.getProject();
        final String connectionName = connectionHandler.getName();
        final TransactionListener transactionListener = EventManager.notify(getProject(), TransactionListener.TOPIC);
        new ModalTask(project, "Performing " + actions[0].getName() + " on connection " + connectionName, inBackground) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                for (TransactionAction action : actions) {
                    if (action != null) {
                        boolean success = true;
                        try {
                            // notify pre-action
                            transactionListener.beforeAction(connectionHandler, action);

                            indicator.setIndeterminate(true);
                            indicator.setText("Performing " + action.getName() + " on connection " + connectionName);
                            action.execute(connectionHandler);
                            NotificationUtil.sendNotification(
                                    project,
                                    action.getSuccessNotificationType(),
                                    action.getName(),
                                    action.getSuccessNotificationMessage(),
                                    connectionName);
                        } catch (SQLException ex) {
                            NotificationUtil.sendErrorNotification(
                                    project,
                                    action.getName(),
                                    action.getErrorNotificationMessage(),
                                    connectionName,
                                    ex.getMessage());
                            success = false;
                        } finally {
                            // notify post-action
                            transactionListener.afterAction(connectionHandler, action, success);

                            if (action.isStatusChange()) {
                                ConnectionStatusListener statusListener = EventManager.notify(getProject(), ConnectionStatusListener.TOPIC);
                                statusListener.statusChanged(connectionHandler.getId());
                            }
                        }
                    }
                }
            }
        }.start();
    }

    public void commit(final ConnectionHandler connectionHandler, boolean background) {
        execute(connectionHandler, background, TransactionAction.COMMIT);
    }

    public void rollback(final ConnectionHandler connectionHandler, boolean background) {
        execute(connectionHandler, background, TransactionAction.ROLLBACK);
    }

    public void disconnect(final ConnectionHandler connectionHandler, boolean background) {
        execute(connectionHandler, background, TransactionAction.DISCONNECT);
    }


    public boolean showUncommittedChangesOverviewDialog(TransactionAction additionalOperation) {
        UncommittedChangesOverviewDialog executionDialog = new UncommittedChangesOverviewDialog(getProject(), additionalOperation);
        executionDialog.show();
        return executionDialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }

    public boolean showUncommittedChangesDialog(ConnectionHandler connectionHandler, TransactionAction additionalOperation) {
        UncommittedChangesDialog executionDialog = new UncommittedChangesDialog(connectionHandler, additionalOperation, false);
        executionDialog.show();
        return executionDialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }

    public void toggleAutoCommit(ConnectionHandler connectionHandler) {
        boolean isAutoCommit = connectionHandler.isAutoCommit();
        TransactionAction autoCommitAction = isAutoCommit ?
                TransactionAction.TURN_AUTO_COMMIT_OFF :
                TransactionAction.TURN_AUTO_COMMIT_ON;

        if (!isAutoCommit && connectionHandler.hasUncommittedChanges()) {
            int result = toggleAutoCommitOptionHandler.resolve(connectionHandler.getName());
            switch (result) {
                case 0: execute(connectionHandler, true, TransactionAction.COMMIT, autoCommitAction); break;
                case 1: execute(connectionHandler, true, TransactionAction.ROLLBACK, autoCommitAction); break;
                case 2: showUncommittedChangesDialog(connectionHandler, autoCommitAction);
            }
        } else {
            execute(connectionHandler, false, autoCommitAction);
        }
    }

    public void disconnect(ConnectionHandler connectionHandler) {
        if (connectionHandler.hasUncommittedChanges()) {
            int result = disconnectOptionHandler.resolve(connectionHandler.getName());

            switch (result) {
                case 0: execute(connectionHandler, false, TransactionAction.COMMIT, TransactionAction.DISCONNECT); break;
                case 1: execute(connectionHandler, false, TransactionAction.DISCONNECT); break;
                case 2: showUncommittedChangesDialog(connectionHandler, TransactionAction.DISCONNECT);
            }
        } else {
            execute(connectionHandler, false, TransactionAction.DISCONNECT);
        }
    }

    /**********************************************
    *            ProjectManagerListener           *
    ***********************************************/

    @Override
    public void projectOpened(Project project) {

    }

    @Override
    public boolean canCloseProject(Project project) {
        return true;
    }

    @Override
    public void projectClosed(Project project) {
    }

    @Override
    public void projectClosing(Project project) {
    }

    /**********************************************
    *                ProjectComponent             *
    ***********************************************/
    @NonNls
    @NotNull
    public String getComponentName() {
        return "DBNavigator.Project.TransactionManager";
    }
}