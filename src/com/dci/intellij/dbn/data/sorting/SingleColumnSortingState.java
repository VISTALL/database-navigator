package com.dci.intellij.dbn.data.sorting;

public class SingleColumnSortingState implements SortingState{
    private SortDirection direction = SortDirection.INDEFINITE;
    private String columnName = "";

    public SortDirection getDirection() {
        return direction;
    }

    public void setDirection(SortDirection direction) {
        this.direction = direction;
    }

    public void setDirectionAsString(String direction) {
        this.direction =
            "ASC".equals(direction) ? SortDirection.ASCENDING :
            "DESC".equals(direction) ? SortDirection.DESCENDING :
                    SortDirection.INDEFINITE;
    }


    public String getColumnName() {
        return columnName == null ? "" : columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isColumnName(String columnName) {
        return columnName.equals(this.columnName);
    }

    public boolean isDirection(SortDirection direction) {
        return this.direction == direction;
    }

    public String getDirectionAsString() {
        return
            direction == SortDirection.ASCENDING ? "ASC" :
            direction == SortDirection.DESCENDING ? "DESC" :
            "INDEFINITE";
    }

    public void swichDirection() {
        direction =
                direction == SortDirection.ASCENDING ? SortDirection.DESCENDING :
                direction == SortDirection.DESCENDING ? SortDirection.ASCENDING : SortDirection.ASCENDING;
    }

    public boolean isValid() {
        return
            direction != SortDirection.INDEFINITE &&
            columnName != null &&
            columnName.trim().length() > 0;
    }
}
