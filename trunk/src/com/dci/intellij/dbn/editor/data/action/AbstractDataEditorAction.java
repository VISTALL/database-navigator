package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.ui.UIUtil;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.EditorUtil;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
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

    public static DatasetEditor getActiveDatasetEditor(Project project) {
        if (project != null) {
            FileEditor[] fileEditors = FileEditorManager.getInstance(project).getSelectedEditors();
            for (FileEditor fileEditor : fileEditors) {
                if (fileEditor instanceof DatasetEditor && UIUtil.isFocused(fileEditor.getComponent(), true)) {
                    return (DatasetEditor) fileEditor;
                }
            }
        }
        return null;
    }

    public static DatasetEditor getDatasetEditor(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        if (project != null) {
            if (ActionPlaces.MAIN_MENU.equals(e.getPlace())) {
                // action is triggered via shortcut
                return getActiveDatasetEditor(project);
            } else {
                FileEditor fileEditor = EditorUtil.getFileEditor(project, e.getPlace());
                return (DatasetEditor) fileEditor;
            }
        }
        return null;
    }
}
