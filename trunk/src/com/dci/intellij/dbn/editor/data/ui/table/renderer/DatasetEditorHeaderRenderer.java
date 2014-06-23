package com.dci.intellij.dbn.editor.data.ui.table.renderer;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorModel;
import com.dci.intellij.dbn.object.DBColumn;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class DatasetEditorHeaderRenderer implements TableCellRenderer {
    private JPanel mainPanel;
    private JLabel nameLabel;
    private JLabel sortingLabel;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
        DatasetEditorModel model = (DatasetEditorModel) table.getModel();
        sortingLabel.setText(null);
        if (columnIndex == table.convertColumnIndexToView(model.getSortColumnIndex())) {
            sortingLabel.setIcon(
                    model.getSortDirection() == SortDirection.ASCENDING ?
                            Icons.ACTION_SORT_ASC :
                            Icons.ACTION_SORT_DESC);
        } else {
            sortingLabel.setIcon(null);
        }
        String columnName = value.toString();
        nameLabel.setText(columnName);
        DBColumn column = model.getDataset().getColumn(columnName);
        if (column != null) {
            boolean primaryKey = column.isPrimaryKey();
            boolean foreignKey = column.isForeignKey();
            if (primaryKey && foreignKey) nameLabel.setIcon(Icons.DBO_LABEL_PK_FK);
             else if (primaryKey) nameLabel.setIcon(Icons.DBO_LABEL_PK);
             else if (foreignKey) nameLabel.setIcon(Icons.DBO_LABEL_FK);
             else nameLabel.setIcon(null);
        }

        return mainPanel;
    }
}
