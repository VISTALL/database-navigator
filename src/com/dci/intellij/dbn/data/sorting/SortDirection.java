package com.dci.intellij.dbn.data.sorting;

public enum SortDirection {
    INDEFINITE(0, ""),
    ASCENDING(1, "asc"),
    DESCENDING(-1, "desc");

    private int compareIndex;
    private String sqlToken;

    SortDirection(int compareIndex, String sqlToken) {
        this.compareIndex = compareIndex;
        this.sqlToken = sqlToken;
    }

    public int getCompareIndex() {
        return compareIndex;
    }

    public boolean isIndefinite() {
        return this == INDEFINITE;
    }

    public String getSqlToken() {
        return sqlToken;
    }
}
