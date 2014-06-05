package com.dci.intellij.dbn.editor.data.ui.table.renderer;

import com.dci.intellij.dbn.common.util.CommonUtil;
import com.dci.intellij.dbn.data.editor.color.DataGridTextAttributes;
import com.dci.intellij.dbn.data.ui.table.basic.BasicTableCellRenderer;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorColumnInfo;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorModelCell;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorModelRow;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.dci.intellij.dbn.object.DBColumn;
import com.intellij.openapi.project.Project;
import com.intellij.ui.SimpleTextAttributes;

import javax.swing.JTable;
import javax.swing.border.Border;
import java.awt.Color;

public class DatasetEditorTableCellRenderer extends BasicTableCellRenderer {
    public DatasetEditorTableCellRenderer(Project project) {
        super(project);
    }

    protected void customizeCellRenderer(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {

        DatasetEditorModelCell cell = (DatasetEditorModelCell) value;
        DatasetEditorTable datasetEditorTable = (DatasetEditorTable) table;

        if (cell != null && !cell.isDisposed()) {
            DatasetEditorModelRow row = cell.getRow();
            DatasetEditorColumnInfo columnInfo = cell.getColumnInfo();
            boolean isLoading = datasetEditorTable.isLoading();
            boolean isInserting = datasetEditorTable.isInserting();

            boolean isDeletedRow = row.isDeleted();
            boolean isInsertRow = row.isInsert();
            boolean isCaretRow = !isInsertRow && table.getCellSelectionEnabled() && table.getSelectedRow() == rowIndex && table.getSelectedRowCount() == 1;
            boolean isModified = cell.isModified();

            DataGridTextAttributes attributes = getAttributes();
            SimpleTextAttributes textAttributes = attributes.getPlainData(isModified, isCaretRow);

            DBColumn column = columnInfo.getColumn();
            if (isSelected) {
                if (datasetEditorTable.isCellEditable(rowIndex, columnIndex)) {
                    if (!hasFocus || table.getSelectedRowCount() > 1 || table.getSelectedColumnCount() > 1) {
                        textAttributes = attributes.getSelection();
                    }
                } else {
                    textAttributes = attributes.getSelection();
                }
            } else if (isLoading || !datasetEditorTable.getDataset().getConnectionHandler().isConnected()) {
                textAttributes = attributes.getLoadingData(isCaretRow);
            } else if (isDeletedRow) {
                textAttributes = attributes.getDeletedData();
            } else if ((isInserting && !isInsertRow)) {
                textAttributes = attributes.getReadonlyData(isModified, isCaretRow);
            } else if (isPrimaryKey(column)) {
                textAttributes = attributes.getPrimaryKey(isModified, isCaretRow);
            } else if (isForeignKey(column)) {
                textAttributes = attributes.getForeignKey(isModified, isCaretRow);
            } else if (cell.isLobValue()) {
                textAttributes = attributes.getReadonlyData(isModified, isCaretRow);
            }

            Color background = CommonUtil.nvl(textAttributes.getBgColor(), table.getBackground());
            Color foreground = CommonUtil.nvl(textAttributes.getFgColor(), table.getForeground());


            Border border = getLineBorder(background);

            if (cell.hasError()) {
                border = getLineBorder(SimpleTextAttributes.ERROR_ATTRIBUTES.getFgColor());
                SimpleTextAttributes errorData = attributes.getErrorData();
                background = errorData.getBgColor();
                foreground = errorData.getFgColor();
                textAttributes = textAttributes.derive(errorData.getStyle(), foreground, background, null);
            }

            setBorder(border);
            setBackground(background);
            setForeground(foreground);
            writeUserValue(cell, textAttributes, attributes);


/*            //DataModelCell cellAtMouseLocation = datasetEditorTable.getCellAtMouseLocation();
            if (true) return;


            if (cell.getUserValue() != null) {
                textAttributes =
                        isSelected ? attributes.getSelection() :
                        isLoading || !datasetEditorTable.getDataset().getConnectionHandler().isConnected() ? attributes.getLoadingData() :
                        isInserting && !isInsertRow ? attributes.getReadonlyData() :
                        isDeletedRow ? attributes.getDeletedData() :
                        isPrimaryKey(column) ? (isCaretRow ? attributes.getPrimaryKeyAtCaretRow() : attributes.getPrimaryKey()) :
                        isForeignKey(column) ? (isCaretRow ? attributes.getForeignKeyAtCaretRow() : attributes.getForeignKey()) :
                        cell.isModified() ? attributes.getModifiedData() :
                        cell.isLobValue() ? attributes.getReadonlyData() :
                                            attributes.getPlainData();

                writeUserValue(cell, textAttributes, attributes);

                
            } *//*else {
                    append("[null]", SimpleTextAttributes.GRAYED_ATTRIBUTES);
                }*//*

            //updateBorder(cell, datasetEditorTable);
            


            setBorder(null);
            if (!isSelected) {
                if (isLoading || !datasetEditorTable.getDataset().getConnectionHandler().isConnected()) {
                    setBackground(attributes.getLoadingData().getBgColor());
                } else {

                    if (isCaretRow) {
                        setBackground(attributes.getCaretRowBgColor());

                    } else if (isDeletedRow) {
                        setBackground(attributes.getDeletedData().getBgColor());

                    } else if (cell.hasError()) {
                        setBackground(attributes.getErrorData().getBgColor());

                    } else if (isInserting && !isInsertRow) {
                        setBackground(attributes.getReadonlyData().getBgColor());

                    } else if (isInsertRow) {
                        setBackground(attributes.getPlainData().getBgColor());

                    } else if (isPrimaryKey(column)) {
                        setBackground(attributes.getPrimaryKey().getBgColor());

                    } else if (isForeignKey(column)) {
                        setBackground(attributes.getForeignKey().getBgColor());

                    } else {
                        setBackground(attributes.getPlainData().getBgColor());
                    }
                }
            }*/
        }
    }

    private boolean isForeignKey(DBColumn column) {
        return column != null && column.isForeignKey();
    }

    private boolean isPrimaryKey(DBColumn column) {
        return column != null && column.isPrimaryKey();
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
    }
}
                                                                