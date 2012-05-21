package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.action.DBNDataKeys;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;

public class DuplicateRecordAction extends DumbAwareAction {

    public DuplicateRecordAction() {
        super("Duplicate record", null, Icons.DATA_EDITOR_DUPLICATE_RECORD);
    }

    public void actionPerformed(AnActionEvent e) {
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        if (datasetEditor != null) {
            datasetEditor.duplicateRecord();
        }
    }

    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setText("Duplicate record");
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);

        if (datasetEditor == null) {
            presentation.setEnabled(false);
        } else {
            presentation.setEnabled(true);
            presentation.setVisible(!datasetEditor.isReadonlyData());
            if (datasetEditor.isInserting() || datasetEditor.isLoading() || datasetEditor.isReadonly()) {
                presentation.setEnabled(false);
            } else {
                DatasetEditorTable editorTable = datasetEditor.getEditorTable();
                int[] selectedrows =
                        editorTable == null ? null :
                        editorTable.getSelectedRows();
                presentation.setEnabled(selectedrows != null && selectedrows.length == 1 && selectedrows[0] < editorTable.getModel().getSize());
            }
        }
    }
}