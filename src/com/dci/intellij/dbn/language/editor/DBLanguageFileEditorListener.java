package com.dci.intellij.dbn.language.editor;

import com.dci.intellij.dbn.language.common.DBLanguageFileType;
import com.dci.intellij.dbn.language.editor.ui.DBLanguageFileEditorToolbarForm;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.BorderLayout;

public class DBLanguageFileEditorListener implements FileEditorManagerListener{
    public void fileOpened(FileEditorManager source, VirtualFile file) {
        if (file.isInLocalFileSystem() && file.getFileType() instanceof DBLanguageFileType) {
            FileEditor editor = source.getSelectedEditor(file);
            if (editor != null) {
                DBLanguageFileEditorToolbarForm toolbarForm = new DBLanguageFileEditorToolbarForm(source.getProject(), file);
                editor.getComponent().add(toolbarForm.getComponent(), BorderLayout.NORTH);
                editor.putUserData(DBLanguageFileEditorToolbarForm.USER_DATA_KEY, toolbarForm);
            }
        }
    }

    public void fileClosed(FileEditorManager source, VirtualFile file) {
        if (file.isInLocalFileSystem() && file.getFileType() instanceof DBLanguageFileType) {
            FileEditor editor = source.getSelectedEditor(file);
            if (editor != null) {
                DBLanguageFileEditorToolbarForm toolbarForm = editor.getUserData(DBLanguageFileEditorToolbarForm.USER_DATA_KEY);
                if (toolbarForm != null) {
                    toolbarForm.dispose();
                }
            }
        }

    }

    public void selectionChanged(FileEditorManagerEvent event) {
    }
}
