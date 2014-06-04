package com.dci.intellij.dbn.data.editor.color;

import com.dci.intellij.dbn.common.util.TextAttributesUtil;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.ui.SimpleTextAttributes;

import java.awt.Color;

public class DataGridTextAttributes {
    private SimpleTextAttributes plainData;
    private SimpleTextAttributes plainDataAtCaretRow;
    private SimpleTextAttributes modifiedData;
    private SimpleTextAttributes modifiedDataAtCaretRow;
    private SimpleTextAttributes deletedData;
    private SimpleTextAttributes errorData;
    private SimpleTextAttributes readonlyData;
    private SimpleTextAttributes readonlyDataAtCaretRow;
    private SimpleTextAttributes loadingData;
    private SimpleTextAttributes loadingDataAtCaretRow;
    private SimpleTextAttributes primaryKey;
    private SimpleTextAttributes primaryKeyModified;
    private SimpleTextAttributes primaryKeyAtCaretRow;
    private SimpleTextAttributes primaryKeyAtCaretRowModified;
    private SimpleTextAttributes foreignKey;
    private SimpleTextAttributes foreignKeyModified;
    private SimpleTextAttributes foreignKeyAtCaretRow;
    private SimpleTextAttributes foreignKeyAtCaretRowModified;
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
        plainDataAtCaretRow = new SimpleTextAttributes(caretRowBgColor, plainData.getFgColor(), null, plainData.getFontStyle());
        modifiedData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.MODIFIED_DATA);
        modifiedDataAtCaretRow = new SimpleTextAttributes(caretRowBgColor, modifiedData.getFgColor(), null, modifiedData.getFontStyle());
        deletedData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.DELETED_DATA);
        errorData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.ERROR_DATA);
        readonlyData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.READONLY_DATA);
        readonlyDataAtCaretRow = new SimpleTextAttributes(caretRowBgColor, readonlyData.getFgColor(), null, readonlyData.getFontStyle());
        loadingData = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.LOADING_DATA);
        loadingDataAtCaretRow = new SimpleTextAttributes(caretRowBgColor, loadingData.getFgColor(), null, loadingData.getFontStyle());
        primaryKey= TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.PRIMARY_KEY);
        primaryKeyModified = new SimpleTextAttributes(primaryKey.getBgColor(), primaryKey.getFgColor(), null, modifiedData.getStyle());
        primaryKeyAtCaretRow = new SimpleTextAttributes(caretRowBgColor, primaryKey.getFgColor(), null, primaryKey.getStyle());
        primaryKeyAtCaretRowModified = new SimpleTextAttributes(caretRowBgColor, primaryKey.getFgColor(), null, modifiedData.getStyle());
        foreignKey = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.FOREIGN_KEY);
        foreignKeyModified = new SimpleTextAttributes(foreignKey.getBgColor(), foreignKey.getFgColor(), null, modifiedData.getStyle());
        foreignKeyAtCaretRow = new SimpleTextAttributes(caretRowBgColor, foreignKey.getFgColor(), null, foreignKey.getStyle());
        foreignKeyAtCaretRowModified = new SimpleTextAttributes(caretRowBgColor, foreignKey.getFgColor(), null, modifiedData.getStyle());
        selection = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.SELECTION);
        searchResult = TextAttributesUtil.getSimpleTextAttributes(EditorColors.TEXT_SEARCH_RESULT_ATTRIBUTES);
    }

    public SimpleTextAttributes getPlainData(boolean atCaretRow) {
        return atCaretRow ? plainDataAtCaretRow : plainData;
    }

    public SimpleTextAttributes getModifiedData(boolean atCaretRow) {
        return atCaretRow ? modifiedDataAtCaretRow : modifiedData;
    }

    public SimpleTextAttributes getDeletedData() {
        return deletedData;
    }

    public SimpleTextAttributes getErrorData() {
        return errorData;
    }

    public SimpleTextAttributes getReadonlyData(boolean atCaretRow) {
        return atCaretRow ? readonlyDataAtCaretRow : readonlyData;
    }

    public SimpleTextAttributes getLoadingData(boolean atCaretRow) {
        return atCaretRow ? loadingDataAtCaretRow : loadingData;
    }

    public SimpleTextAttributes getPrimaryKey(boolean modified, boolean atCaretRow) {
        return modified && atCaretRow ? primaryKeyAtCaretRowModified :
                atCaretRow ? primaryKeyAtCaretRow :
                        modified ? primaryKeyModified : primaryKey;
    }

    public SimpleTextAttributes getForeignKey(boolean modified, boolean atCaretRow) {
        return modified && atCaretRow ? foreignKeyAtCaretRowModified :
                atCaretRow ? foreignKeyAtCaretRow :
                        modified ? foreignKeyModified : foreignKey;
    }

    public SimpleTextAttributes getPrimaryKeyAtCaretRow() {
        return primaryKeyAtCaretRow;
    }

    public SimpleTextAttributes getPrimaryKeyModified() {
        return primaryKeyModified;
    }

    public SimpleTextAttributes getPrimaryKeyAtCaretRowModified() {
        return primaryKeyAtCaretRowModified;
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
