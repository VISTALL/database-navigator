package com.dci.intellij.dbn.editor.data.ui.table.renderer;

import com.dci.intellij.dbn.common.Colors;
import com.dci.intellij.dbn.common.ui.table.BasicTable;
import com.dci.intellij.dbn.common.ui.table.renderer.BasicTableCellRenderer;
import com.dci.intellij.dbn.data.editor.color.DataGridTextAttributes;
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

    public DatasetEditorTableCellRenderer(Project project) {
        super(project);
    }

    protected void customizeCellRenderer(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.customizeCellRenderer(table, null, isSelected, hasFocus, row, column);

        DataGridTextAttributes configTextAttributes = ((BasicTable) table).getConfigTextAttributes();

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
                        isSelected ? configTextAttributes.getSelection() :
                        isDeletedRow || isLob ? configTextAttributes.getDeletedData() :
                        cell.getColumnInfo().getColumn().isForeignKey() ? configTextAttributes.getForeignReference() :
                        cell.isModified() ? configTextAttributes.getModifiedData() :
                                            configTextAttributes.getPlainData();

                writeUserValue(cell, textAttributes, configTextAttributes);

                
            } /*else {
                    append("[null]", SimpleTextAttributes.GRAYED_ATTRIBUTES);
                }*/

            //updateBorder(cell, datasetEditorTable);
            


            if (!isSelected) {
                if (isLoading) {
                    setBackground(Colors.DSE_CELL_BACKGROUND_DISABLED);
                } else {
                    if (cell.hasError()) {
                        setBorder(CELL_ERROR_BORDER);
                        setBackground(configTextAttributes.getErrorData().getBgColor());
                    }
                    else if (isDeletedRow) {
                        setBackground(configTextAttributes.getDeletedData().getBgColor());
                    }

                    else if (isInserting && !isInsertRow) {
                        setBackground(Colors.DSE_CELL_BACKGROUND_DISABLED);
                    }

                    else if (isInsertRow) {
                        setBackground(UIUtil.getTableBackground());
                    }
                }
            }
        }
    }
}
                                                                