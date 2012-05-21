package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public abstract class AbstractDataEditorAction extends DumbAwareAction {
    private DatasetEditor datasetEditor;
    public AbstractDataEditorAction(DatasetEditor datasetEditor, String text) {
        super(text);
        this.datasetEditor = datasetEditor;
    }

    public AbstractDataEditorAction(DatasetEditor datasetEditor, String text, Icon icon) {
        super(text, null, icon);
        this.datasetEditor = datasetEditor;
    }
    
    @Nullable
    protected DatasetEditor getDatasetEditor(AnActionEvent e) {
        return datasetEditor;
    }
}
