package com.dci.intellij.dbn.editor.data.state.sorting.action;

import com.dci.intellij.dbn.editor.data.state.sorting.ui.DatasetSortingColumnForm;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;


public class SelectColumnAction extends DumbAwareAction {
    private DatasetSortingColumnForm sortingColumnForm;
    private DBObjectRef<DBColumn> columnRef;

    public SelectColumnAction(DatasetSortingColumnForm sortingColumnForm, DBColumn column) {
        super(column.getPresentableText(), null, column.getIcon());
        this.sortingColumnForm = sortingColumnForm;
    }

    public void actionPerformed(AnActionEvent e) {
        sortingColumnForm.selectColumn(columnRef.get());
    }
}
