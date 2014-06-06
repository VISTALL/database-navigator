package com.dci.intellij.dbn.editor.data.state.ui;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.editor.data.state.DatasetEditorState;
import com.dci.intellij.dbn.editor.data.state.sorting.ui.DatasetSortingForm;
import com.dci.intellij.dbn.editor.data.state.visibility.ui.DatasetColumnVisibilityForm;
import com.dci.intellij.dbn.object.DBDataset;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class DatasetEditorStateForm extends DBNFormImpl{
    private JPanel mainPanel;
    private JPanel sortingPanel;
    private JPanel visibilityPanel;

    public DatasetEditorStateForm(DBDataset dataset, DatasetEditorState datasetEditorState) {
        DatasetSortingForm datasetSortingForm = new DatasetSortingForm(dataset, datasetEditorState.getDataSortingState());
        DatasetColumnVisibilityForm columnVisibilityForm = new DatasetColumnVisibilityForm(dataset, datasetEditorState.getColumnVisibilityState());
        sortingPanel.add(datasetSortingForm.getComponent(), BorderLayout.CENTER);
        visibilityPanel.add(columnVisibilityForm.getComponent(), BorderLayout.CENTER);
    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }
}
