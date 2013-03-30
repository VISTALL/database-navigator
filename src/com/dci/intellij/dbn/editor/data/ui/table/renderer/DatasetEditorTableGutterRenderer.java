package com.dci.intellij.dbn.editor.data.ui.table.renderer;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.ui.table.renderer.BasicTableGutterCellRenderer;
import com.dci.intellij.dbn.editor.data.ui.table.model.DatasetEditorModel;
import com.dci.intellij.dbn.editor.data.ui.table.model.DatasetEditorModelRow;
import com.intellij.util.ui.UIUtil;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;

public class DatasetEditorTableGutterRenderer extends JPanel implements ListCellRenderer {
    private static final Border BORDER = new CompoundBorder(
            UIManager.getBorder("TableHeader.cellBorder"),
            new EmptyBorder(0, 2, 0, 6));


    private JLabel textLabel;
    private JLabel imageLabel;

    public static ListCellRenderer INSTANCE = new DatasetEditorTableGutterRenderer();

    private DatasetEditorTableGutterRenderer() {
        setForeground(BasicTableGutterCellRenderer.Colors.FOREGROUND);
        setBackground(UIUtil.getPanelBackground());
        setBorder(BORDER);
        setLayout(new BorderLayout());
        textLabel = new JLabel();
        imageLabel = new JLabel();

        add(textLabel, BorderLayout.WEST);
        add(imageLabel, BorderLayout.EAST);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        DatasetEditorModel model = (DatasetEditorModel) list.getModel();
        DatasetEditorModelRow row = model.getRowAtIndex(index);
        if (row != null) {
            Icon icon =
                    row.isNew() ? Icons.DATA_EDITOR_ROW_NEW :
                            row.isInsert() ? Icons.DATA_EDITOR_ROW_INSERT :
                                    row.isDeleted() ? Icons.DATA_EDITOR_ROW_DELETED :
                                            row.isModified() ? Icons.DATA_EDITOR_ROW_MODIFIED : null;

            textLabel.setText("" + row.getIndex());
            imageLabel.setIcon(icon);
        }
        //lText.setFont(isSelected ? BOLD_FONT : REGULAR_FONT);
        textLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        textLabel.setForeground(isSelected ? BasicTableGutterCellRenderer.Colors.SELECTED_FOREGROUND : BasicTableGutterCellRenderer.Colors.FOREGROUND);
        return this;
    }
}
