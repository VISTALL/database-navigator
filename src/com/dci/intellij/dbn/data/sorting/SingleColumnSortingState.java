package com.dci.intellij.dbn.data.sorting;

import com.dci.intellij.dbn.common.util.StringUtil;
import org.jdom.Element;

public class SingleColumnSortingState extends SortingInstruction implements SortingState{
    public SingleColumnSortingState() {
        super("", SortDirection.INDEFINITE);
    }

    public void setDirectionAsString(String direction) {
        SortDirection sortDirection =
            "ASC".equals(direction) ? SortDirection.ASCENDING :
            "DESC".equals(direction) ? SortDirection.DESCENDING :
                    SortDirection.INDEFINITE;
        setDirection(sortDirection);
    }


    public boolean isColumnName(String columnName) {
        return columnName.equals(getColumnName());
    }

    public boolean isDirection(SortDirection direction) {
        return direction == getDirection();
    }

    public String getDirectionAsString() {
        SortDirection direction = getDirection();
        return
            direction == SortDirection.ASCENDING ? "ASC" :
            direction == SortDirection.DESCENDING ? "DESC" :
            "INDEFINITE";
    }

    public void swichDirection() {
        SortDirection direction = getDirection();
        direction =
                direction == SortDirection.ASCENDING ? SortDirection.DESCENDING :
                direction == SortDirection.DESCENDING ? SortDirection.ASCENDING : SortDirection.ASCENDING;
        setDirection(direction);
    }

    public boolean isValid() {
        return
            getDirection() != SortDirection.INDEFINITE &&
            StringUtil.isNotEmpty(getColumnName());
    }

    @Override
    public void readState(Element element) {

    }

    @Override
    public void writeState(Element element) {

    }

    @Override
    public SortingInstruction getSortingInstruction(String columnName) {
        if (columnName.equalsIgnoreCase(getColumnName())) {
            return this;
        }
        return null;
    }
}
