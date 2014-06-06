package com.dci.intellij.dbn.editor.data.state.visibility.ui;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.ui.list.CheckBoxList;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.editor.data.state.visibility.DatasetColumnVisibility;
import com.dci.intellij.dbn.editor.data.state.visibility.DatasetColumnVisibilityState;
import com.dci.intellij.dbn.editor.data.state.visibility.action.MoveDownAction;
import com.dci.intellij.dbn.editor.data.state.visibility.action.MoveUpAction;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListModel;
import java.awt.BorderLayout;
import java.util.Collections;
import java.util.List;

public class DatasetColumnVisibilityForm  extends DBNFormImpl {
    private JPanel mainPanel;
    private JPanel actionPanel;
    private JBScrollPane columnListScrollPane;
    private CheckBoxList columnList;
    private DatasetColumnVisibilityState visibilityState;

    public DatasetColumnVisibilityForm(DBDataset dataset, DatasetColumnVisibilityState visibilityState) {
        if (!visibilityState.isInitialized()) {
            visibilityState.init(dataset);
        }
        this.visibilityState = visibilityState;
        List<DatasetColumnVisibility> columns = visibilityState.getColumns();
        columnList = new CheckBoxList(columns, true);
        columnListScrollPane.setViewportView(columnList);

        ActionToolbar actionToolbar = ActionUtil.createActionToolbar("", true,
                new MoveUpAction(columnList),
                new MoveDownAction(columnList));
        actionPanel.add(actionToolbar.getComponent(), BorderLayout.WEST);

    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public void applyChanges(){
        columnList.applyChanges();
        ListModel model = columnList.getModel();
        for(int i=0; i<model.getSize(); i++ ) {
            DatasetColumnVisibility columnVisibility = (DatasetColumnVisibility) model.getElementAt(i);
            columnVisibility.setPosition(i);
        }
        Collections.sort(visibilityState.getColumns());
    }
}
