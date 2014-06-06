package com.dci.intellij.dbn.editor.data.state.sorting;

import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.data.sorting.SortingInstruction;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;

public class DatasetSortingInstruction extends SortingInstruction<DBColumn> {
    public DatasetSortingInstruction(DBColumn column, SortDirection direction) {
        super(DBObjectRef.from(column), direction);
    }
}
