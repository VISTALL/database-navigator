package com.dci.intellij.dbn.editor.data.state;

import com.dci.intellij.dbn.common.AbstractProjectComponent;
import com.dci.intellij.dbn.editor.data.state.ui.DatasetEditorStateDialog;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DatasetEditorStateManager extends AbstractProjectComponent implements JDOMExternalizable {
    private DatasetEditorStateManager(Project project) {
        super(project);
    }


    public static DatasetEditorStateManager getInstance(Project project) {
        return project.getComponent(DatasetEditorStateManager.class);
    }

    public void openStateDialog(DBDataset dataset, DatasetEditorState editorState) {
        DatasetEditorStateDialog dialog = new DatasetEditorStateDialog(dataset, editorState);
        dialog.show();
    }

    /***************************************
    *            ProjectComponent           *
    ****************************************/
    @NonNls
    @NotNull
    public String getComponentName() {
        return "DBNavigator.Project.DatasetStateManager";
    }
    public void disposeComponent() {
        super.disposeComponent();
    }

    /*************************************************
    *               JDOMExternalizable              *
    *************************************************/
    public void readExternal(Element element) throws InvalidDataException {
    }

    public void writeExternal(Element element) throws WriteExternalException {
    }

}
