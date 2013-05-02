package com.dci.intellij.dbn.editor.data.sorting.ui;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.ui.DBNHeaderForm;
import com.dci.intellij.dbn.data.sorting.SortingInstruction;
import com.dci.intellij.dbn.editor.data.sorting.DatasetSortingState;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class DatasetSortingForm extends DBNFormImpl{
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel sortingInstructionsPanel;

    private DBDataset dataset;
    private List<DatasetSortingColumnForm> sortingInstructionForms = new ArrayList<DatasetSortingColumnForm>();

    public DatasetSortingForm(DBDataset dataset, DatasetSortingState sortingState) {
        this.dataset = dataset;
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

        BoxLayout sortingInstructionsPanelLayout = new BoxLayout(sortingInstructionsPanel, BoxLayout.Y_AXIS);
        sortingInstructionsPanel.setLayout(sortingInstructionsPanelLayout);

        for (SortingInstruction<DBColumn> sortingInstruction : sortingState.getSortingInstructions()) {
            DatasetSortingColumnForm sortingInstructionForm = new DatasetSortingColumnForm(this, sortingInstruction);
            sortingInstructionForms.add(sortingInstructionForm);
            sortingInstructionsPanel.add(sortingInstructionForm.getComponent());
        }
    }



    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public DBDataset getDataset() {
        return dataset;
    }
}
