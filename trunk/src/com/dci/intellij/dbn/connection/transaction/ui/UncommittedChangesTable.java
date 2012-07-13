package com.dci.intellij.dbn.connection.transaction.ui;

import com.dci.intellij.dbn.common.ui.table.DBNTable;
import com.dci.intellij.dbn.connection.transaction.UncommittedChange;
import com.intellij.ui.ColoredTableCellRenderer;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class UncommittedChangesTable extends DBNTable {
    private static final Border EMPTY_BORDER = new EmptyBorder(0, 2, 0, 2);

    public UncommittedChangesTable(UncommittedChangesTableModel model) {
        super(model.getProject(), model);
        setDefaultRenderer(UncommittedChange.class, new CellRenderer());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellSelectionEnabled(true);
        setRowHeight(getRowHeight() + 2);
        accommodateColumnsSize();
    }


    public class CellRenderer extends ColoredTableCellRenderer {
        @Override
        protected void customizeCellRenderer(JTable table, Object value, boolean selected, boolean hasFocus, int row, int column) {
            UncommittedChange change = (UncommittedChange) value;
            if (column == 0) {
                setIcon(change.getIcon());
                append(change.getDisplayFilePath());
            } else if (column == 1) {
                append(change.getChangesCount() + " uncommitted changes");
            }

        }
    }
}
