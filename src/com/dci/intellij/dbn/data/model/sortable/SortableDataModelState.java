package com.dci.intellij.dbn.data.model.sortable;

import com.dci.intellij.dbn.data.model.DataModelSortingState;
import com.dci.intellij.dbn.data.model.DataModelState;

public class SortableDataModelState extends DataModelState {
    private DataModelSortingState sortingState = new DataModelSortingState();

    public DataModelSortingState getSortingState() {
        return sortingState;
    }

    public void setSortingState(DataModelSortingState sortingState) {
        this.sortingState = sortingState;
    }

}
