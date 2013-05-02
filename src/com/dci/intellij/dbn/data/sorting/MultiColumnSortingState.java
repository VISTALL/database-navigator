package com.dci.intellij.dbn.data.sorting;

import java.util.ArrayList;
import java.util.List;

public class MultiColumnSortingState<T> {
    private int maxColumns = 3;     
    private List<SortingInstruction<T>> sortingInstructions = new ArrayList<SortingInstruction<T>>();

    public void applySorting(T column, SortDirection direction, boolean isAddition) {
        SortingInstruction<T> instruction = getInstruction(column);
        boolean isNewColumn = instruction == null;
        if (isNewColumn) {
            if (direction.isIndefinite()) {
                direction = SortDirection.ASCENDING;
            }
            instruction = new SortingInstruction<T>(column, direction);
        } else {
            if (direction.isIndefinite()) {
                instruction.switchDirection();
            } else {
                instruction.setDirection(direction);
            }
        }


        if (isAddition) {
            if (isNewColumn) {
                if (sortingInstructions.size()== maxColumns) {
                    sortingInstructions.remove(sortingInstructions.size()-1);
                }
                sortingInstructions.add(instruction);
            }

        } else {
            sortingInstructions.clear();
            sortingInstructions.add(instruction);
        }
    }
    
    private SortingInstruction<T> getInstruction(T column) {
        for (SortingInstruction<T> instruction : sortingInstructions) {
            if (instruction.getColumn().equals(column)) {
                return instruction;
            }
        }
        return null;
    }

    public List<SortingInstruction<T>> getSortingInstructions() {
        return sortingInstructions;
    }

    public int getMaxColumns() {
        return maxColumns;
    }

    public void setMaxColumns(int maxColumns) {
        this.maxColumns = maxColumns;
        if (sortingInstructions.size() > maxColumns) {
            sortingInstructions = new ArrayList<SortingInstruction<T>>(sortingInstructions.subList(0, maxColumns));
        }
    }
    
    public MultiColumnSortingState<T> clone() {
        MultiColumnSortingState<T> clone = new MultiColumnSortingState<T>();
        for (SortingInstruction<T> criterion : sortingInstructions) {
            clone.sortingInstructions.add(criterion.clone());
        }
        return clone;
    }
}
