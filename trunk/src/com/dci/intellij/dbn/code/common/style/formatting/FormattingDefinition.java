package com.dci.intellij.dbn.code.common.style.formatting;

import com.dci.intellij.dbn.common.util.CommonUtil;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import org.jdom.Element;

public class FormattingDefinition {
    public static final FormattingDefinition LINE_BREAK_BEFORE = new FormattingDefinition(null, null, FormattingSpacing.MIN_LINE_BREAK, null);
    public static final FormattingDefinition LINE_BREAK_AFTER = new FormattingDefinition(null, null, null, FormattingSpacing.MIN_LINE_BREAK);

    public static final FormattingDefinition ONE_SPACE_BEFORE = new FormattingDefinition(null, null, FormattingSpacing.ONE_SPACE, null);
    public static final FormattingDefinition NO_SPACE_BEFORE = new FormattingDefinition(null, null, FormattingSpacing.NO_SPACE, null);

    private FormattingWrap formattingWrap;
    private FormattingIndent formattingIndent;
    private FormattingSpacing formattingSpacingBefore;
    private FormattingSpacing formattingSpacingAfter;
    private FormattingAttributes attributes;

    public FormattingDefinition(){
    }

    public FormattingDefinition(FormattingWrap formattingWrap, FormattingIndent formattingIndent, FormattingSpacing formattingSpacingBefore, FormattingSpacing formattingSpacingAfter) {
        this.formattingWrap = formattingWrap;
        this.formattingIndent = formattingIndent;
        this.formattingSpacingBefore = formattingSpacingBefore;
        this.formattingSpacingAfter = formattingSpacingAfter;
    }

    protected FormattingDefinition(FormattingDefinition attributes) {
        formattingIndent = attributes.formattingIndent;
        formattingWrap = attributes.formattingWrap;
        formattingSpacingBefore = attributes.formattingSpacingBefore;
        formattingSpacingAfter = attributes.formattingSpacingAfter;
    }

    protected FormattingDefinition(Element def) {
        formattingIndent = FormattingIndent.get(def);
        formattingWrap = FormattingWrap.get(def);
        formattingSpacingBefore = FormattingSpacing.get(def, true);
        formattingSpacingAfter = FormattingSpacing.get(def, false);
    }

    public void merge(FormattingDefinition defaults) {
        formattingWrap = CommonUtil.nvln(formattingWrap, defaults.formattingWrap);
        formattingIndent = CommonUtil.nvln(formattingIndent, defaults.formattingIndent);
        formattingSpacingBefore = CommonUtil.nvln(formattingSpacingBefore, defaults.formattingSpacingBefore);
        formattingSpacingAfter = CommonUtil.nvln(formattingSpacingAfter, defaults.formattingSpacingAfter);
    }
    
    public FormattingAttributes getAttributes() {
        if (attributes == null) {
            Wrap wrap = formattingWrap == null ? null : formattingWrap.getValue();
            Indent indent = formattingIndent == null ? null : formattingIndent.getValue();
            Spacing spacingBefore = formattingSpacingBefore == null ? null : formattingSpacingBefore.getValue();
            Spacing spacingAfter = formattingSpacingAfter == null ? null : formattingSpacingAfter.getValue();
            attributes = new FormattingAttributes(wrap, indent, spacingBefore, spacingAfter);
        }
        return attributes;
    }

    public boolean isEmpty() {
        return  formattingWrap == null &&
                formattingIndent == null &&
                formattingSpacingBefore == null &&
                formattingSpacingAfter == null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("");
        if (formattingWrap != null) result.append("wrap=").append(formattingWrap).append(" ");
        if (formattingIndent != null) result.append("indent=").append(formattingIndent).append(" ");
        if (formattingSpacingBefore != null) result.append("spacingBefore=").append(formattingSpacingBefore).append(" ");
        if (formattingSpacingAfter != null) result.append("spacingAfter=").append(formattingSpacingAfter).append(" ");

        return result.toString();
    }
}

