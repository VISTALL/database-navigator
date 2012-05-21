package com.dci.intellij.dbn.editor.code.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.compatibility.CompatibilityUtil;
import com.dci.intellij.dbn.common.thread.WriteActionRunner;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.editor.code.SourceCodeEditorManager;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.common.status.DBObjectStatus;
import com.dci.intellij.dbn.vfs.SourceCodeFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;

public class SaveChangesAction extends AbstractSourceCodeEditorAction {
    public SaveChangesAction() {
        super("", null, Icons.CODE_EDITOR_SAVE);
    }

    public void actionPerformed(AnActionEvent e) {
        final Editor editor = getEditor(e);

        new WriteActionRunner() {
            public void run() {
                Document document = editor.getDocument();
                CompatibilityUtil.stripDocumentTrailingSpaces(document);
                SourceCodeFile virtualFile = getSourcecodeFile(editor);
                SourceCodeEditorManager.getInstance(virtualFile.getProject()).updateSourceToDatabase(editor, virtualFile);
            }
        }.start();
    }

    public void update(AnActionEvent e) {
        SourceCodeFile virtualFile = getSourcecodeFile(e);
        Presentation presentation = e.getPresentation();
        if (virtualFile == null) {
            presentation.setEnabled(false);
        } else {
            String text =
                    virtualFile.getContentType() == DBContentType.CODE_SPEC ? "Save spec" :
                            virtualFile.getContentType() == DBContentType.CODE_BODY ? "Save body" : "Save";

            DBSchemaObject object = virtualFile.getObject();
            boolean isSaving = object.getStatus().is(DBObjectStatus.SAVING);
            presentation.setEnabled(!isSaving && virtualFile.isModified());
            presentation.setText(text);
        }
    }
}
