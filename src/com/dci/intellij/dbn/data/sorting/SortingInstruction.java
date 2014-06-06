package com.dci.intellij.dbn.data.sorting;

public class SortingInstruction<T> {
    private T column;
    private SortDirection direction;

    public SortingInstruction(T column, SortDirection direction) {
        this.column = column;
        this.direction = direction;
    }

    public T getColumn() {
        return column;
    }

    public void setColumn(T column) {
        this.column = column;
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
        return new SortingInstruction(column, direction);
    }
}
