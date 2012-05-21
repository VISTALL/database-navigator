package com.dci.intellij.dbn.language.common.element;

import java.util.List;

public interface QualifiedIdentifierElementType extends ElementType {
    List<LeafElementType[]> getVariants();

    TokenElementType getSeparatorToken();
}
