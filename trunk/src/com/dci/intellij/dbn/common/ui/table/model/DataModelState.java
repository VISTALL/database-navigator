package com.dci.intellij.dbn.common.ui.table.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataModelState {
    protected Map<String, String> contentTypesMap;
    private boolean isReadonly;
    private int rowCount;
    private List<String> columns;

    public void setTextContentType(String columnName, String contentTypeName) {
        if (contentTypesMap == null) contentTypesMap = new HashMap<String, String>();
        contentTypesMap.put(columnName, contentTypeName);
    }

    public String getTextContentTypeName(String columnName) {
        if (contentTypesMap != null) {
            return contentTypesMap.get(columnName);
        }
        return null;
    }

    public boolean isReadonly() {
        return isReadonly;
    }

    public void setReadonly(boolean readonly) {
        isReadonly = readonly;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
