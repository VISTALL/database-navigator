package com.dci.intellij.dbn.editor.code.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.thread.WriteActionRunner;
import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.vfs.SourceCodeFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;

import java.sql.SQLException;

public class RollbackChangesAction extends AbstractSourceCodeEditorAction {
    public RollbackChangesAction() {
        super("Rollback changes", null, Icons.CODE_EDITOR_RESET);
    }

    public void actionPerformed(AnActionEvent e) {
        final Editor editor = getEditor(e);
        final SourceCodeFile contentFile = getSourcecodeFile(editor);
        try {
            contentFile.reloadFromDatabase();

            new WriteActionRunner() {
                public void run() {
                    editor.getDocument().setText(contentFile.getContent());
                    contentFile.setModified(false);
                }
            }.start();

            contentFile.getDatabaseFile().updateDDLFiles(contentFile.getContentType());
        } catch (SQLException ex) {
            MessageUtil.showErrorDialog("Could not reload source code for " + contentFile.getObject().getQualifiedNameWithType() + " from database.", ex);
        }
    }

    public void update(AnActionEvent e) {
        SourceCodeFile virtualFile = getSourcecodeFile(e);
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(virtualFile!= null && virtualFile.isModified());
        presentation.setText("Rollback changes");
    }
}
