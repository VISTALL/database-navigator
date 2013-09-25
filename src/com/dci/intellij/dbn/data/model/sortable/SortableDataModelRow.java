package com.dci.intellij.dbn.data.model.sortable;

import com.dci.intellij.dbn.data.model.basic.BasicDataModelRow;

public class SortableDataModelRow<T extends SortableDataModelCell> extends BasicDataModelRow<T> implements Comparable {

    protected SortableDataModelRow(SortableDataModel model) {
        super(model);
    }

    @Override
    public SortableDataModel getModel() {
        return (SortableDataModel) super.getModel();
    }

    @Override
    public T getCellAtIndex(int index) {
        return super.getCellAtIndex(index);
    }

    public int compareTo(Object o) {
        SortableDataModel model = getModel();
        int index = model.getSortColumnIndex();

        if (index == -1) return 0;
        SortableDataModelRow row = (SortableDataModelRow) o;

        SortableDataModelCell local = getCellAtIndex(index);
        SortableDataModelCell remote = row.getCellAtIndex(index);

        int compareIndex = model.getSortDirection().getCompareIndex();

        if (remote == null && local == null) return 0;
        if (local == null) return -compareIndex;
        if (remote == null) return compareIndex;

        return compareIndex * local.compareTo(remote);
    }

}
