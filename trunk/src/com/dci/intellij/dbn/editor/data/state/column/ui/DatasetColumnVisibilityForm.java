package com.dci.intellij.dbn.editor.data.state.column.ui;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.ui.list.CheckBoxList;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.editor.data.state.column.DatasetColumnState;
import com.dci.intellij.dbn.editor.data.state.column.DatasetHeaderState;
import com.dci.intellij.dbn.editor.data.state.column.action.MoveDownAction;
import com.dci.intellij.dbn.editor.data.state.column.action.MoveUpAction;
import com.dci.intellij.dbn.editor.data.state.column.action.OrderAlphabeticallyAction;
import com.dci.intellij.dbn.editor.data.state.column.action.RevertColumnOrderAction;
import com.dci.intellij.dbn.editor.data.state.column.action.SelectAllColumnsAction;
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
    private DatasetHeaderState headerState;

    public DatasetColumnVisibilityForm(DBDataset dataset, DatasetHeaderState headerState) {
        this.headerState = headerState;
        List<DatasetColumnState> columnStates = headerState.getColumnStates();
        columnList = new CheckBoxList(columnStates, true);
        columnListScrollPane.setViewportView(columnList);

        ActionToolbar actionToolbar = ActionUtil.createActionToolbar("", true,
                new SelectAllColumnsAction(columnList),
                ActionUtil.SEPARATOR,
                new OrderAlphabeticallyAction(columnList),
                new MoveUpAction(columnList),
                new MoveDownAction(columnList),
                new RevertColumnOrderAction(columnList));
        actionPanel.add(actionToolbar.getComponent(), BorderLayout.WEST);

    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public boolean applyChanges(){
        boolean changed = columnList.applyChanges();
        ListModel model = columnList.getModel();
        for(int i=0; i<model.getSize(); i++ ) {
            DatasetColumnState columnState = (DatasetColumnState) columnList.getElementAt(i);
            changed = changed || columnState.getPosition() != i;
            columnState.setPosition(i);
        }
        Collections.sort(headerState.getColumnStates());
        return changed;
    }
}
