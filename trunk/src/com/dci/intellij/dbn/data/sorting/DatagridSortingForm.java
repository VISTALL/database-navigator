package com.dci.intellij.dbn.data.sorting;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class DatagridSortingForm extends DBNFormImpl{
    private JPanel mainPanel;

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }
}
