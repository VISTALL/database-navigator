package com.dci.intellij.dbn.code.common.completion;

import com.dci.intellij.dbn.common.util.DocumentUtil;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;


public class CodeCompletionEditorFactoryListener implements EditorFactoryListener {
    // store status of the listener to workaround assertions on add and remove
    private static final Key<String> CC_LISTENER_ADDED = Key.create("CC_LISTENER_ADDED");
    private static final String YES = "YES";
    private static final String NO = "NO";

    public void editorCreated(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();
        Document document = editor.getDocument();
        if (!isListenerAdded(document)) {
            PsiFile file = DocumentUtil.getFile(editor);
            if (file != null && file.getLanguage() instanceof DBLanguage) {
                document.addDocumentListener(CodeCompletionPopupTrigger.INSTANCE);
                markListenerAdded(document);
            }
        }
    }

    public void editorReleased(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();
        Document document = editor.getDocument();

        if (isListenerAdded(document)) {
            PsiFile file = DocumentUtil.getFile(editor);
            if (file != null && file.getLanguage() instanceof DBLanguage) {
                document.removeDocumentListener(CodeCompletionPopupTrigger.INSTANCE);
                markListenerRemoved(document);
            }
        }
    }

    private boolean isListenerAdded(Document document) {
        return YES.equals(document.getUserData(CC_LISTENER_ADDED));
    }

    private void markListenerAdded(Document document) {
        document.putUserData(CC_LISTENER_ADDED, YES);
    }

    private void markListenerRemoved(Document document) {
        document.putUserData(CC_LISTENER_ADDED, NO);
    }

}
