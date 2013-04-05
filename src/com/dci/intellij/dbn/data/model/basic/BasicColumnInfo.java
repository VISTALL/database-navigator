package com.dci.intellij.dbn.data.model.basic;

import com.dci.intellij.dbn.data.model.ColumnInfo;
import com.dci.intellij.dbn.data.type.BasicDataType;
import com.dci.intellij.dbn.data.type.DBDataType;

public class BasicColumnInfo implements ColumnInfo {
    protected String name;
    protected int columnIndex;
    protected DBDataType dataType;

    public BasicColumnInfo(String name, DBDataType dataType, int columnIndex) {
        this.name = name;
        this.columnIndex = columnIndex;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public DBDataType getDataType() {
        return dataType;
    }

    public void dispose() {
        dataType = null;
    }

    public boolean isSortable() {
        return dataType.isNative() && dataType.getNativeDataType().getBasicDataType().is(BasicDataType.LITERAL, BasicDataType.NUMERIC, BasicDataType.DATE_TIME);
    }

}
