package com.dci.intellij.dbn.code.common.completion.options.sorting;

import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.common.options.ui.ConfigurationEditorForm;
import com.dci.intellij.dbn.language.common.TokenTypeIdentifier;
import com.dci.intellij.dbn.object.common.DBObjectType;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;

public class CodeCompletionSortingItem extends Configuration {
    private DBObjectType objectType;
    private TokenTypeIdentifier tokenTypeIdentifier = TokenTypeIdentifier.UNKNOWN;

    public DBObjectType getObjectType() {
        return objectType;
    }

    public TokenTypeIdentifier getTokenTypeIdentifier() {
        return tokenTypeIdentifier;
    }

    public String getTokenTypeName() {
        return tokenTypeIdentifier.getName();
    }

    public boolean equals(Object obj) {
        CodeCompletionSortingItem remote = (CodeCompletionSortingItem) obj;
        return
            remote.objectType == objectType &&
            remote.tokenTypeIdentifier == tokenTypeIdentifier;
    }

    public String toString() {
        return objectType == null ? tokenTypeIdentifier.getName() : objectType.getName();
    }

    @Nls
    public String getDisplayName() {
        return null;
    }

    /*********************************************************
     *                      Configuration                    *
     *********************************************************/
    protected ConfigurationEditorForm createConfigurationEditor() {
        return null;
    }

    @Override
    public String getConfigElementName() {
        return "sorting-element";
    }

    public void readConfiguration(Element element) {
        String sortingItemType = element.getAttributeValue("type");
        if (sortingItemType.equals("OBJECT")) {
            String objectTypeName = element.getAttributeValue("id");
            objectType = DBObjectType.getObjectType(objectTypeName);
        } else {
            String tokenTypeName = element.getAttributeValue("id");
            tokenTypeIdentifier = TokenTypeIdentifier.getIdentifier(tokenTypeName);
        }
    }

    public void writeConfiguration(Element element) {
        if (objectType != null) {
            element.setAttribute("type", "OBJECT");
            element.setAttribute("id", objectType.getName());
        } else {
            element.setAttribute("type", "RESERVED_WORD");
            element.setAttribute("id", tokenTypeIdentifier.getName());
        }
    }
}
