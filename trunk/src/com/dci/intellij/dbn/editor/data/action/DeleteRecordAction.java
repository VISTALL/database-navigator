package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.action.DBNDataKeys;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.dci.intellij.dbn.editor.data.ui.table.model.DatasetEditorModelRow;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;

public class DeleteRecordAction extends DumbAwareAction {

    public DeleteRecordAction() {
        super("Delete records", null, Icons.DATA_EDITOR_DELETE_RECORD);
    }

    public void actionPerformed(AnActionEvent e) {
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        if (datasetEditor != null) {
            datasetEditor.deleteRecords();
        }
    }

    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setText("Delete records");
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        if (datasetEditor == null) {
            presentation.setEnabled(false);
        } else {
            presentation.setVisible(!datasetEditor.isReadonlyData());
            presentation.setEnabled(true);
            if (datasetEditor.isInserting() || datasetEditor.isLoading() || datasetEditor.isReadonly()) {
                presentation.setEnabled(false);
            } else {
                DatasetEditorTable editorTable = datasetEditor.getEditorTable();
                if (editorTable != null && editorTable.getSelectedRows() != null && editorTable.getSelectedRows().length > 0) {
                    for (int selectedRow : editorTable.getSelectedRows()) {
                        if (selectedRow < editorTable.getModel().getRowCount()) {
                            DatasetEditorModelRow row = editorTable.getModel().getRowAtIndex(selectedRow);
                            if (!row.isDeleted()) {
                                presentation.setEnabled(true);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}