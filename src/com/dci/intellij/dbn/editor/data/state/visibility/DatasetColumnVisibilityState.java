package com.dci.intellij.dbn.editor.data.state.visibility;

import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatasetColumnVisibilityState {
    private List<DatasetColumnVisibility> columns;

    public List<DatasetColumnVisibility> getColumns() {
        return columns;
    }

    public synchronized void init(DBDataset dataset) {
        columns = new ArrayList<DatasetColumnVisibility>();
        for (DBColumn column : dataset.getColumns()) {
            columns.add(new DatasetColumnVisibility(column));
        }
        Collections.sort(columns);
    }

    public boolean isInitialized() {
        return columns != null;
    }
}
