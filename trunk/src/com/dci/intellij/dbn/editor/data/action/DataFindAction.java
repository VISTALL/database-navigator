package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.action.DBNDataKeys;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;

public class DataFindAction extends DumbAwareAction {
    public DataFindAction() {
        super("Find...", null, Icons.COMMON_FIND);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        final DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        if (datasetEditor != null) {
            datasetEditor.showSearchHeader();
/*
            FindModel findModel = findManager.getFindInFileModel();

            findManager.showFindDialog(findModel, new Runnable() {
                @Override
                public void run() {
                    datasetEditor.getEditorForm().showSearchPanel();
                }
            });
*/

        }
    }

    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setText("Find data...");
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);

        if (datasetEditor == null) {
            presentation.setEnabled(false);
        } else {
            presentation.setEnabled(true);
            if (datasetEditor.isInserting() || datasetEditor.isLoading()) {
                presentation.setEnabled(false);
            }
        }

    }
}
