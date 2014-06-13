package com.dci.intellij.dbn.editor.data.state.column.ui;

import com.dci.intellij.dbn.common.ui.dialog.DBNDialog;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Action;
import javax.swing.JComponent;

public class DatasetColumnSetupDialog extends DBNDialog {
    private DatasetColumnSetupForm columnSetupForm;
    private DatasetEditor datasetEditor;

    public DatasetColumnSetupDialog(DatasetEditor datasetEditor) {
        super(datasetEditor.getProject(), "Column Setup", true);
        this.datasetEditor = datasetEditor;
        setModal(true);
        setResizable(true);
        columnSetupForm = new DatasetColumnSetupForm(datasetEditor);
        getCancelAction().putValue(Action.NAME, "Close");
        init();
    }

    protected String getDimensionServiceKey() {
        return "DBNavigator.DatasetColumnSetup";
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
        boolean changed = columnSetupForm.applyChanges();
        super.doOKAction();
        if (changed) {
            datasetEditor.loadData(true, true, true, true);
        }
    }

    @Nullable
    protected JComponent createCenterPanel() {
        return columnSetupForm.getComponent();
    }

    @Override
    protected void dispose() {
        super.dispose();
        columnSetupForm.dispose();
    }
}
