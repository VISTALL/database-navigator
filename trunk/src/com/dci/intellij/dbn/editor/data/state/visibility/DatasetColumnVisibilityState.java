package com.dci.intellij.dbn.editor.data.state.visibility;

import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import org.jdom.Element;

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

    public void init(DBDataset dataset, Element element) {
        if (element != null) {
            List<Element> children = element.getChildren();
            for (Element child : children) {
                if (columns == null) columns = new ArrayList<DatasetColumnVisibility>();
                DatasetColumnVisibility columnVisibility = new DatasetColumnVisibility(dataset, child);
                columns.add(columnVisibility);
            }
        }

    }

    public boolean isInitialized() {
        return columns != null;
    }
}
