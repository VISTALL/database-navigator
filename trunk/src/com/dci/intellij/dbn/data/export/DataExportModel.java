package com.dci.intellij.dbn.data.export;

import com.dci.intellij.dbn.data.type.BasicDataType;
import com.intellij.openapi.project.Project;

public interface DataExportModel {
    String getTableName();
    int getColumnCount();
    int getRowCount();
    Object getValue(int rowIndex, int columnIndex);
    String getColumnName(int columnIndex);
    BasicDataType getBasicDataType(int columnIndex);
    Project getProject();
}
