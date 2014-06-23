package com.dci.intellij.dbn.editor.data.ui.table.renderer;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorModel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;

public class DatasetEditorTableHeaderRenderer extends JPanel implements TableCellRenderer{
    private static final Border BORDER = UIManager.getBorder("TableHeader.cellBorder");
    public static final TableCellRenderer INSTANCE = new DatasetEditorTableHeaderRenderer();

    private JLabel nameLabel = new JLabel();
    private JLabel sortingLabel = new JLabel();

    private DatasetEditorTableHeaderRenderer() {
        super(new BorderLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(nameLabel, BorderLayout.WEST);
        centerPanel.add(sortingLabel, BorderLayout.CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        DatasetEditorModel model = (DatasetEditorModel) table.getModel();
        JTableHeader header = table.getTableHeader();

        setBackground(header.getBackground());
        setFont(header.getFont());
        setBorder(BORDER);
        if (column == table.convertColumnIndexToView(model.getSortColumnIndex())) {
            sortingLabel.setIcon(model.getSortDirection() == SortDirection.ASCENDING ? Icons.ACTION_SORT_ASC : Icons.ACTION_SORT_DESC);
        } else {
            sortingLabel.setIcon(null);
        }
        nameLabel.setText(value.toString());
        return this;
    }
}
