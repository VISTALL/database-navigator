package com.dci.intellij.dbn.code.common.completion;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;

public class BasicInsertHandler implements InsertHandler {
    public static final BasicInsertHandler INSTANCE = new BasicInsertHandler();

    public void handleInsert(InsertionContext insertionContext, LookupElement lookupElement) {
        char completionChar = insertionContext.getCompletionChar();

        if (completionChar == ' ' || completionChar == '\t' || completionChar == '\u0000') {
            Editor editor = insertionContext.getEditor();
            CaretModel caretModel = editor.getCaretModel();
            caretModel.moveCaretRelatively(1, 0, false, false, false);
        }


    }

    protected boolean shouldInsertCharacter(char chr) {
        return chr != '\t' && chr != '\n' && chr!='\u0000';
    }
}
