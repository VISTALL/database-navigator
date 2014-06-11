package com.dci.intellij.dbn.data.model.basic;


import com.dci.intellij.dbn.common.dispose.DisposeUtil;
import com.dci.intellij.dbn.common.filter.Filter;
import com.dci.intellij.dbn.common.list.FiltrableList;
import com.dci.intellij.dbn.data.model.ColumnInfo;
import com.dci.intellij.dbn.data.model.DataModelHeader;
import com.dci.intellij.dbn.data.type.DBDataType;

import java.util.List;

public class BasicDataModelHeader implements DataModelHeader {
    private FiltrableList<ColumnInfo> columnInfos = new FiltrableList<ColumnInfo>();

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
        DisposeUtil.disposeCollection(columnInfos);
    }

    public void setColumnFilter(Filter<ColumnInfo> filter) {
        columnInfos.setFilter(filter);
    }
}
