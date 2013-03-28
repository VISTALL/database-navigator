package com.dci.intellij.dbn.common.ui.dialog;

import com.dci.intellij.dbn.common.Constants;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;

public abstract class DBNDialog extends DialogWrapper{
   private Project project;
    protected DBNDialog(Project project, String title, boolean canBeParent) {
        super(project, canBeParent);
        setTitle(Constants.DBN_TITLE_PREFIX + title);
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
