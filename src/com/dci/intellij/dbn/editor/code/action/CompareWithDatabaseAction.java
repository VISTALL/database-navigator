package com.dci.intellij.dbn.editor.code.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.vfs.SourceCodeFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;

import java.sql.SQLException;

public class CompareWithDatabaseAction extends AbstractDiffAction {
    public CompareWithDatabaseAction() {
        super("Compare with database", null, Icons.CODE_EDITOR_DIFF_DB);
    }

    public void actionPerformed(AnActionEvent e) {
        Editor editor = getEditor(e);
        if (editor == null) {
            e.getPresentation().setEnabled(false);
        } else {
            SourceCodeFile virtualFile = getSourcecodeFile(editor);
            String content = editor.getDocument().getText();
            virtualFile.setContent(content);
            DBSchemaObject object = virtualFile.getObject();
            try {
                String referenceText = object.loadCodeFromDatabase(virtualFile.getContentType());
                openDiffWindow(e, referenceText, "Database version", "Local version vs. database version");
            } catch (SQLException e1) {
                MessageUtil.showErrorDialog(
                        "Could not load sourcecode for " +
                        object.getQualifiedNameWithType() + " from database.", e1);
            }


        }
    }

    public void update(AnActionEvent e) {
        Editor editor = getEditor(e);
        e.getPresentation().setText("Compare with database");
        e.getPresentation().setEnabled(editor != null);
    }
}
