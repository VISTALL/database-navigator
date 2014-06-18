package com.dci.intellij.dbn.language.common.element;

public interface OneOfElementType extends DBNElementType {
    void sort();

    DBNElementType[] getPossibleElementTypes();

    void warnAmbiguousBranches();
}
