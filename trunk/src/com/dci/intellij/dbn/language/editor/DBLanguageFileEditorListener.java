package com.dci.intellij.dbn.language.editor;

import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.language.common.DBLanguageFileType;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.BorderLayout;

public class DBLanguageFileEditorListener implements FileEditorManagerListener{
    public void fileOpened(FileEditorManager source, VirtualFile file) {
        if (file.isInLocalFileSystem() && file.getFileType() instanceof DBLanguageFileType) {
            ActionToolbar actionToolbar = ActionUtil.createActionToolbar("", true, "DBNavigator.ActionGroup.FileEditor");
            //FileEditorManager.getInstance(editor.getProject()).addTopComponent(fileEditor, actionToolbar.getComponent());
            FileEditor editor = source.getSelectedEditor(file);
            editor.getComponent().add(actionToolbar.getComponent(), BorderLayout.NORTH);
        }
    }

    public void fileClosed(FileEditorManager source, VirtualFile file) {
    }

    public void selectionChanged(FileEditorManagerEvent event) {
    }
}
