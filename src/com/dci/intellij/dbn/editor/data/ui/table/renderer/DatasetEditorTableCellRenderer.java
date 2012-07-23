package com.dci.intellij.dbn.editor.data.ui.table.renderer;

import com.dci.intellij.dbn.common.ui.table.BasicTable;
import com.dci.intellij.dbn.common.ui.table.renderer.BasicTableCellRenderer;
import com.dci.intellij.dbn.data.editor.color.DataGridTextAttributes;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.dci.intellij.dbn.editor.data.ui.table.model.DatasetEditorModelCell;
import com.dci.intellij.dbn.object.DBColumn;
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

    protected void customizeCellRenderer(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
        DataGridTextAttributes configTextAttributes = ((BasicTable) table).getConfigTextAttributes();

        DatasetEditorModelCell cell = (DatasetEditorModelCell) value;
        DatasetEditorTable datasetEditorTable = (DatasetEditorTable) table;


        if (cell != null && cell.getRow() != null) {
            boolean isLoading = datasetEditorTable.isLoading();
            boolean isInserting = datasetEditorTable.isInserting();

            boolean isDeletedRow = cell.getRow().isDeleted();
            boolean isInsertRow = cell.getRow().isInsert();


            //DataModelCell cellAtMouseLocation = datasetEditorTable.getCellAtMouseLocation();

            DBColumn column = cell.getColumnInfo().getColumn();
            if (cell.getUserValue() != null) {
                SimpleTextAttributes textAttributes =
                        isSelected ? configTextAttributes.getSelection() :
                        isLoading ? configTextAttributes.getLoadingData() :
                        isDeletedRow ? configTextAttributes.getDeletedData() :
                        column.isPrimaryKey() ? configTextAttributes.getPrimaryKey() :
                        column.isForeignKey() ? configTextAttributes.getForeignKey() :
                        cell.isModified() ? configTextAttributes.getModifiedData() :
                        cell.isLobValue() ? configTextAttributes.getReadonlyData() :
                                            configTextAttributes.getPlainData();

                writeUserValue(cell, textAttributes, configTextAttributes);

                
            } /*else {
                    append("[null]", SimpleTextAttributes.GRAYED_ATTRIBUTES);
                }*/

            //updateBorder(cell, datasetEditorTable);
            


            if (!isSelected) {
                if (isLoading || !datasetEditorTable.getDataset().getConnectionHandler().isConnected()) {
                    setBackground(configTextAttributes.getLoadingData().getBgColor());
                } else {
                    if (cell.hasError()) {
                        setBorder(CELL_ERROR_BORDER);
                        setBackground(configTextAttributes.getErrorData().getBgColor());
                    }

                    if (isDeletedRow) {
                        setBackground(configTextAttributes.getDeletedData().getBgColor());
                    }

                    if (isInserting && !isInsertRow) {
                        setBackground(configTextAttributes.getReadonlyData().getBgColor());
                    }

                    if (isInsertRow) {
                        setBackground(UIUtil.getTableBackground());
                    }
                    if (table.getSelectedRow() == rowIndex && !match(table.getSelectedColumns(), columnIndex) && table.getCellSelectionEnabled()) {
                        setBackground(configTextAttributes.getCaretRow().getBgColor());
                    }

                    if (column.isPrimaryKey()) {
                        Color primaryKeyBackground = configTextAttributes.getPrimaryKey().getBgColor();
                        if (primaryKeyBackground != null) {
                            setBackground(primaryKeyBackground);
                        }
                    } else if (column.isForeignKey()) {
                        Color foreignKeyBackground = configTextAttributes.getForeignKey().getBgColor();
                        if (foreignKeyBackground != null) {
                            setBackground(foreignKeyBackground);
                        }
                    }
                }
            }
        }
    }
}
                                                                