package com.dci.intellij.dbn.language.common.element;

import com.dci.intellij.dbn.object.common.DBObjectType;

import java.util.List;

public interface QualifiedIdentifierElementType extends DBNElementType {
    List<LeafElementType[]> getVariants();

    TokenElementType getSeparatorToken();

    boolean containsObjectType(DBObjectType objectType);
}
