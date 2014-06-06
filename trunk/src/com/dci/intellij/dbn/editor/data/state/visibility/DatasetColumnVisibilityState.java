package com.dci.intellij.dbn.editor.data.state.visibility;

import java.util.ArrayList;
import java.util.List;

public class DatasetColumnVisibilityState {
    private List<DatasetColumnVisibility> columns = new ArrayList<DatasetColumnVisibility>();

    public List<DatasetColumnVisibility> getColumns() {
        return columns;
    }
}
