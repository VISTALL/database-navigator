package com.dci.intellij.dbn.editor.data.sorting;

import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.data.sorting.SortingInstruction;
import com.dci.intellij.dbn.object.DBColumn;

public class DatasetSortingInstruction extends SortingInstruction<DBColumn> {
    public DatasetSortingInstruction(DBColumn column, SortDirection direction) {
        super(column, direction);
    }
}
