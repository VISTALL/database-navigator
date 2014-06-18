package com.dci.intellij.dbn.language.common.element.nesting;

import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.intellij.lang.PsiBuilder;

import java.util.ArrayList;
import java.util.List;

public class NestedRangeMonitor {
    private DBLanguageDialect languageDialect;
    private List<NestedRangeMarker> rangeMarkers = new ArrayList<NestedRangeMarker>();
    private PsiBuilder builder;

    public NestedRangeMonitor(DBLanguageDialect languageDialect, PsiBuilder builder) {
        this.languageDialect = languageDialect;
        this.builder = builder;
    }
}
