package com.dci.intellij.dbn.editor.data.state.sorting.ui;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.data.sorting.SortingInstruction;
import com.dci.intellij.dbn.editor.data.state.sorting.DatasetSortingInstruction;
import com.dci.intellij.dbn.editor.data.state.sorting.DatasetSortingState;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

public class DatasetSortingForm extends DBNFormImpl{
    private JPanel mainPanel;
    private JPanel sortingInstructionsPanel;

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
    }



    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public DBDataset getDataset() {
        return datasetRef.get();
    }

    public void removeChildPanel(DatasetSortingColumnForm sortingInstructionForm) {
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
