package com.dci.intellij.dbn.editor.data.state.sorting.ui;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.data.sorting.SortingInstruction;
import com.dci.intellij.dbn.editor.data.state.sorting.DatasetSortingInstruction;
import com.dci.intellij.dbn.editor.data.state.sorting.action.ChangeSortingDirectionAction;
import com.dci.intellij.dbn.editor.data.state.sorting.action.DeleteSortingCriteriaAction;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatasetSortingColumnForm extends DBNFormImpl {
    private JComboBox columnComboBox;
    private JPanel actionsPanel;
    private JPanel mainPanel;

    private DatasetSortingForm parentForm;
    private SortingInstruction sortingInstruction;

    public DatasetSortingColumnForm(DatasetSortingForm parentForm, DatasetSortingInstruction sortingInstruction) {
        this.parentForm = parentForm;
        this.sortingInstruction = sortingInstruction;
        DBColumn sortColumn = DBObjectRef.get(sortingInstruction.getColumn());

        DBDataset dataset = parentForm.getDataset();
        columnComboBox.setRenderer(cellRenderer);
        List<DBColumn> columns = new ArrayList<DBColumn>(dataset.getColumns());
        Collections.sort(columns);

        for (DBColumn column : columns) {
            columnComboBox.addItem(column);
        }

        ActionToolbar actionToolbar = ActionUtil.createActionToolbar(
                "DBNavigator.DataEditor.Sorting.Instruction", true,
                new ChangeSortingDirectionAction(this),
                new DeleteSortingCriteriaAction(this));
        actionsPanel.add(actionToolbar.getComponent(), BorderLayout.CENTER);

        columnComboBox.setSelectedItem(sortColumn);

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

    private ListCellRenderer cellRenderer = new ColoredListCellRenderer() {
        protected void customizeCellRenderer(JList list, Object value, int index, boolean selected, boolean hasFocus) {
            DBColumn column = (DBColumn) value;
            if (column != null) {
                setIcon(column.getIcon());
                append(column.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            }
        }
    };

    public DBDataset getDataset() {
        return parentForm.getDataset();
    }

    @Override
    public void dispose() {
        super.dispose();
        parentForm = null;
    }
}
