package com.dci.intellij.dbn.common.ui.table.model;

import com.dci.intellij.dbn.data.type.DBDataType;
import com.intellij.openapi.Disposable;

import java.util.List;

public interface DataModelHeader extends Disposable {
    List<ColumnInfo> getColumnInfos();

    ColumnInfo getColumnInfo(int columnIndex);

    int getColumnIndex(String name);

    String getColumnName(int columnIndex);

    DBDataType getColumnDataType(int columnIndex);

    int getColumnCount();
}
