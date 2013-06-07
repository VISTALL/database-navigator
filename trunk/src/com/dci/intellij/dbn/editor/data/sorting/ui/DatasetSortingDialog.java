package com.dci.intellij.dbn.editor.data.sorting.ui;

import com.dci.intellij.dbn.common.ui.dialog.DBNDialog;
import com.dci.intellij.dbn.editor.data.sorting.DatasetSortingState;
import com.dci.intellij.dbn.object.DBDataset;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Action;
import javax.swing.JComponent;

public class DatasetSortingDialog extends DBNDialog {
    private DatasetSortingForm sortingForm;

    public DatasetSortingDialog(DBDataset dataset, DatasetSortingState sortingState) {
        super(dataset.getProject(), "Sorting", true);
        setModal(true);
        setResizable(true);
        sortingForm = new DatasetSortingForm(dataset, sortingState);
        getCancelAction().putValue(Action.NAME, "Close");
        init();
    }

    protected String getDimensionServiceKey() {
        return "DBNavigator.DatasetSorting";
    }

    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                getOKAction(),
                getCancelAction(),
                getHelpAction()
        };
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }

    @Nullable
    protected JComponent createCenterPanel() {
        return sortingForm.getComponent();
    }

    @Override
    protected void dispose() {
        super.dispose();
        sortingForm.dispose();
    }
}
