package com.dci.intellij.dbn.data.editor.color;

import com.dci.intellij.dbn.common.Icons;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataEditorColorSettingsPage implements ColorSettingsPage {
    protected final List<AttributesDescriptor> attributeDescriptors = new ArrayList<AttributesDescriptor>();

    public DataEditorColorSettingsPage() {
        attributeDescriptors.add(new AttributesDescriptor("Line Comment", DataEditorTextAttributesKeys.LINE_COMMENT));
        attributeDescriptors.add(new AttributesDescriptor("Block Comment", DataEditorTextAttributesKeys.BLOCK_COMMENT));
        attributeDescriptors.add(new AttributesDescriptor("String Literal", DataEditorTextAttributesKeys.STRING));
        attributeDescriptors.add(new AttributesDescriptor("Numeric Literal", DataEditorTextAttributesKeys.NUMBER));
        attributeDescriptors.add(new AttributesDescriptor("Alias", DataEditorTextAttributesKeys.ALIAS));
        attributeDescriptors.add(new AttributesDescriptor("Identifier", DataEditorTextAttributesKeys.IDENTIFIER));
        attributeDescriptors.add(new AttributesDescriptor("Quoted Identifier", DataEditorTextAttributesKeys.QUOTED_IDENTIFIER));
        attributeDescriptors.add(new AttributesDescriptor("Keyword", DataEditorTextAttributesKeys.KEYWORD));
        attributeDescriptors.add(new AttributesDescriptor("Function", DataEditorTextAttributesKeys.FUNCTION));
        attributeDescriptors.add(new AttributesDescriptor("DataType", DataEditorTextAttributesKeys.DATA_TYPE));
        attributeDescriptors.add(new AttributesDescriptor("Exception", DataEditorTextAttributesKeys.EXCEPTION));
        attributeDescriptors.add(new AttributesDescriptor("Operator", DataEditorTextAttributesKeys.OPERATOR));
    }

    @Override
    public Icon getIcon() {
        return Icons.FILE_SQL;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new PlainSyntaxHighlighter();
    }

    @NonNls
    @NotNull
    public final String getDemoText() {
        return " ";
    }

    @NotNull
    public AttributesDescriptor[] getAttributeDescriptors() {
        return attributeDescriptors.toArray(new AttributesDescriptor[attributeDescriptors.size()]);
    }

    @NotNull
    public ColorDescriptor[] getColorDescriptors() {
        return new ColorDescriptor[0];
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Dataset Editor (DBN)";
    }

    @Nullable
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }
}
