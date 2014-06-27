package com.dci.intellij.dbn.editor.data.ui.table.renderer;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorModel;
import com.dci.intellij.dbn.object.DBColumn;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;

public class DatasetEditorHeaderRenderer implements TableCellRenderer {
    private JPanel mainPanel;
    private JLabel nameLabel;
    private JLabel sortingLabel;

    public DatasetEditorHeaderRenderer() {
        mainPanel.setOpaque(false);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
        DatasetEditorModel model = (DatasetEditorModel) table.getModel();
        sortingLabel.setText(null);
        int width = 0;
        if (columnIndex == table.convertColumnIndexToView(model.getSortColumnIndex())) {
            Icon icon = model.getSortDirection() == SortDirection.ASCENDING ?
                    Icons.ACTION_SORT_ASC :
                    Icons.ACTION_SORT_DESC;
            sortingLabel.setIcon(icon);
            width += icon.getIconWidth();
        } else {
            sortingLabel.setIcon(null);
        }
        String columnName = value.toString();
        nameLabel.setText(columnName);
        DBColumn column = model.getDataset().getColumn(columnName);
        if (column != null) {
            boolean primaryKey = column.isPrimaryKey();
            boolean foreignKey = column.isForeignKey();
            Icon icon = null;
            if (primaryKey && foreignKey) {
                icon = Icons.DBO_LABEL_PK_FK;
            } else if (primaryKey) {
                icon = Icons.DBO_LABEL_PK;
            } else if (foreignKey) {
                icon = Icons.DBO_LABEL_FK;
            }
            nameLabel.setIcon(icon);
            if (icon != null) {
                width += icon.getIconWidth();
            }
        }

        FontMetrics fontMetrics = nameLabel.getFontMetrics(nameLabel.getFont());
        width += fontMetrics.stringWidth(columnName) + 24;
        mainPanel.setPreferredSize(new Dimension(width, (int) mainPanel.getPreferredSize().getHeight()));

        return mainPanel;
    }


}
