package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.action.DBNDataKeys;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilter;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterManager;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterType;
import com.dci.intellij.dbn.editor.data.options.DataEditorSettings;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;

public class CreateEditDatasetFilterAction extends DumbAwareAction {
    public CreateEditDatasetFilterAction() {
        super("Create / Edit Filter", null, Icons.DATASET_FILTER_NEW);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        if (datasetEditor != null) {
            DBDataset dataset = datasetEditor.getDataset();

            DatasetFilterManager filterManager = DatasetFilterManager.getInstance(dataset.getProject());
            DatasetFilter activeFilter = filterManager.getActiveFilter(dataset);
            if (activeFilter == null || activeFilter.getFilterType() == DatasetFilterType.NONE) {
                DataEditorSettings settings = DataEditorSettings.getInstance(dataset.getProject());
                DatasetFilterType filterType = settings.getFilterSettings().getDefaultFilterType();
                if (filterType == null || filterType == DatasetFilterType.NONE) {
                    filterType = DatasetFilterType.BASIC;
                }


                filterManager.openFiltersDialog(dataset, false, true, filterType);
            }
            else {
                filterManager.openFiltersDialog(dataset, false, false,DatasetFilterType.NONE);
            }
        }
    }

    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();

        DatasetEditor datasetEditor = e.getData(DBNDataKeys.DATASET_EDITOR);
        if (datasetEditor == null) {
            presentation.setEnabled(false);
        } else {
            DBDataset dataset = datasetEditor.getDataset();
            boolean enabled =
                !datasetEditor.isInserting() &&
                !datasetEditor.isLoading();

            presentation.setEnabled(enabled);

            DatasetFilterManager filterManager = DatasetFilterManager.getInstance(dataset.getProject());
            DatasetFilter activeFilter = filterManager.getActiveFilter(dataset);
            if (activeFilter == null || activeFilter.getFilterType() == DatasetFilterType.NONE) {
                presentation.setText("Create filter");
                presentation.setIcon(Icons.DATASET_FILTER_NEW);
            } else {
                presentation.setText("Edit filter");
                presentation.setIcon(Icons.DATASET_FILTER_EDIT);
            }
        }
    }
}
