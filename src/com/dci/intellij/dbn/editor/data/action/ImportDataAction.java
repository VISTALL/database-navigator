package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.Constants;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.action.DBNDataKeys;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.Messages;

public class ImportDataAction extends DumbAwareAction {

    public ImportDataAction() {
        super("Import Data", null, Icons.DATA_IMPORT);
    }

    public void actionPerformed(AnActionEvent e) {
        Messages.showInfoMessage("Data import is not implemented yet.", Constants.DBN_TITLE_PREFIX + "Not implemented");
    }

    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setText("Import Data");
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        if (datasetEditor == null) {
            presentation.setEnabled(false);
        } else {
            presentation.setVisible(!datasetEditor.isReadonlyData());
            boolean enabled =
                    datasetEditor.getEditorTable() != null &&
                    datasetEditor.getActiveConnection().isConnected() &&
                    !datasetEditor.isReadonly() &&
                    !datasetEditor.isInserting();
            presentation.setEnabled(enabled);
        }
    }
}