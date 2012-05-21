package com.dci.intellij.dbn.ddl.action;

import com.dci.intellij.dbn.ddl.DDLFileBindingManager;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public class BindDDLFileAction extends AnAction {
    private DBSchemaObject object;
    public BindDDLFileAction(DBSchemaObject object) {
        super("Bind files");
        this.object = object;
    }

    public void actionPerformed(AnActionEvent e) {
        Project project = object.getProject();
        DDLFileBindingManager fileBindingManager = DDLFileBindingManager.getInstance(project);
        fileBindingManager.bindDDLFiles(object);
    }
}
