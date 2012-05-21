package com.dci.intellij.dbn.common.ui.table.listener;

import com.dci.intellij.dbn.common.ui.table.SortableTable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SortableTableMouseListener extends MouseAdapter{
    private SortableTable table;

    public SortableTableMouseListener(SortableTable table) {
        this.table = table;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
    }
}
