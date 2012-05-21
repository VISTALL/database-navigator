package com.dci.intellij.dbn.data.find;

import com.dci.intellij.dbn.common.ui.table.BasicTable;

public interface SearchableDataComponent {
    void showSearchHeader();
    void hideSearchHeader();
    void cancelEditActions();
    String getSelectedText();
    BasicTable getTable();
}
