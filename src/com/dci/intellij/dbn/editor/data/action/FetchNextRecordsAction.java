package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.action.DBNDataKeys;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.options.DataEditorSettings;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;

public class FetchNextRecordsAction extends DumbAwareAction {

    public FetchNextRecordsAction() {
        super("Fetch next records", null, Icons.DATA_EDITOR_FETCH_NEXT_RECORDS);
    }

    public void actionPerformed(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        if (datasetEditor != null) {
            DataEditorSettings settings = DataEditorSettings.getInstance(project);
            datasetEditor.fetchNextRecords(settings.getGeneralSettings().getFetchBlockSize().value());
        }
    }

    public void update(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        Presentation presentation = e.getPresentation();
        presentation.setText("Fetch next records");
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);

        if (project != null) {
            DataEditorSettings settings = DataEditorSettings.getInstance(project);
            presentation.setText("Fetch next " + settings.getGeneralSettings().getFetchBlockSize() + " records");
        }
        boolean enabled =
                datasetEditor != null &&
                datasetEditor.getEditorTable() != null &&
                !datasetEditor.isInserting() &&
                !datasetEditor.isLoading() &&        
                !datasetEditor.getEditorTable().getModel().isResultSetExhausted();
        presentation.setEnabled(enabled);

    }
}