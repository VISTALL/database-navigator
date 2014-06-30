package com.dci.intellij.dbn.editor.data.filter;

import com.dci.intellij.dbn.data.sorting.MultiColumnSortingState;
import com.dci.intellij.dbn.data.sorting.SingleColumnSortingState;
import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.data.sorting.SortingInstruction;
import com.dci.intellij.dbn.data.sorting.SortingState;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.DBTable;

import java.util.List;

public class DatasetFilterUtil {

    public static void addOrderByClause(DBDataset dataset, StringBuilder buffer, SortingState sortingState) {
        if (sortingState instanceof SingleColumnSortingState) {
            SingleColumnSortingState singleColumnSortingState = (SingleColumnSortingState) sortingState;
            if (singleColumnSortingState.isValid()) {
                buffer.append(" order by ");
                buffer.append(singleColumnSortingState.getColumnName());
                buffer.append(" ");
                buffer.append(singleColumnSortingState.getDirectionAsString());
            } else {
                if (dataset instanceof DBTable) {
                    DBTable table = (DBTable) dataset;
                    List<DBColumn> primaryKeyColumns = table.getPrimaryKeyColumns();
                    if (primaryKeyColumns.size() > 0) {
                        buffer.append(" order by ");
                        for (DBColumn column : primaryKeyColumns) {
                            buffer.append(column.getName());
                            if (primaryKeyColumns.get(primaryKeyColumns.size()-1) == column ) {
                                buffer.append(" asc");
                            } else {
                                buffer.append(", ");
                            }
                        }
                    }
                }
            }
        } else if (sortingState instanceof MultiColumnSortingState) {
            MultiColumnSortingState multiColumnSortingState = (MultiColumnSortingState) sortingState;
            List<SortingInstruction> sortingInstructions = multiColumnSortingState.getSortingInstructions();
            if (sortingInstructions.size() > 0) {
                buffer.append(" order by ");
                boolean instructionAdded = false;
                for (SortingInstruction sortingInstruction : sortingInstructions) {
                    SortDirection sortDirection = sortingInstruction.getDirection();
                    DBColumn column = dataset.getColumn(sortingInstruction.getColumnName());
                    if (column != null && !sortDirection.isIndefinite()) {
                        buffer.append(instructionAdded ? ", " : "");
                        buffer.append(column.getName());
                        buffer.append(" ");
                        buffer.append(sortDirection.getSqlToken());
                        instructionAdded = true;
                    }
                }
            }
        }
    }

    public static void addForUpdateClause(DBDataset dataset, StringBuilder buffer) {
        if (dataset instanceof DBTable && dataset.hasLobColumns()) {
            buffer.append(" for update");
        }
    }

    public static void createSelectStatement(DBDataset dataset, StringBuilder buffer) {
        buffer.append("select ");
        int index = 0;
        for (DBColumn column : dataset.getColumns()) {
            if (index > 0) {
                buffer.append(", ");
            }
            buffer.append(column.getQuotedName(false));
            index++;
        }
        buffer.append(" from ");
        buffer.append(dataset.getSchema().getQuotedName(false));
        buffer.append(".");
        buffer.append(dataset.getQuotedName(false));

    }

    public static void createSimpleSelectStatement(DBDataset dataset, StringBuilder buffer) {
        buffer.append("select a.* from ");
        buffer.append(dataset.getSchema().getQuotedName(false));
        buffer.append(".");
        buffer.append(dataset.getQuotedName(false));
        buffer.append(" a");

    }
}
