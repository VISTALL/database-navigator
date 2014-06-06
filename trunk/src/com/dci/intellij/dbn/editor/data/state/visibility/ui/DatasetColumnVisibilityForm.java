package com.dci.intellij.dbn.editor.data.state.visibility.ui;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.ui.list.CheckBoxList;
import com.dci.intellij.dbn.editor.data.state.visibility.DatasetColumnVisibilityState;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class DatasetColumnVisibilityForm  extends DBNFormImpl {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JBScrollPane columnListScrollPane;
    private CheckBoxList lookupObjectsList;

    public DatasetColumnVisibilityForm(DBDataset dataset, DatasetColumnVisibilityState visibilityState) {
        lookupObjectsList = new CheckBoxList(visibilityState.getColumns());
    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }
}
