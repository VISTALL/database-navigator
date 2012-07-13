package com.dci.intellij.dbn.connection.transaction.action;

import com.dci.intellij.dbn.connection.transaction.UncommittedChange;
import com.intellij.ui.ColoredTableCellRenderer;

import javax.swing.JTable;

public class UncommittedChangesTableCellRenderer extends ColoredTableCellRenderer{
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
