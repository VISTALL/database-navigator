package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.EditorUtil;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;

import javax.swing.Icon;

public abstract class AbstractDataEditorAction extends DumbAwareAction {
    public AbstractDataEditorAction(String text) {
        super(text);
    }

    public AbstractDataEditorAction(String text, Icon icon) {
        super(text, null, icon);
    }

    public DatasetEditor getDatasetEditor(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        return (DatasetEditor) EditorUtil.getFileEditor(project, e.getPlace());
    }
}
