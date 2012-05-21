package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.action.DBNDataKeys;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;

public class EditRecordAction extends DumbAwareAction {

    public EditRecordAction() {
        super("Edit Record", null, Icons.DATA_EDITOR_EDIT_RECORD);
    }

    public void actionPerformed(AnActionEvent e) {
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        if (datasetEditor != null) {
            datasetEditor.openRecordEditor();
        }
    }

    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setText("Edit Record");
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);

        boolean enabled =
                datasetEditor != null &&
                datasetEditor.getEditorTable() != null &&
                datasetEditor.getEditorTable().getSelectedRow() != -1 &&
                !datasetEditor.isInserting() &&
                !datasetEditor.isLoading();
        presentation.setEnabled(enabled);

    }
}