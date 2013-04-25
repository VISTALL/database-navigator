package com.dci.intellij.dbn.editor.code.action;

import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.EditorUtil;
import com.dci.intellij.dbn.editor.code.SourceCodeEditor;
import com.dci.intellij.dbn.vfs.SourceCodeFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractSourceCodeEditorAction extends DumbAwareAction {
    public AbstractSourceCodeEditorAction(String text, String description, javax.swing.Icon icon) {
        super(text, description, icon);
    }

    @Nullable
    protected Editor getEditor(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        FileEditor fileEditor = EditorUtil.getFileEditor(project, e.getPlace());
        if (fileEditor instanceof PsiAwareTextEditorImpl) {
            PsiAwareTextEditorImpl textEditor = (PsiAwareTextEditorImpl) fileEditor;
            return textEditor.getEditor();
        }

        if (fileEditor instanceof SourceCodeEditor) {
            SourceCodeEditor sourceCodeEditor = (SourceCodeEditor) fileEditor;
            return sourceCodeEditor.getEditor();
        }
        return null;
    }

    @Nullable
    protected SourceCodeFile getSourcecodeFile(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        if (project != null) {
            VirtualFile virtualFile = EditorUtil.getVirtualFile(project, e.getPlace());
            if (virtualFile instanceof SourceCodeFile) {
                return (SourceCodeFile) virtualFile;
            }
        }
        return null;
    }
}
