package com.dci.intellij.dbn.editor.data;

import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.editor.data.state.DatasetEditorState;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.vfs.DatabaseEditableObjectFile;
import com.dci.intellij.dbn.vfs.DatasetFile;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DatasetEditorProvider implements FileEditorProvider, ApplicationComponent, DumbAware {
    /*********************************************************
     *                  FileEditorProvider                   *
     *********************************************************/

    public boolean accept(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        if (virtualFile instanceof DatabaseEditableObjectFile) {
            DatabaseEditableObjectFile databaseFile = (DatabaseEditableObjectFile) virtualFile;
            return databaseFile.getObject().getContentType() == DBContentType.DATA ||
                   databaseFile.getObject().getContentType() == DBContentType.CODE_AND_DATA;
        }
        return false;
    }

    @NotNull
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        DatabaseEditableObjectFile databaseFile = (DatabaseEditableObjectFile) file;
        DatasetFile datasetFile = (DatasetFile) databaseFile.getContentFile(DBContentType.DATA);
        DBDataset dataset = datasetFile.getObject();
        return new DatasetEditor(databaseFile, dataset);
    }

    public void disposeEditor(@NotNull FileEditor editor) {
        DatasetEditor datasetEditor = (DatasetEditor) editor;
        datasetEditor.dispose();
    }

    @NotNull
    public FileEditorState readState(@NotNull Element sourceElement, @NotNull Project project, @NotNull VirtualFile virtualFile) {
        DatasetEditorState editorState = new DatasetEditorState();
        editorState.readState(sourceElement);
        return editorState;
    }

    public void writeState(@NotNull FileEditorState state, @NotNull Project project, @NotNull Element targetElement) {
        if (state instanceof DatasetEditorState) {
            DatasetEditorState editorState = (DatasetEditorState) state;
            editorState.writeState(targetElement);
        }
    }

    @NotNull
    @NonNls
    public String getEditorTypeId() {
        return "Data";
    }

    @NotNull
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }

    /*********************************************************
     *                ApplicationComponent                   *
     *********************************************************/
    @NonNls
    @NotNull
    public String getComponentName() {
        return "DBNavigator.DatasetEditorProvider";
    }

    public void initComponent() {

    }

    public void disposeComponent() {

    }
}

