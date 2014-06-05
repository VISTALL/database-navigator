package com.dci.intellij.dbn.data.sorting;

import com.dci.intellij.dbn.object.DBColumn;

import java.util.ArrayList;
import java.util.List;

public class MultiColumnSortingState {
    private int maxColumns = 3;     
    private List<SortingInstruction> sortingInstructions = new ArrayList<SortingInstruction>();

    public void applySorting(DBColumn column, SortDirection direction, boolean isAddition) {
        SortingInstruction instruction = getInstruction(column);
        boolean isNewColumn = instruction == null;
        if (isNewColumn) {
            if (direction.isIndefinite()) {
                direction = SortDirection.ASCENDING;
            }
            instruction = new SortingInstruction(column, direction);
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
    
    private SortingInstruction getInstruction(DBColumn column) {
        for (SortingInstruction instruction : sortingInstructions) {
            if (instruction.getColumn().equals(column)) {
                return instruction;
            }
        }
        return null;
    }

    public List<SortingInstruction> getSortingInstructions() {
        return sortingInstructions;
    }

    public int getMaxColumns() {
        return maxColumns;
    }

    public void setMaxColumns(int maxColumns) {
        this.maxColumns = maxColumns;
        if (sortingInstructions.size() > maxColumns) {
            sortingInstructions = new ArrayList<SortingInstruction>(sortingInstructions.subList(0, maxColumns));
        }
    }
    
    public MultiColumnSortingState clone() {
        MultiColumnSortingState clone = new MultiColumnSortingState();
        for (SortingInstruction criterion : sortingInstructions) {
            clone.sortingInstructions.add(criterion.clone());
        }
        return clone;
    }
}
