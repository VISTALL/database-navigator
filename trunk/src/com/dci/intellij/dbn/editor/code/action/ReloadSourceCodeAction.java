package com.dci.intellij.dbn.editor.code.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.thread.WriteActionRunner;
import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.vfs.SourceCodeFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;

import java.sql.SQLException;

public class ReloadSourceCodeAction extends AbstractSourceCodeEditorAction {
    public ReloadSourceCodeAction() {
        super("", null, Icons.CODE_EDITOR_RELOAD);
    }

    public void actionPerformed(AnActionEvent e) {
        final Editor editor = getEditor(e);
        final SourceCodeFile virtualFile = getSourcecodeFile(editor);
        try {
            virtualFile.reloadFromDatabase();
            new WriteActionRunner() {
                public void run() {
                    editor.getDocument().setText(virtualFile.getContent());
                    virtualFile.setModified(false);
                }
            }.start();

        } catch (SQLException ex) {
            MessageUtil.showErrorDialog("Could not reload source code for " + virtualFile.getObject().getQualifiedNameWithType() + " from database.", ex);
        }
    }

    public void update(AnActionEvent e) {
        SourceCodeFile virtualFile = getSourcecodeFile(e);
        Presentation presentation = e.getPresentation();
        if (virtualFile == null) {
            presentation.setEnabled(false);
        } else {
            String text =
                virtualFile.getContentType() == DBContentType.CODE_SPEC ? "Reload spec" :
                virtualFile.getContentType() == DBContentType.CODE_BODY ? "Reload body" : "Reload";

            presentation.setText(text);
            presentation.setEnabled(!virtualFile.isModified());
        }
    }
}