package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.action.DBNDataKeys;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;

public class InsertRecordAction extends DumbAwareAction {

    public InsertRecordAction() {
        super("Insert record", null, Icons.DATA_EDITOR_INSERT_RECORD);
    }

    public void actionPerformed(AnActionEvent e) {
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        if (datasetEditor != null) {
            datasetEditor.insertRecord();
        }
    }

    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setText("Insert record");
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);

        if (datasetEditor == null) {
            presentation.setEnabled(false);
        } else {
            presentation.setVisible(!datasetEditor.isReadonlyData());
            presentation.setEnabled(
                    !datasetEditor.isReadonly() &&
                    !datasetEditor.isInserting() && 
                    !datasetEditor.isLoading());

        }
    }
}