package com.dci.intellij.dbn.common.ui.table.model;

import java.util.ArrayList;
import java.util.List;

public class BasicDataModelRow<T extends DataModelCell> implements DataModelRow<T> {
    protected BasicDataModel model;
    protected List<T> cells;
    private int index;

    public BasicDataModelRow(BasicDataModel model) {
        cells = new ArrayList<T>(model.getColumnCount());
        this.model = model;
    }

    public BasicDataModel getModel() {
        return model;
    }

    public List<T> getCells() {
        return cells;
    }

    public T getCellAtIndex(int index) {
        return cells.get(index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void dispose() {
        for (DataModelCell cell : cells) {
            cell.dispose();
        }
        cells.clear();
        cells = null;
        model = null;
    }
}
