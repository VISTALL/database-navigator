package com.dci.intellij.dbn.language.common.element;

public interface WrapperElementType extends DBNElementType {

    TokenElementType getBeginTokenElement();

    TokenElementType getEndTokenElement();

    DBNElementType getWrappedElement();

    boolean isWrappingOptional();
}
