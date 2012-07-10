package com.dci.intellij.dbn.data.editor.color;

import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

public interface DataEditorTextAttributesKeys {
    TextAttributesKey LINE_COMMENT       = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.LineComment",       SyntaxHighlighterColors.LINE_COMMENT.getDefaultAttributes());
    TextAttributesKey BLOCK_COMMENT      = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.BlockComment",      SyntaxHighlighterColors.DOC_COMMENT.getDefaultAttributes());
    TextAttributesKey STRING             = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.String",            SyntaxHighlighterColors.STRING.getDefaultAttributes());
    TextAttributesKey NUMBER             = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.Number",            SyntaxHighlighterColors.NUMBER.getDefaultAttributes());
    TextAttributesKey DATA_TYPE          = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.DataType",          SyntaxHighlighterColors.KEYWORD.getDefaultAttributes());
    TextAttributesKey ALIAS              = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.Alias",             HighlighterColors.TEXT.getDefaultAttributes());
    TextAttributesKey IDENTIFIER         = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.Identifier",        HighlighterColors.TEXT.getDefaultAttributes());
    TextAttributesKey QUOTED_IDENTIFIER  = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.QuotedIdentifier",  HighlighterColors.TEXT.getDefaultAttributes());
    TextAttributesKey KEYWORD            = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.Keyword",           SyntaxHighlighterColors.KEYWORD.getDefaultAttributes());
    TextAttributesKey FUNCTION           = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.Function",          SyntaxHighlighterColors.KEYWORD.getDefaultAttributes());
    TextAttributesKey PARAMETER          = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.Parameter",         SyntaxHighlighterColors.KEYWORD.getDefaultAttributes());
    TextAttributesKey EXCEPTION          = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.Exception",         SyntaxHighlighterColors.KEYWORD.getDefaultAttributes());
    TextAttributesKey OPERATOR           = TextAttributesKey.createTextAttributesKey("DBNavigator.TextAttributes.DataEditor.Operator",          SyntaxHighlighterColors.OPERATION_SIGN.getDefaultAttributes());
}
