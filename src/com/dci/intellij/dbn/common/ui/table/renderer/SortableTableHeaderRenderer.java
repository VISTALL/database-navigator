package com.dci.intellij.dbn.common.ui.table.renderer;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.sorting.SortDirection;
import com.dci.intellij.dbn.common.ui.table.model.SortableDataModel;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class SortableTableHeaderRenderer extends DefaultTableCellRenderer {
    private static final Border BORDER = UIManager.getBorder("TableHeader.cellBorder");
    public static final TableCellRenderer INSTANCE = new SortableTableHeaderRenderer();

    private SortableTableHeaderRenderer() {}

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        SortableDataModel model = (SortableDataModel) table.getModel();
        JTableHeader header = table.getTableHeader();

        setBackground(header.getBackground());
        setFont(header.getFont());
        setBorder(BORDER);
        setIcon(null);
        setHorizontalTextPosition(JLabel.LEADING);
        setHorizontalAlignment(JLabel.CENTER);
        if (column == table.convertColumnIndexToView(model.getSortColumnIndex())) {
            renderer.setIcon(model.getSortDirection() == SortDirection.ASCENDING ? Icons.ACTION_SORT_ASC : Icons.ACTION_SORT_DESC);
        }
        return renderer;
    }
}
