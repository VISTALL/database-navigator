package com.dci.intellij.dbn.ddl.action;

import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.ddl.DDLFileBindingManager;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;

public class UnbindDDLFileAction extends AnAction {
    private DBSchemaObject object;
    public UnbindDDLFileAction(DBSchemaObject object) {
        super("Unbind files");
        this.object = object;
    }

    public void actionPerformed(AnActionEvent e) {
        Project project = object.getProject();
        DDLFileBindingManager fileBindingManager = DDLFileBindingManager.getInstance(project);
        fileBindingManager.unbindDDLFiles(object);
    }

    public void update(AnActionEvent e) {

        Project project = ActionUtil.getProject(e);
        DDLFileBindingManager fileBindingManager = DDLFileBindingManager.getInstance(project);
        boolean hasBoundDDLFiles = fileBindingManager.hasBoundDDLFiles(object);
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(hasBoundDDLFiles);
    }
}