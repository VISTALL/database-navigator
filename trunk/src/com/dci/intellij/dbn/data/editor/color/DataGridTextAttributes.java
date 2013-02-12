package com.dci.intellij.dbn.data.editor.color;

import com.dci.intellij.dbn.common.util.TextAttributesUtil;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.ui.SimpleTextAttributes;

import java.awt.Color;

public class DataGridTextAttributes {
    private SimpleTextAttributes plainData;
    private SimpleTextAttributes modifiedData;
    private SimpleTextAttributes deletedData;
    private SimpleTextAttributes errorData;
    private SimpleTextAttributes readonlyData;
    private SimpleTextAttributes loadingData;
    private SimpleTextAttributes primaryKey;
    private SimpleTextAttributes foreignKey;
    private SimpleTextAttributes primaryKeyAtCaretRow;
    private SimpleTextAttributes foreignKeyAtCaretRow;
    private SimpleTextAttributes selection;
    private SimpleTextAttributes searchResult;

    private Color caretRowBgColor;

    public DataGridTextAttributes() {
        load();
    }

    public void load() {
        EditorColorsScheme globalScheme = EditorColorsManager.getInstance().getGlobalScheme();
        caretRowBgColor = globalScheme.getColor(DataGridTextAttributesKeys.CARET_ROW_BACKGROUND);

        plainData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.PLAIN_DATA);
        modifiedData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.MODIFIED_DATA);
        deletedData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.DELETED_DATA);
        errorData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.ERROR_DATA);
        readonlyData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.READONLY_DATA);
        loadingData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.LOADING_DATA);
        primaryKey= TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.PRIMARY_KEY);
        foreignKey = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.FOREIGN_KEY);
        primaryKeyAtCaretRow = new SimpleTextAttributes(caretRowBgColor, primaryKey.getFgColor(), null, primaryKey.getStyle());
        foreignKeyAtCaretRow = new SimpleTextAttributes(caretRowBgColor, foreignKey.getFgColor(), null, foreignKey.getStyle());
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

    public SimpleTextAttributes getPrimaryKey() {
        return primaryKey;
    }

    public SimpleTextAttributes getForeignKey() {
        return foreignKey;
    }

    public SimpleTextAttributes getPrimaryKeyAtCaretRow() {
        return primaryKeyAtCaretRow;
    }

    public SimpleTextAttributes getForeignKeyAtCaretRow() {
        return foreignKeyAtCaretRow;
    }

//    public SimpleTextAttributes getCaretRow() {
//        return caretRow;
//    }

    public SimpleTextAttributes getSelection() {
        return selection;
    }

    public SimpleTextAttributes getSearchResult() {
        return searchResult;
    }

    public Color getCaretRowBgColor() {
        return caretRowBgColor;
    }
}
