package com.dci.intellij.dbn.language.common.element.impl;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;

public class NestedRangeElementType extends IElementType {
    public NestedRangeElementType(Language language) {
        super("nested-range", language, false);
    }
}
