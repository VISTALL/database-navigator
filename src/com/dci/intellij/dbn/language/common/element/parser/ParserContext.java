package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.intellij.lang.PsiBuilder;

public class ParserContext {
    private long timestamp = System.currentTimeMillis();
    private PsiBuilder builder;
    private NestedRangeMonitor nesting;

    public ParserContext(DBLanguageDialect languageDialect, PsiBuilder builder) {
        this.nesting = new NestedRangeMonitor(languageDialect, builder);
        this.builder = builder;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public PsiBuilder getBuilder() {
        return builder;
    }

    public NestedRangeMonitor getNesting() {
        return nesting;
    }
}
