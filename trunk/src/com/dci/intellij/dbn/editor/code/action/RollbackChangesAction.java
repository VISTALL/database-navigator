package com.dci.intellij.dbn.editor.code.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.thread.WriteActionRunner;
import com.dci.intellij.dbn.vfs.SourceCodeFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;

public class RollbackChangesAction extends AbstractSourceCodeEditorAction {
    public RollbackChangesAction() {
        super("Rollback changes", null, Icons.CODE_EDITOR_RESET);
    }

    public void actionPerformed(AnActionEvent e) {
        final Editor editor = getEditor(e);
        final SourceCodeFile sourceCodeFile = getSourcecodeFile(e);

        if (editor != null && sourceCodeFile != null) {
            boolean reloaded = sourceCodeFile.reloadFromDatabase();

            if (reloaded) {
                new WriteActionRunner() {
                    public void run() {
                        editor.getDocument().setText(sourceCodeFile.getContent());
                        sourceCodeFile.setModified(false);
                    }
                }.start();

                sourceCodeFile.getDatabaseFile().updateDDLFiles(sourceCodeFile.getContentType());
            }
        }
    }

    public void update(AnActionEvent e) {
        SourceCodeFile virtualFile = getSourcecodeFile(e);
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(virtualFile!= null && virtualFile.isModified());
        presentation.setText("Rollback changes");
    }
}
