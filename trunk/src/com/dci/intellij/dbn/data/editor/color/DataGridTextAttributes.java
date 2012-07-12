package com.dci.intellij.dbn.data.editor.color;

import com.dci.intellij.dbn.common.util.TextAttributesUtil;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.ui.SimpleTextAttributes;

public class DataGridTextAttributes {
    private SimpleTextAttributes plainData;
    private SimpleTextAttributes modifiedData;
    private SimpleTextAttributes deletedData;
    private SimpleTextAttributes errorData;
    private SimpleTextAttributes readonlyData;
    private SimpleTextAttributes loadingData;
    private SimpleTextAttributes foreignReference;
    private SimpleTextAttributes caretRow;
    private SimpleTextAttributes selection;
    private SimpleTextAttributes searchResult;

    public DataGridTextAttributes() {
        load();
    }

    public void load() {
        plainData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.PLAIN_DATA);
        modifiedData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.MODIFIED_DATA);
        deletedData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.DELETED_DATA);
        errorData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.ERROR_DATA);
        readonlyData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.READONLY_DATA);
        loadingData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.LOADING_DATA);
        foreignReference = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.FOREIGN_REFERENCE);
        caretRow = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.CARET_ROW);
        selection = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.SELECTION);
        searchResult = TextAttributesUtil.getSimpleTextAttributes(EditorColors.TEXT_SEARCH_RESULT_ATTRIBUTES);
    }

    public SimpleTextAttributes getPlainData() {
        return plainData;
    }

    public SimpleTextAttributes getModifiedData() {
        return modifiedData;
    }

    public SimpleTextAttributes getDeletedData() {
        return deletedData;
    }

    public SimpleTextAttributes getErrorData() {
        return errorData;
    }

    public SimpleTextAttributes getReadonlyData() {
        return readonlyData;
    }

    public SimpleTextAttributes getLoadingData() {
        return loadingData;
    }

    public SimpleTextAttributes getForeignReference() {
        return foreignReference;
    }

    public SimpleTextAttributes getCaretRow() {
        return caretRow;
    }

    public SimpleTextAttributes getSelection() {
        return selection;
    }

    public SimpleTextAttributes getSearchResult() {
        return searchResult;
    }
}
