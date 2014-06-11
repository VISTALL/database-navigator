package com.dci.intellij.dbn.editor.data.state.ui;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.ui.DBNHeaderForm;
import com.dci.intellij.dbn.editor.data.state.DatasetEditorState;
import com.dci.intellij.dbn.editor.data.state.column.ui.DatasetColumnVisibilityForm;
import com.dci.intellij.dbn.editor.data.state.sorting.ui.DatasetSortingForm;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

public class DatasetEditorStateForm extends DBNFormImpl{
    private JPanel mainPanel;
    private JPanel sortingPanel;
    private JPanel visibilityPanel;
    private JPanel headerPanel;
    private DatasetSortingForm datasetSortingForm;
    private DatasetColumnVisibilityForm columnVisibilityForm;

    public DatasetEditorStateForm(DBDataset dataset, DatasetEditorState datasetEditorState) {
        Project project = dataset.getProject();

        String headerTitle = dataset.getQualifiedName();
        Icon headerIcon = dataset.getIcon();
        Color headerBackground = UIUtil.getPanelBackground();
        if (getEnvironmentSettings(project).getVisibilitySettings().getDialogHeaders().value()) {
            headerBackground = dataset.getEnvironmentType().getColor();
        }
        DBNHeaderForm headerForm = new DBNHeaderForm(
                headerTitle,
                headerIcon,
                headerBackground);
        headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);


        datasetSortingForm = new DatasetSortingForm(dataset, datasetEditorState.getDataSortingState());
        columnVisibilityForm = new DatasetColumnVisibilityForm(dataset, datasetEditorState.getColumnsState());

        sortingPanel.add(datasetSortingForm.getComponent(), BorderLayout.CENTER);
        visibilityPanel.add(columnVisibilityForm.getComponent(), BorderLayout.CENTER);
    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public void applyChanges(){
        datasetSortingForm.applyChanges();
        columnVisibilityForm.applyChanges();
    }

    @Override
    public void dispose() {
        if (!isDisposed()) {
            super.dispose();
            datasetSortingForm.dispose();
            columnVisibilityForm.dispose();
        }
    }
}
