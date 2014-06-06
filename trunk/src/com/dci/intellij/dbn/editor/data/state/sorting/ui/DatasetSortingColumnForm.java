package com.dci.intellij.dbn.editor.data.state.sorting.ui;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.data.sorting.SortingInstruction;
import com.dci.intellij.dbn.editor.data.state.sorting.DatasetSortingInstruction;
import com.dci.intellij.dbn.editor.data.state.sorting.action.ChangeSortingDirectionAction;
import com.dci.intellij.dbn.editor.data.state.sorting.action.DeleteSortingCriteriaAction;
import com.dci.intellij.dbn.editor.data.state.sorting.action.SelectColumnAction;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatasetSortingColumnForm extends DBNFormImpl {
    private JComboBox columnComboBox;
    private JPanel actionsPanel;
    private JPanel mainPanel;
    private JLabel columnLabel;

    private DatasetSortingForm parentForm;
    private SortingInstruction sortingInstruction;

    public DatasetSortingColumnForm(final DatasetSortingForm parentForm, DatasetSortingInstruction sortingInstruction) {
        this.parentForm = parentForm;
        this.sortingInstruction = sortingInstruction;
        DBColumn sortColumn = DBObjectRef.get(sortingInstruction.getColumn());

        DBDataset dataset = parentForm.getDataset();
        columnComboBox.setRenderer(cellRenderer);
        final List<DBColumn> columns = new ArrayList<DBColumn>(dataset.getColumns());
        Collections.sort(columns);



        selectColumn(sortColumn);
        columnLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        columnLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                DBDataset dataset = parentForm.getDataset();
                if (dataset != null) {
                    final List<DBColumn> columns = new ArrayList<DBColumn>(dataset.getColumns());
                    Collections.sort(columns);

                    DefaultActionGroup actionGroup = new DefaultActionGroup();
                    for (DBColumn column : columns) {
                        actionGroup.add(new SelectColumnAction(DatasetSortingColumnForm.this, column));
                    }
                    ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup(
                            "DDL File",
                            actionGroup,
                            DataManager.getInstance().getDataContext(columnLabel),
                            JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                            true, null, 10);

                    Point locationOnScreen = columnLabel.getLocationOnScreen();
                    Point location = new Point(
                            (int) (locationOnScreen.getX()),
                            (int) locationOnScreen.getY() + columnLabel.getHeight());
                    popup.showInScreenCoordinates(columnLabel, location);

                }
            }
        });

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

    public void selectColumn(DBColumn column) {
        if (column == null) {
            columnLabel.setIcon(Icons.DBO_COLUMN_HIDDEN);
            columnLabel.setText("Select column");
        } else {
            columnLabel.setIcon(column.getIcon());
            columnLabel.setText(column.getPresentableText());
        }
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
