package com.dci.intellij.dbn.editor.code.action;

import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.EditorUtil;
import com.dci.intellij.dbn.editor.code.SourceCodeEditor;
import com.dci.intellij.dbn.vfs.SourceCodeFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractSourceCodeEditorAction extends DumbAwareAction {
    public AbstractSourceCodeEditorAction(String text, String description, javax.swing.Icon icon) {
        super(text, description, icon);
    }

    @Nullable
    protected Editor getEditor(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        return EditorUtil.getSelectedEditor(project);
    }

    @Nullable
    protected SourceCodeFile getSourcecodeFile(AnActionEvent e) {
        Editor editor = getEditor(e);
        if (editor != null) {
            VirtualFile virtualFile =  FileDocumentManager.getInstance().getFile(editor.getDocument());
            if (virtualFile  instanceof SourceCodeFile) {
                return (SourceCodeFile) virtualFile;
            }
        }
        return null;
    }

    protected SourceCodeFile getSourcecodeFile(@NotNull Editor editor) {
        return (SourceCodeFile) FileDocumentManager.getInstance().getFile(editor.getDocument());
    }

    protected SourceCodeEditor getSourceCodeEditor(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        SourceCodeFile sourceCodeFile = getSourcecodeFile(e);
        if (project != null && sourceCodeFile != null) {
            return (SourceCodeEditor) FileEditorManager.getInstance(project).getSelectedEditor(sourceCodeFile.getDatabaseFile());
        }
        return null;                     
    }
}
