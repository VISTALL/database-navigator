package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.EditorUtil;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;

public class LockUnlockDataEditing extends ToggleAction implements DumbAware {

    public LockUnlockDataEditing() {
        super("Lock / Unlock Editing", null, Icons.DATA_EDITOR_LOCK_EDITING);
    }

    public boolean isSelected(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        DatasetEditor datasetEditor = (DatasetEditor) EditorUtil.getFileEditor(project, e.getPlace());
        return datasetEditor != null && datasetEditor.isReadonly();
    }

    public void setSelected(AnActionEvent e, boolean selected) {
        Project project = ActionUtil.getProject(e);
        DatasetEditor datasetEditor = (DatasetEditor) EditorUtil.getFileEditor(project, e.getPlace());
        if (datasetEditor != null) datasetEditor.setReadonly(selected);
    }

    public void update(AnActionEvent e) {
        super.update(e);
        Project project = ActionUtil.getProject(e);
        DatasetEditor datasetEditor = (DatasetEditor) EditorUtil.getFileEditor(project, e.getPlace());

        Presentation presentation = e.getPresentation();
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
}