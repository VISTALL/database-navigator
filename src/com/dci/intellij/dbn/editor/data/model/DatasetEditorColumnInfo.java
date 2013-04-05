package com.dci.intellij.dbn.editor.data.model;

import com.dci.intellij.dbn.data.model.ColumnInfo;
import com.dci.intellij.dbn.data.type.BasicDataType;
import com.dci.intellij.dbn.data.type.DBDataType;
import com.dci.intellij.dbn.editor.data.DatasetEditorUtils;
import com.dci.intellij.dbn.editor.data.options.DataEditorSettings;
import com.dci.intellij.dbn.object.DBColumn;

import java.util.ArrayList;
import java.util.List;

public class DatasetEditorColumnInfo implements ColumnInfo {
    private static final List<String> EMPTY_LIST = new ArrayList<String>(0);
    private List<String> possibleValues;
    private DBColumn column;
    private int columnIndex;

    public DatasetEditorColumnInfo(DBColumn column, int columnIndex) {
        this.column = column;
        this.columnIndex = columnIndex;
    }

    public DBColumn getColumn() {
        // columns loaded with quick loaders get disposed as soon as the main loader finishes
        column = (DBColumn) column.getUndisposedElement();
        return column;
    }

    public String getName() {
        return getColumn().getName();
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public DBDataType getDataType() {
        return getColumn().getDataType();
    }

    public List<String> getPossibleValues() {
        if (possibleValues == null) {
            setPossibleValues(EMPTY_LIST);
            List<String> values;
            if (column.isForeignKey()) {
                DBColumn foreignKeyColumn = column.getForeignKeyColumn();
                values = DatasetEditorUtils.loadDistinctColumnValues(foreignKeyColumn);
            } else {
                values = DatasetEditorUtils.loadDistinctColumnValues(column);
            }

            if (values != null) {
                DataEditorSettings dataEditorSettings = DataEditorSettings.getInstance(column.getProject());
                int maxElementCount = dataEditorSettings.getValueListPopupSettings().getElementCountThreshold();
                if (values.size() > maxElementCount) values.clear();
                possibleValues = values;
            }
        }
        return possibleValues;
    }

    public void setPossibleValues(List<String> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public void dispose() {
        column = null;
        if (possibleValues != null) possibleValues.clear();
    }

    public boolean isSortable() {
        DBDataType type = column.getDataType();
        if (column.getDataType().isNative()) {
            return type.getNativeDataType().getBasicDataType().is(BasicDataType.LITERAL, BasicDataType.NUMERIC, BasicDataType.DATE_TIME);
        }
        return false;                                                                                                                                            
    }

}
