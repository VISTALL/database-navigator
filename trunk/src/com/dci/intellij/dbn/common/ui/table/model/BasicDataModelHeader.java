package com.dci.intellij.dbn.common.ui.table.model;


import com.dci.intellij.dbn.data.type.DBDataType;

import java.util.ArrayList;
import java.util.List;

public class BasicDataModelHeader implements DataModelHeader {
    private List<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();

    public List<ColumnInfo> getColumnInfos() {
        return columnInfos;
    }

    public ColumnInfo getColumnInfo(int columnIndex) {
        return columnInfos.get(columnIndex);
    }

    public int getColumnIndex(String name) {
        for (int i=0; i<columnInfos.size(); i++) {
            ColumnInfo columnInfo = columnInfos.get(i);
            if (columnInfo.getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public String getColumnName(int columnIndex) {
        return getColumnInfo(columnIndex).getName();
    }

    public DBDataType getColumnDataType(int columnIndex) {
        return getColumnInfo(columnIndex).getDataType();
    }

    public int getColumnCount() {
        return columnInfos.size();
    }

    public void dispose() {
        for (ColumnInfo columnInfo : columnInfos) {
            columnInfo.dispose();
        }
        columnInfos.clear();
    }
}
