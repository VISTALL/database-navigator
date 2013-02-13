package com.dci.intellij.dbn.data.editor.color;

import com.intellij.execution.process.ConsoleHighlighter;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.ColorKey;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.SimpleTextAttributes;

import java.awt.Color;

public interface DataGridTextAttributesKeys {
    Color DEFAULT_SELECTION_FG_COLOR = new Color(255, 255, 255);
    Color DEFAULT_SELECTION_BG_COLOR = new Color(82, 109, 165);


    interface Colors {
        Color DEFAULT_BACKGROUND   = HighlighterColors.TEXT.getDefaultAttributes().getBackgroundColor();
        Color DEFAULT_FOREGROUND   = HighlighterColors.TEXT.getDefaultAttributes().getForegroundColor();
        Color LIGHT_BACKGROUND     = CodeInsightColors.FOLLOWED_HYPERLINK_ATTRIBUTES.getDefaultAttributes().getBackgroundColor();
        Color LIGHT_FOREGROUND     = SyntaxHighlighterColors.JAVA_BLOCK_COMMENT.getDefaultAttributes().getForegroundColor();
        Color ERROR_BACKGROUND     = HighlighterColors.BAD_CHARACTER.getDefaultAttributes().getBackgroundColor();
        Color PK_FOREGROUND        = CodeInsightColors.INSTANCE_FIELD_ATTRIBUTES.getDefaultAttributes().getForegroundColor();
        Color FK_FOREGROUND        = ConsoleHighlighter.GREEN.getDefaultAttributes().getForegroundColor();
        Color CARET_ROW_BACKGROUND = EditorColorsManager.getInstance().getGlobalScheme().getColor(EditorColors.CARET_ROW_COLOR);
    }

    interface Attributes {
        TextAttributes DEFAULT     = new TextAttributes(Colors.DEFAULT_FOREGROUND, Colors.DEFAULT_BACKGROUND, null, null, SimpleTextAttributes.STYLE_PLAIN);
        TextAttributes DELETED     = new TextAttributes(Colors.LIGHT_FOREGROUND, Colors.LIGHT_BACKGROUND, null, null, SimpleTextAttributes.STYLE_PLAIN);
        TextAttributes READONLY    = new TextAttributes(Colors.LIGHT_FOREGROUND, Colors.LIGHT_BACKGROUND, null, null, SimpleTextAttributes.STYLE_PLAIN);
        TextAttributes ERROR       = new TextAttributes(Colors.DEFAULT_FOREGROUND, Colors.ERROR_BACKGROUND, null, null, SimpleTextAttributes.STYLE_PLAIN);
        TextAttributes PRIMARY_KEY = new TextAttributes(Colors.PK_FOREGROUND,Colors.DEFAULT_BACKGROUND, null, null, SimpleTextAttributes.STYLE_PLAIN);
        TextAttributes FOREIGN_KEY = new TextAttributes(Colors.FK_FOREGROUND, Colors.DEFAULT_BACKGROUND, null, null, SimpleTextAttributes.STYLE_PLAIN);
    }

    ColorKey CARET_ROW_BACKGROUND = ColorKey.createColorKey("DBNavigator.TextAttributes.DataEditor.CaretRowBackground", Colors.CARET_ROW_BACKGROUND);

    TextAttributesKey PLAIN_DATA     = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.PlainData",    Attributes.DEFAULT);
    TextAttributesKey MODIFIED_DATA  = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.ModifiedData", new TextAttributes(null, null, null, null, SimpleTextAttributes.STYLE_BOLD));
    TextAttributesKey DELETED_DATA   = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.DeletedData",  Attributes.DELETED);
    TextAttributesKey ERROR_DATA     = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.ErrorData",    Attributes.ERROR);
    TextAttributesKey READONLY_DATA  = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.ReadonlyData", Attributes.READONLY);
    TextAttributesKey LOADING_DATA   = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.LoadingData",  Attributes.READONLY);
    TextAttributesKey PRIMARY_KEY    = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.PrimaryKey",   Attributes.PRIMARY_KEY);
    TextAttributesKey FOREIGN_KEY    = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.ForeignKey",   Attributes.FOREIGN_KEY);
    TextAttributesKey SELECTION      = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.Selection",    new TextAttributes(DEFAULT_SELECTION_FG_COLOR, DEFAULT_SELECTION_BG_COLOR, null, null, SimpleTextAttributes.STYLE_PLAIN));
}
