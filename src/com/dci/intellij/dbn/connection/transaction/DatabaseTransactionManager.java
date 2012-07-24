package com.dci.intellij.dbn.connection.transaction;

import com.dci.intellij.dbn.common.AbstractProjectComponent;
import com.dci.intellij.dbn.common.notification.NotificationUtil;
import com.dci.intellij.dbn.common.option.InteractiveOptionHandler;
import com.dci.intellij.dbn.common.thread.ModalTask;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionOperation;
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

    public void execute(final ConnectionHandler connectionHandler, boolean inBackground, final ConnectionOperation ... operations) {
        final Project project = connectionHandler.getProject();
        final String connectionName = connectionHandler.getName();
        new ModalTask(project, "Performing " + operations[0].getName() + " on connection " + connectionName, inBackground) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                for (ConnectionOperation operation : operations) {
                    if (operation != null) {

                        try {
                            indicator.setIndeterminate(true);
                            indicator.setText("Performing " + operation.getName() + " on connection " + connectionName);
                            operation.execute(connectionHandler);
                            NotificationUtil.sendInfoNotification(project, operation.getName(), operation.getSuccessMessage(), connectionName);
                        } catch (SQLException ex) {
                            //MessageUtil.showErrorDialog("Could not perform " + operation.getName() + " operation.", ex);
                            NotificationUtil.sendErrorNotification(project, operation.getName(), operation.getFailureMessage(), connectionName, ex.getMessage());
                        }
                    }
                }
            }
        }.start();

    }

    public void commit(final ConnectionHandler connectionHandler, boolean background) {
        execute(connectionHandler, background, ConnectionOperation.COMMIT);
    }

    public void rollback(final ConnectionHandler connectionHandler, boolean background) {
        execute(connectionHandler, background, ConnectionOperation.ROLLBACK);
    }

    public void disconnect(final ConnectionHandler connectionHandler, boolean background) {
        execute(connectionHandler, background, ConnectionOperation.DISCONNECT);
    }


    public boolean showUncommittedChangesOverviewDialog(ConnectionOperation additionalOperation) {
        UncommittedChangesOverviewDialog executionDialog = new UncommittedChangesOverviewDialog(getProject(), additionalOperation);
        executionDialog.show();
        return executionDialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }

    public boolean showUncommittedChangesDialog(ConnectionHandler connectionHandler, ConnectionOperation additionalOperation) {
        UncommittedChangesDialog executionDialog = new UncommittedChangesDialog(connectionHandler, additionalOperation, false);
        executionDialog.show();
        return executionDialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }

    public void toggleAutoCommit(ConnectionHandler connectionHandler) {
        boolean isAutoCommit = connectionHandler.isAutoCommit();
        if (!isAutoCommit) {
            int result = toggleAutoCommitOptionHandler.resolve(connectionHandler.getName());
            switch (result) {
                case 0: execute(connectionHandler, true, ConnectionOperation.COMMIT, ConnectionOperation.TOGGLE_AUTO_COMMIT); break;
                case 1: execute(connectionHandler, true, ConnectionOperation.ROLLBACK, ConnectionOperation.TOGGLE_AUTO_COMMIT); break;
                case 2: showUncommittedChangesDialog(connectionHandler, ConnectionOperation.TOGGLE_AUTO_COMMIT);
            }
        } else {
            execute(connectionHandler, false, ConnectionOperation.TOGGLE_AUTO_COMMIT);
        }
    }

    public void disconnect(ConnectionHandler connectionHandler) {
        if (connectionHandler.hasUncommittedChanges()) {
            int result = disconnectOptionHandler.resolve(connectionHandler.getName());

            switch (result) {
                case 0: execute(connectionHandler, false, ConnectionOperation.COMMIT, ConnectionOperation.DISCONNECT); break;
                case 1: execute(connectionHandler, false, ConnectionOperation.DISCONNECT); break;
                case 2: showUncommittedChangesDialog(connectionHandler, ConnectionOperation.DISCONNECT);
            }
        } else {
            execute(connectionHandler, false, ConnectionOperation.DISCONNECT);
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