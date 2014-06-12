package com.dci.intellij.dbn.editor.data.state.ui;

import com.dci.intellij.dbn.common.ui.dialog.DBNDialog;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Action;
import javax.swing.JComponent;

public class DatasetEditorStateDialog extends DBNDialog {
    private DatasetEditorStateForm stateForm;
    private DatasetEditor datasetEditor;

    public DatasetEditorStateDialog(DatasetEditor datasetEditor) {
        super(datasetEditor.getProject(), "Sorting", true);
        this.datasetEditor = datasetEditor;
        setModal(true);
        setResizable(true);
        stateForm = new DatasetEditorStateForm(datasetEditor.getDataset(), datasetEditor.getState());
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
        boolean changed = stateForm.applyChanges();
        super.doOKAction();
        if (changed) {
            datasetEditor.loadData(true, true, true, true);
        }
    }

    @Nullable
    protected JComponent createCenterPanel() {
        return stateForm.getComponent();
    }

    @Override
    protected void dispose() {
        super.dispose();
        stateForm.dispose();
    }
}
