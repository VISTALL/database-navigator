package com.dci.intellij.dbn.data.sorting;

import com.dci.intellij.dbn.common.Reference;
import com.dci.intellij.dbn.common.Referenceable;

public class SortingInstruction<T extends Referenceable> {
    private Reference<T> columnRef;
    private SortDirection direction;

    public SortingInstruction(T column, SortDirection direction) {
        this.columnRef = column.getRef();
        this.direction = direction;
    }

    public SortingInstruction(Reference<T> columnRef, SortDirection direction) {
        this.columnRef = columnRef;
        this.direction = direction;
    }

    public T getColumn() {
        return columnRef == null ? null : columnRef.get();
    }

    public void setColumn(T column) {
        this.columnRef = column.getRef();
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

    public SortingInstruction<T> clone() {
        return new SortingInstruction<T>(columnRef, direction);
    }
}
