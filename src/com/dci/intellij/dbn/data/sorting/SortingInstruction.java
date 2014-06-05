package com.dci.intellij.dbn.data.sorting;

import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;

public class SortingInstruction {
    private DBObjectRef<DBColumn> columnRef;
    private SortDirection direction;

    public SortingInstruction(DBColumn column, SortDirection direction) {
        this.columnRef = column.getRef();
        this.direction = direction;
    }

    private SortingInstruction(DBObjectRef<DBColumn> columnRef, SortDirection direction) {
        this.columnRef = columnRef;
        this.direction = direction;
    }

    public DBColumn getColumn() {
        return columnRef.get();
    }

    public void setColumn(DBColumn column) {
        this.columnRef = column.getRef();
    }

    public void setColumnRef(DBObjectRef<DBColumn> columnRef) {
        this.columnRef = columnRef;
    }

    public SortDirection getDirection() {
        return direction;
    }

    public void setDirection(SortDirection direction) {
        this.direction = direction;
    }

    public void switchDirection() {
        if (direction == SortDirection.ASCENDING) {
            direction = SortDirection.DESCENDING;
        } else if (direction == SortDirection.DESCENDING) {
            direction = SortDirection.ASCENDING;
        }
    }

    public SortingInstruction clone() {
        return new SortingInstruction(columnRef, direction);
    }
}
