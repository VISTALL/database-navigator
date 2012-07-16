package com.dci.intellij.dbn.connection.transaction;

import com.dci.intellij.dbn.common.AbstractProjectComponent;
import com.dci.intellij.dbn.common.thread.ModalTask;
import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.transaction.ui.UncommittedChangesDialog;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class DatabaseTransactionManager extends AbstractProjectComponent implements ProjectManagerListener{
    private DatabaseTransactionManager(Project project) {
        super(project);
        ProjectManager projectManager = ProjectManager.getInstance();
        projectManager.addProjectManagerListener(project, this);
    }

    public static DatabaseTransactionManager getInstance(Project project) {
        return project.getComponent(DatabaseTransactionManager.class);
    }

    public void commit(final ConnectionHandler connectionHandler) {
        Project project = connectionHandler.getProject();
        new ModalTask(project, "Performing commit on connection " + connectionHandler.getName(), false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    indicator.setIndeterminate(true);
                    connectionHandler.commit();
                } catch (SQLException ex) {
                    MessageUtil.showErrorDialog("Could not perform commit operation.", ex);
                }
            }
        }.start();
    }

    public void rollback(final ConnectionHandler connectionHandler) {
        Project project = connectionHandler.getProject();
        new ModalTask(project, "Performing rollback on connection " + connectionHandler.getName(), false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    indicator.setIndeterminate(true);
                    connectionHandler.rollback();
                } catch (SQLException ex) {
                    MessageUtil.showErrorDialog("Could not perform rollback operation.", ex);
                }
            }
        }.start();
    }


    public void showUncommittedChangesDialog(ConnectionHandler connectionHandler) {
        UncommittedChangesDialog executionDialog = new UncommittedChangesDialog(connectionHandler);
        executionDialog.show();
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