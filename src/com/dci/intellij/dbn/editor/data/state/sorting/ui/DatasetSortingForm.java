package com.dci.intellij.dbn.editor.data.state.sorting.ui;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.data.sorting.SortingInstruction;
import com.dci.intellij.dbn.editor.data.state.sorting.DatasetSortingInstruction;
import com.dci.intellij.dbn.editor.data.state.sorting.DatasetSortingState;
import com.dci.intellij.dbn.editor.data.state.sorting.action.AddSortingColumnAction;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.actionSystem.ActionToolbar;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class DatasetSortingForm extends DBNFormImpl{
    private JPanel mainPanel;
    private JPanel sortingInstructionsPanel;
    private JPanel actionsPanel;

    private DBObjectRef<DBDataset> datasetRef;
    private List<DatasetSortingColumnForm> sortingInstructionForms = new ArrayList<DatasetSortingColumnForm>();

    public DatasetSortingForm(DBDataset dataset, DatasetSortingState sortingState) {
        this.datasetRef = dataset.getRef();

        BoxLayout sortingInstructionsPanelLayout = new BoxLayout(sortingInstructionsPanel, BoxLayout.Y_AXIS);
        sortingInstructionsPanel.setLayout(sortingInstructionsPanelLayout);

        for (SortingInstruction sortingInstruction : sortingState.getSortingInstructions()) {
            DatasetSortingInstruction datasetSortingInstruction = (DatasetSortingInstruction) sortingInstruction;
            DatasetSortingColumnForm sortingInstructionForm = new DatasetSortingColumnForm(this, datasetSortingInstruction);
            sortingInstructionForms.add(sortingInstructionForm);
            sortingInstructionsPanel.add(sortingInstructionForm.getComponent());
        }

        ActionToolbar actionToolbar = ActionUtil.createActionToolbar(
                "DBNavigator.DataEditor.Sorting.Add", true,
                new AddSortingColumnAction(this));
        actionsPanel.add(actionToolbar.getComponent(), BorderLayout.WEST);
    }



    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public DBDataset getDataset() {
        return datasetRef.get();
    }

    public void addSortingColumn() {
        DBDataset dataset = datasetRef.get();
        if (dataset != null) {
            List<DBColumn> columns = dataset.getColumns();
            DBColumn firstColumn = columns.size() == 0 ? null : columns.get(0);
            DatasetSortingInstruction datasetSortingInstruction = new DatasetSortingInstruction(firstColumn, SortDirection.ASCENDING);
            DatasetSortingColumnForm sortingInstructionForm = new DatasetSortingColumnForm(this, datasetSortingInstruction);
            sortingInstructionForms.add(sortingInstructionForm);
            sortingInstructionsPanel.add(sortingInstructionForm.getComponent());
            sortingInstructionsPanel.updateUI();
        }
    }


    public void removeSortingColumn(DatasetSortingColumnForm sortingInstructionForm) {
        sortingInstructionsPanel.remove(sortingInstructionForm.getComponent());
        sortingInstructionsPanel.updateUI();
        sortingInstructionForms.remove(sortingInstructionForm);
        sortingInstructionForm.dispose();
    }

    public void applyChanges() {

    }

    @Override
    public void dispose() {
        super.dispose();
        for (DatasetSortingColumnForm sortingColumnForm : sortingInstructionForms) {
            sortingColumnForm.dispose();
        }
        sortingInstructionForms.clear();
    }

}
