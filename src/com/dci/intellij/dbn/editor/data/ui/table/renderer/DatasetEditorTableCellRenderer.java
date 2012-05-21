package com.dci.intellij.dbn.editor.data.ui.table.renderer;

import com.dci.intellij.dbn.common.Colors;
import com.dci.intellij.dbn.common.ui.table.renderer.BasicTableCellRenderer;
import com.dci.intellij.dbn.data.value.LazyLoadedValue;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.dci.intellij.dbn.editor.data.ui.table.model.DatasetEditorModelCell;
import com.intellij.openapi.project.Project;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ui.UIUtil;

import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class DatasetEditorTableCellRenderer extends BasicTableCellRenderer {
    private static final Border CELL_ERROR_BORDER = new LineBorder(Color.RED, 1);
    public static final SimpleTextAttributes FOREIGN_KEY_COLUMN_TEXT_ATTRIBUTES = new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, new Color(0, 0, 128));

    public DatasetEditorTableCellRenderer(Project project) {
        super(project);
    }

    protected void customizeCellRenderer(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.customizeCellRenderer(table, null, isSelected, hasFocus, row, column);

        DatasetEditorModelCell cell = (DatasetEditorModelCell) value;
        DatasetEditorTable datasetEditorTable = (DatasetEditorTable) table;


        if (cell != null && cell.getRow() != null) {
            boolean isLoading = datasetEditorTable.isLoading();
            boolean isInserting = datasetEditorTable.isInserting();

            boolean isDeletedRow = cell.getRow().isDeleted();
            boolean isInsertRow = cell.getRow().isInsert();
            boolean isLob = cell.getUserValue() instanceof LazyLoadedValue;


            //DataModelCell cellAtMouseLocation = datasetEditorTable.getCellAtMouseLocation();

            if (cell.getUserValue() != null) {
                SimpleTextAttributes textAttributes =
                        isDeletedRow || isLob ? SimpleTextAttributes.GRAYED_ATTRIBUTES :
                        cell.getColumnInfo().getColumn().isForeignKey() ? FOREIGN_KEY_COLUMN_TEXT_ATTRIBUTES :
                        cell.isModified() ? SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES :
                                            SimpleTextAttributes.REGULAR_ATTRIBUTES;

                writeUserValue(cell, textAttributes);

                
            } /*else {
                    append("[null]", SimpleTextAttributes.GRAYED_ATTRIBUTES);
                }*/

            //updateBorder(cell, datasetEditorTable);
            

            if (isLoading) {
                if (!isSelected) {
                    setBackground(Colors.DSE_CELL_BACKGROUND_DISABLED);
                }
            } else {
                if (cell.hasError() && !isSelected) {
                    setBorder(CELL_ERROR_BORDER);
                    setBackground(Colors.DSE_CELL_BACKGROUD_ERROR);
                }
                if (isDeletedRow && !isSelected) {
                    setBackground(Colors.DSE_CELL_BACKGROUND_DISABLED);
                }

                if (isInserting && !isInsertRow && !isSelected) {
                    setBackground(Colors.DSE_CELL_BACKGROUND_DISABLED);
                }

                if (isInsertRow && !isSelected) {
                    setBackground(UIUtil.getTableBackground());
                }
            }
        }
    }
}
                                                                