package com.dci.intellij.dbn.common.sorting;

public enum SortDirection {
    UNDEFINED(0),
    ASCENDING(1),
    DESCENDING(-1);

    private int compareIndex;

    SortDirection(int compareIndex) {
        this.compareIndex = compareIndex;
    }

    public int getCompareIndex() {
        return compareIndex;
    }
}
