package com.dci.intellij.dbn.data.editor.color;

import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.SimpleTextAttributes;

import java.awt.Color;

public interface DataGridTextAttributesKeys {
    Color DEFAULT_CARET_ROW_BG_COLOR = new Color(255, 255, 215);
    Color DEFAULT_FOREIGN_REFERENCE_FG_COLOR = new Color(0, 0, 128);
    Color DEFAULT_SELECTION_FG_COLOR = new Color(255, 255, 255);
    Color DEFAULT_SELECTION_BG_COLOR = new Color(82, 109, 165);
    Color DEFAULT_ERROR_BG_COLOR = new Color(255, 240, 240);
    Color DEFAULT_LOADING_BG_COLOR = new Color(245, 245, 245);
    Color DEFAULT_READONLY_FG_COLOR = Color.GRAY;

    TextAttributesKey PLAIN_DATA        = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.PlainData",         HighlighterColors.TEXT.getDefaultAttributes());
    TextAttributesKey MODIFIED_DATA     = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.ModifiedData",      new TextAttributes(null, null, null, null, SimpleTextAttributes.STYLE_BOLD));
    TextAttributesKey DELETED_DATA      = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.DeletedData",       SimpleTextAttributes.GRAYED_ATTRIBUTES.toTextAttributes());
    TextAttributesKey ERROR_DATA        = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.ErrorData",         new TextAttributes(null, DEFAULT_ERROR_BG_COLOR, null, null, SimpleTextAttributes.STYLE_PLAIN));
    TextAttributesKey READONLY_DATA     = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.ReadonlyData",      new TextAttributes(DEFAULT_READONLY_FG_COLOR, null, null, null, SimpleTextAttributes.STYLE_PLAIN));
    TextAttributesKey LOADING_DATA      = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.LoadingData",       new TextAttributes(DEFAULT_READONLY_FG_COLOR, DEFAULT_LOADING_BG_COLOR, null, null, SimpleTextAttributes.STYLE_PLAIN));
    TextAttributesKey FOREIGN_REFERENCE = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.ForeignReference",  new TextAttributes(DEFAULT_FOREIGN_REFERENCE_FG_COLOR, null, null, null, SimpleTextAttributes.STYLE_PLAIN));
    TextAttributesKey CARET_ROW         = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.CaretRow",          new TextAttributes(null, DEFAULT_CARET_ROW_BG_COLOR, null, null, SimpleTextAttributes.STYLE_PLAIN));
    TextAttributesKey SELECTION         = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.Selection",         new TextAttributes(DEFAULT_SELECTION_FG_COLOR, DEFAULT_SELECTION_BG_COLOR, null, null, SimpleTextAttributes.STYLE_PLAIN));
}
