package com.dci.intellij.dbn.editor.data.sorting;

import com.dci.intellij.dbn.common.AbstractProjectComponent;
import com.dci.intellij.dbn.editor.data.sorting.ui.DatasetSortingDialog;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DatasetSortingManager extends AbstractProjectComponent implements JDOMExternalizable {
    private DatasetSortingManager(Project project) {
        super(project);
    }


    public static DatasetSortingManager getInstance(Project project) {
        return project.getComponent(DatasetSortingManager.class);
    }

    public void openSortingDialog(DBDataset dataset, DatasetSortingState sortingState) {
        DatasetSortingDialog dialog = new DatasetSortingDialog(dataset, sortingState);
        dialog.show();
    }

    /***************************************
    *            ProjectComponent           *
    ****************************************/
    @NonNls
    @NotNull
    public String getComponentName() {
        return "DBNavigator.Project.DatasetSortingManager";
    }
    public void disposeComponent() {
    }

    /*************************************************
    *               JDOMExternalizable              *
    *************************************************/
    public void readExternal(Element element) throws InvalidDataException {
    }

    public void writeExternal(Element element) throws WriteExternalException {
    }

}
