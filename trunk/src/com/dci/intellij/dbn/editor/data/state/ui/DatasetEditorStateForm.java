package com.dci.intellij.dbn.editor.data.state.ui;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.ui.DBNHeaderForm;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.state.column.ui.DatasetColumnSetupForm;
import com.dci.intellij.dbn.editor.data.state.sorting.ui.DatasetEditorSortingForm;
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
    private DatasetEditorSortingForm datasetEditorSortingForm;
    private DatasetColumnSetupForm columnVisibilityForm;

    public DatasetEditorStateForm(DatasetEditor datasetEditor) {
        DBDataset dataset = datasetEditor.getDataset();
        Project project = datasetEditor.getProject();

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


        datasetEditorSortingForm = new DatasetEditorSortingForm(datasetEditor);
        columnVisibilityForm = new DatasetColumnSetupForm(datasetEditor);

        sortingPanel.add(datasetEditorSortingForm.getComponent(), BorderLayout.CENTER);
        visibilityPanel.add(columnVisibilityForm.getComponent(), BorderLayout.CENTER);
    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public boolean applyChanges(){
        boolean changed = false;
        datasetEditorSortingForm.applyChanges();
        changed = columnVisibilityForm.applyChanges();
        return changed;
    }

    @Override
    public void dispose() {
        if (!isDisposed()) {
            super.dispose();
            datasetEditorSortingForm.dispose();
            columnVisibilityForm.dispose();
        }
    }
}
