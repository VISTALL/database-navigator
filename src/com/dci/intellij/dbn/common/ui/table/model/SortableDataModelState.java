package com.dci.intellij.dbn.common.ui.table.model;

import com.dci.intellij.dbn.common.ui.table.SortingState;

public class SortableDataModelState extends DataModelState {
    private SortingState sortingState = new SortingState();

    public SortingState getSortingState() {
        return sortingState;
    }

    public void setSortingState(SortingState sortingState) {
        this.sortingState = sortingState;
    }

}
