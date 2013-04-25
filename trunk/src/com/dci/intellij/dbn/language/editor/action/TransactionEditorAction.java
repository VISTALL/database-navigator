package com.dci.intellij.dbn.language.editor.action;

import com.dci.intellij.dbn.common.thread.SimpleLaterInvocator;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.mapping.FileConnectionMappingManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;

import javax.swing.Icon;

public abstract class TransactionEditorAction extends DumbAwareAction {
    protected TransactionEditorAction(String text, String description, Icon icon) {
        super(text, description, icon);
    }

    public void update(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        boolean enabled = false;
        if (project != null) {
            FileConnectionMappingManager connectionMappingManager = FileConnectionMappingManager.getInstance(project);
            ConnectionHandler activeConnection = connectionMappingManager.lookupActiveConnectionForEditor(e.getPlace());
            enabled = activeConnection != null && activeConnection.hasUncommittedChanges();
        }
        e.getPresentation().setEnabled(enabled);
    }

    protected void showErrorDialog(final String message) {
        new SimpleLaterInvocator() {
            public void run() {
                MessageUtil.showErrorDialog(message);
            }
        }.start();
    }

    protected ConnectionHandler getConnectionHandler(Project project, String actionPlace) {
        if (project != null) {
            FileConnectionMappingManager connectionMappingManager = FileConnectionMappingManager.getInstance(project);
            return connectionMappingManager.lookupActiveConnectionForEditor(actionPlace);
        }
        return null;
    }
}
