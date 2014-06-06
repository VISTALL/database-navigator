package com.dci.intellij.dbn.editor.data.state.ui;

import com.dci.intellij.dbn.common.ui.dialog.DBNDialog;
import com.dci.intellij.dbn.editor.data.state.DatasetEditorState;
import com.dci.intellij.dbn.object.DBDataset;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Action;
import javax.swing.JComponent;

public class DatasetEditorStateDialog extends DBNDialog {
    private DatasetEditorStateForm stateForm;

    public DatasetEditorStateDialog(DBDataset dataset, DatasetEditorState editorState) {
        super(dataset.getProject(), "Sorting", true);
        setModal(true);
        setResizable(true);
        stateForm = new DatasetEditorStateForm(dataset, editorState);
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
        stateForm.applyChanges();
        super.doOKAction();
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
