package com.dci.intellij.dbn.data.sorting;

import java.util.HashMap;
import java.util.Map;

public class MultiColumnSortingState<T> {
    Map<T, SortDirection> columns = new HashMap<T, SortDirection>();

    public void applySorting(T column, boolean additional) {
        if (columns.containsKey(column)) {

        } else {

        }
    }

}
