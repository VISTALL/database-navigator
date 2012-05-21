package com.dci.intellij.dbn.language.editor.action;

import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.NamingUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.ModuleConnectionManager;
import com.dci.intellij.dbn.connection.mapping.FileConnectionMappingManager;
import com.dci.intellij.dbn.language.common.DBLanguageFileType;
import com.dci.intellij.dbn.vfs.DatabaseEditableObjectFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class SelectConnectionAction extends DumbAwareAction {
    private final ConnectionHandler connectionHandler;

    public SelectConnectionAction(ConnectionHandler connectionHandler) {
        super();
        this.connectionHandler = connectionHandler;
    }

    public void actionPerformed(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        FileConnectionMappingManager.getInstance(project).selectActiveConnectionForEditor(connectionHandler);
    }

    public void update(AnActionEvent e) {
        boolean enabled = true;
        Project project = ActionUtil.getProject(e);
        VirtualFile[] selectedFiles = FileEditorManager.getInstance(project).getSelectedFiles();
        if (selectedFiles.length == 1) {
            VirtualFile virtualFile = selectedFiles[0];
            if (virtualFile instanceof DatabaseEditableObjectFile) {
                enabled = false;
            } else {
                if (virtualFile.getFileType() instanceof DBLanguageFileType) {
                    if (connectionHandler == null) {
                        enabled = true;
                    } else {
                        ConnectionManager connectionManager = connectionHandler.getConnectionManager();
                        if (connectionManager instanceof ModuleConnectionManager) {
                            Module currentModule = ModuleUtil.findModuleForFile(virtualFile, project);
                            Module connectionModule = ((ModuleConnectionManager) connectionManager).getModule();
                            enabled = connectionModule == currentModule;
                        }
                    }
                } else {
                    enabled = false;
                }
            }
        }
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(enabled);
        if (connectionHandler == null) {
            presentation.setText("No Connection");
            presentation.setIcon(null);
            presentation.setDescription("");
        } else {
            presentation.setText(NamingUtil.enhanceUnderscoresForDisplay(connectionHandler.getQualifiedName()));
            presentation.setIcon(connectionHandler.getIcon());
            presentation.setDescription(connectionHandler.getDescription());
        }
    }
}
