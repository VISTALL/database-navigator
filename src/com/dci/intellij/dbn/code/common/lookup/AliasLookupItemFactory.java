package com.dci.intellij.dbn.code.common.lookup;

import com.dci.intellij.dbn.code.common.completion.CodeCompletionContext;
import com.dci.intellij.dbn.code.common.completion.CodeCompletionLookupConsumer;

import javax.swing.Icon;

public class AliasLookupItemFactory extends LookupItemFactory {
    private String text;
    private boolean isDefinition;

    public AliasLookupItemFactory(String text, boolean isDefinition) {
        this.text = text;
        this.isDefinition = isDefinition;
    }

    public String getTextHint() {
        return isDefinition ? "alias def" : "alias ref";
    }

    @Override
    public DBLookupItem createLookupItem(Object source, CodeCompletionLookupConsumer consumer) {
        return super.createLookupItem(source, consumer);
    }

    public boolean isBold() {
        return false;
    }

    @Override
    public String getText(CodeCompletionContext completionContext) {
        return text;
    }

    public Icon getIcon() {
        return null;
    }
}