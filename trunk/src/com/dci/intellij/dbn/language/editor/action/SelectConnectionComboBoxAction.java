package com.dci.intellij.dbn.language.editor.action;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.common.ui.DBNComboBoxAction;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.NamingUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.mapping.FileConnectionMappingManager;
import com.dci.intellij.dbn.vfs.SQLConsoleFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import javax.swing.JComponent;

public class SelectConnectionComboBoxAction extends DBNComboBoxAction {
    private static final String NAME = "DB Connections";

    @NotNull
    protected DefaultActionGroup createPopupActionGroup(JComponent component) {
        Project project = ActionUtil.getProject(component);
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
        return new SelectConnectionActionGroup(browserManager);
    }

    public synchronized void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        String text = NAME;
        Icon icon = null;

        Project project = ActionUtil.getProject(e);
        if (project != null) {
            ConnectionHandler activeConnection = 
                    FileConnectionMappingManager.getInstance(project).lookupActiveConnectionForEditor();
            if (activeConnection != null) {
                text = NamingUtil.enhanceUnderscoresForDisplay(activeConnection.getQualifiedName());
                icon = activeConnection.getIcon();
            }

            VirtualFile[] selectedFiles = FileEditorManager.getInstance(project).getSelectedFiles();
            boolean isConsole = selectedFiles.length > 0 && selectedFiles[0] instanceof SQLConsoleFile;
            presentation.setVisible(!isConsole);
        }

        presentation.setText(text);
        presentation.setIcon(icon);
    }
 }
