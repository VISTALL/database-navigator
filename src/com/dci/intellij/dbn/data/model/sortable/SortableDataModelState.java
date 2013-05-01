package com.dci.intellij.dbn.data.model.sortable;

import com.dci.intellij.dbn.data.model.DataModelState;
import com.dci.intellij.dbn.data.sorting.SingleColumnSortingState;

public class SortableDataModelState extends DataModelState {
    private SingleColumnSortingState sortingState = new SingleColumnSortingState();

    public SingleColumnSortingState getSortingState() {
        return sortingState;
    }

    public void setSortingState(SingleColumnSortingState sortingState) {
        this.sortingState = sortingState;
    }

}
