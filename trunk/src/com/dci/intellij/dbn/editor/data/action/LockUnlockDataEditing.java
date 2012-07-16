package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.action.DBNDataKeys;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public class LockUnlockDataEditing extends ToggleAction implements DumbAware {

    public LockUnlockDataEditing() {
        super("Lock / Unlock Editing", null, Icons.DATA_EDITOR_LOCK_EDITING);
    }

    public boolean isSelected(AnActionEvent e) {
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        return datasetEditor != null && datasetEditor.isReadonly();
    }

    public void setSelected(AnActionEvent e, boolean selected) {
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        if (datasetEditor != null) datasetEditor.setReadonly(selected);
    }

    public void update(AnActionEvent e) {
        super.update(e);
        Presentation presentation = e.getPresentation();
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        if (datasetEditor == null) {
            presentation.setEnabled(false);
        } else {
            presentation.setVisible(!datasetEditor.isReadonlyData());
            presentation.setEnabled(datasetEditor.getActiveConnection().isConnected());
            presentation.setText(isSelected(e) ? "Unlock Editing" : "Lock Editing");
            boolean enabled =
                        datasetEditor.getEditorTable() != null &&
                        !datasetEditor.isInserting();
            presentation.setEnabled(enabled);
        }
    }

    @Nullable
    protected DatasetEditor getDatasetEditor(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        if (project != null) {
            FileEditor[] fileEditors = FileEditorManager.getInstance(project).getSelectedEditors();
            for (FileEditor fileEditor : fileEditors) {
                if (fileEditor instanceof DatasetEditor) {
                    return (DatasetEditor) fileEditor;
                }
            }
        }
        return null;
    }
}