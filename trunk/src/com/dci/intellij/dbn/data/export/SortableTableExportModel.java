package com.dci.intellij.dbn.data.export;

import com.dci.intellij.dbn.common.ui.table.SortableTable;
import com.dci.intellij.dbn.common.ui.table.model.ColumnInfo;
import com.dci.intellij.dbn.common.ui.table.model.SortableDataModelCell;
import com.dci.intellij.dbn.data.type.BasicDataType;
import com.dci.intellij.dbn.data.type.DBNativeDataType;
import com.intellij.openapi.project.Project;

public class SortableTableExportModel implements DataExportModel{
    private boolean selection;
    private SortableTable table;

    int[] selectedRows;
    int[] selectedColumns;

    public SortableTableExportModel(boolean selection, SortableTable table) {
        this.selection = selection;
        this.table = table;

        if (selection) {
            selectedRows = table.getSelectedRows();
            selectedColumns = table.getSelectedColumns();
        }
    }

    public Project getProject() {
        return table.getProject();
    }

    public String getTableName() {
        return table.getName();
    }

    public int getColumnCount() {
        return selection ?
            selectedColumns.length :
            table.getModel().getColumnCount();
    }

    public int getRowCount() {
        return selection ?
            selectedRows.length :
            table.getModel().getRowCount();
    }

    public Object getValue(int rowIndex, int columnIndex) {
        int realRowIndex = getRealRowIndex(rowIndex);
        int realColumnIndex = getRealColumnIndex(columnIndex);
        SortableDataModelCell dataModelCell = (SortableDataModelCell) table.getModel().getValueAt(realRowIndex, realColumnIndex);
        return dataModelCell == null ? null : dataModelCell.getUserValue();
    }

    public String getColumnName(int columnIndex) {
        int realColumnIndex = getRealColumnIndex(columnIndex);
        return table.getModel().getColumnName(realColumnIndex);
    }

    public BasicDataType getBasicDataType(int columnIndex) {
        int realColumnIndex = getRealColumnIndex(columnIndex);
        ColumnInfo columnInfo = table.getModel().getColumnInfo(realColumnIndex);
        DBNativeDataType nativeDataType = columnInfo.getDataType().getNativeDataType();

        return nativeDataType == null ?
                BasicDataType.LITERAL :
                nativeDataType.getDataTypeDefinition().getBasicDataType();

    }

    /**
     * Returns the column index from the underlying sortable table model.
     */
    private int getRealColumnIndex(int columnIndex) {
        if (selection) {
            int selectedColumnIndex = selectedColumns[columnIndex];
            return table.getModelColumnIndex(selectedColumnIndex);
        } else {
            return table.getModelColumnIndex(columnIndex);
        }
    }

    private int getRealRowIndex(int rowIndex) {
        if (selection) {
            return selectedRows[rowIndex];
        } else {
            return rowIndex;
        }

    }
}
