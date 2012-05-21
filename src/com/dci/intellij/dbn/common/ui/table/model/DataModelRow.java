package com.dci.intellij.dbn.common.ui.table.model;

import com.intellij.openapi.Disposable;

import java.util.List;

public interface DataModelRow<T extends DataModelCell> extends Disposable {
    List<T> getCells();

    T getCellAtIndex(int index);

    int getIndex();

    void setIndex(int index);

    DataModel getModel();
}
