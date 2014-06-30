package com.dci.intellij.dbn.data.sorting;

import org.jdom.Element;

public interface SortingState {
    void readState(Element element);

    void writeState(Element element);

    SortingInstruction getSortingInstruction(String columnName);

    boolean isValid();
}
