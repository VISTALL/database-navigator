package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.action.DBNDataKeys;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;

public class ReloadDataAction extends DumbAwareAction {

    public ReloadDataAction() {
        super("Reload", null, Icons.DATA_EDITOR_RELOAD_DATA);
    }

    public void actionPerformed(AnActionEvent e) {
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        if (datasetEditor != null) {
            datasetEditor.load(true, true);
        }
    }

    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setText("Reload");
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);

        boolean enabled =
                datasetEditor != null &&
                datasetEditor.getEditorTable() != null &&
                !datasetEditor.isInserting() &&
                !datasetEditor.isLoading();
        presentation.setEnabled(enabled);

    }
}