package com.dci.intellij.dbn.common.ui.table.model;

public class SortableDataModelCell extends BasicDataModelCell implements Comparable {

    public SortableDataModelCell(SortableDataModelRow row, Object userValue, ColumnInfo columnInfo) {
        super(userValue, row, columnInfo);
    }

    public int compareTo(Object o) {
        DataModelCell cell = (DataModelCell) o;
        Comparable local = (Comparable) getUserValue();
        Comparable remote = (Comparable) cell.getUserValue();

        if (local == null) return -1;
        if (remote == null) return 1;
        // local class may differ from remote class for
        // columns with data conversion error
        if (local.getClass().equals(remote.getClass())) {
            return local.compareTo(remote);
        } else {
            Class typeClass = cell.getColumnInfo().getDataType().getTypeClass();
            return local.getClass().equals(typeClass) ? 1 :
                   remote.getClass().equals(typeClass) ? -1 : 0;
        }
    }

}
