package com.dci.intellij.dbn.editor.data.state.sorting.ui;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.ui.ValueSelector;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.data.sorting.SortingInstruction;
import com.dci.intellij.dbn.editor.data.state.sorting.DatasetSortingInstruction;
import com.dci.intellij.dbn.editor.data.state.sorting.action.ChangeSortingDirectionAction;
import com.dci.intellij.dbn.editor.data.state.sorting.action.DeleteSortingCriteriaAction;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.util.ui.UIUtil;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatasetSortingColumnForm extends DBNFormImpl {
    private JPanel actionsPanel;
    private JPanel mainPanel;
    private JPanel columnPanel;
    private JLabel indexLabel;
    private JLabel dataTypeLabel;

    private DatasetEditorSortingForm parentForm;
    private SortingInstruction sortingInstruction;

    public DatasetSortingColumnForm(final DatasetEditorSortingForm parentForm, DatasetSortingInstruction sortingInstruction) {
        this.parentForm = parentForm;
        this.sortingInstruction = sortingInstruction;

        DBColumn column = sortingInstruction.getColumn();
        ColumnSelector columnSelector = new ColumnSelector(column);
        columnPanel.add(columnSelector, BorderLayout.CENTER);
        dataTypeLabel.setText(column.getDataType().getName().toLowerCase());
        dataTypeLabel.setForeground(UIUtil.getInactiveTextColor());

        ActionToolbar actionToolbar = ActionUtil.createActionToolbar(
                "DBNavigator.DataEditor.Sorting.Instruction", true,
                new ChangeSortingDirectionAction(this),
                new DeleteSortingCriteriaAction(this));
        actionsPanel.add(actionToolbar.getComponent(), BorderLayout.CENTER);
    }

    private class ColumnSelector extends ValueSelector<DBColumn>{
        public ColumnSelector(DBColumn selectedColumn) {
            super(Icons.DBO_COLUMN_HIDDEN, "Select column...", selectedColumn, true);
        }

        @Override
        public List<DBColumn> loadValues() {
            DBDataset dataset = parentForm.getDataset();
            List<DBColumn> columns = new ArrayList<DBColumn>(dataset.getColumns());
            Collections.sort(columns);
            return columns;
        }

        @Override
        public void valueSelected(DBColumn column) {
            dataTypeLabel.setText(column.getDataType().getName().toLowerCase());
        }
    }

    public void setIndex(int index) {
        indexLabel.setText(Integer.toString(index));
    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public SortingInstruction getSortingInstruction() {
        return sortingInstruction;
    }

    public void remove() {
        parentForm.removeSortingColumn(this);
    }

    public DBDataset getDataset() {
        return parentForm.getDataset();
    }

    @Override
    public void dispose() {
        super.dispose();
        parentForm = null;
    }
}
