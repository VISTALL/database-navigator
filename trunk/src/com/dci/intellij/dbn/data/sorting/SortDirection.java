package com.dci.intellij.dbn.data.sorting;

public enum SortDirection {
    INDEFINITE(0),
    ASCENDING(1),
    DESCENDING(-1);

    private int compareIndex;

    SortDirection(int compareIndex) {
        this.compareIndex = compareIndex;
    }

    public int getCompareIndex() {
        return compareIndex;
    }

    public boolean isIndefinite() {
        return this == INDEFINITE;
    }
}
