package com.dci.intellij.dbn.editor.data.model;

import com.dci.intellij.dbn.data.model.ColumnInfo;
import com.dci.intellij.dbn.data.type.BasicDataType;
import com.dci.intellij.dbn.data.type.DBDataType;
import com.dci.intellij.dbn.editor.data.DatasetEditorUtils;
import com.dci.intellij.dbn.editor.data.options.DataEditorSettings;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatasetEditorColumnInfo implements ColumnInfo {
    private static final List<String> EMPTY_LIST = new ArrayList<String>(0);
    private List<String> possibleValues;
    private DBObjectRef<DBColumn> column;
    private int columnIndex;

    public DatasetEditorColumnInfo(DBColumn column, int columnIndex) {
        this.column = column.getRef();
        this.columnIndex = columnIndex;
    }

    @Nullable
    public DBColumn getColumn() {
        return column.get();
    }

    public String getName() {
        return column.getName();
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public DBDataType getDataType() {
        return getColumn().getDataType();
    }

    public synchronized List<String> getPossibleValues() {
        if (possibleValues == null) {
            setPossibleValues(EMPTY_LIST);
            List<String> values;
            DBColumn column = getColumn();
            if (column != null) {
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
        }
        return possibleValues;
    }

    public void setPossibleValues(List<String> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public void dispose() {
        if (possibleValues != null) possibleValues.clear();
    }

    public boolean isSortable() {
        DBColumn column = getColumn();
        DBDataType type = column == null ? null : column.getDataType();
        return type != null && type.isNative() &&
                type.getNativeDataType().getBasicDataType().is(
                        BasicDataType.LITERAL,
                        BasicDataType.NUMERIC,
                        BasicDataType.DATE_TIME);
    }

}
