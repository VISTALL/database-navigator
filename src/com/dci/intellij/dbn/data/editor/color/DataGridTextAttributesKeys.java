package com.dci.intellij.dbn.data.editor.color;

import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.ColorKey;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.SimpleTextAttributes;

import java.awt.Color;

public interface DataGridTextAttributesKeys {
    Color DEFAULT_SELECTION_FG_COLOR = new Color(255, 255, 255);
    Color DEFAULT_SELECTION_BG_COLOR = new Color(82, 109, 165);
    Color DEFAULT_ERROR_BG_COLOR = new Color(255, 240, 240);
    Color DEFAULT_LOADING_BG_COLOR = new Color(245, 245, 245);
    Color DEFAULT_READONLY_FG_COLOR = Color.GRAY;


    Color DEFAULT_CARET_ROW_BACKGROUND_COLOR = EditorColors.CARET_ROW_COLOR.getDefaultColor();

    //TextAttributes DEFAULT_TEXT_ATTRIBUTES = new TextAttributes(HighlighterColors.TEXT.getDefaultAttributes().getForegroundColor(), UIUtil.getTableBackground(), null, null, SimpleTextAttributes.STYLE_PLAIN);
    TextAttributes DEFAULT_TEXT_ATTRIBUTES = new TextAttributes(HighlighterColors.TEXT.getDefaultAttributes().getForegroundColor(), HighlighterColors.TEXT.getDefaultAttributes().getBackgroundColor(), null, null, SimpleTextAttributes.STYLE_PLAIN);
    TextAttributes DEFAULT_DELETED_TEXT_ATTRIBUTES = EditorColors.DELETED_TEXT_ATTRIBUTES.getDefaultAttributes();
    TextAttributes DEFAULT_PK_ROW_TEXT_ATTRIBUTES = new TextAttributes(
            SyntaxHighlighterColors.NUMBER.getDefaultAttributes().getForegroundColor(),
            HighlighterColors.TEXT.getDefaultAttributes().getBackgroundColor(), null, null, SimpleTextAttributes.STYLE_PLAIN);
    TextAttributes DEFAULT_FK_ROW_TEXT_ATTRIBUTES = new TextAttributes(
            CodeInsightColors.INSTANCE_FIELD_ATTRIBUTES.getDefaultAttributes().getForegroundColor(),
            HighlighterColors.TEXT.getDefaultAttributes().getBackgroundColor(), null, null, SimpleTextAttributes.STYLE_PLAIN);

    ColorKey CARET_ROW_BACKGROUND = ColorKey.createColorKey("DBNavigator.TextAttributes.DataEditor.CaretRowBackground", DEFAULT_CARET_ROW_BACKGROUND_COLOR);

    TextAttributesKey PLAIN_DATA     = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.PlainData",    DEFAULT_TEXT_ATTRIBUTES);
    TextAttributesKey MODIFIED_DATA  = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.ModifiedData", new TextAttributes(null, null, null, null, SimpleTextAttributes.STYLE_BOLD));
    TextAttributesKey DELETED_DATA   = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.DeletedData",  DEFAULT_DELETED_TEXT_ATTRIBUTES);
    TextAttributesKey ERROR_DATA     = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.ErrorData",    new TextAttributes(null, DEFAULT_ERROR_BG_COLOR, null, null, SimpleTextAttributes.STYLE_PLAIN));
    TextAttributesKey READONLY_DATA  = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.ReadonlyData", new TextAttributes(DEFAULT_READONLY_FG_COLOR, null, null, null, SimpleTextAttributes.STYLE_PLAIN));
    TextAttributesKey LOADING_DATA   = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.LoadingData",  new TextAttributes(DEFAULT_READONLY_FG_COLOR, DEFAULT_LOADING_BG_COLOR, null, null, SimpleTextAttributes.STYLE_PLAIN));
    TextAttributesKey PRIMARY_KEY    = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.PrimaryKey",   DEFAULT_PK_ROW_TEXT_ATTRIBUTES);
    TextAttributesKey FOREIGN_KEY    = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.ForeignKey",   DEFAULT_FK_ROW_TEXT_ATTRIBUTES);
    TextAttributesKey SELECTION      = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.Selection",    new TextAttributes(DEFAULT_SELECTION_FG_COLOR, DEFAULT_SELECTION_BG_COLOR, null, null, SimpleTextAttributes.STYLE_PLAIN));
}
