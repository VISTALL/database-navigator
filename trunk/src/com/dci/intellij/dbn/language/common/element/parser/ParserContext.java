package com.dci.intellij.dbn.language.common.element.parser;

import com.intellij.lang.PsiBuilder;

public class ParserContext {
    private long timestamp = System.currentTimeMillis();
    private PsiBuilder builder;
    private NestedRangeMonitor rangeMonitor = new NestedRangeMonitor();

    public ParserContext(PsiBuilder builder) {
        this.builder = builder;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public PsiBuilder getBuilder() {
        return builder;
    }

    public NestedRangeMonitor getRangeMonitor() {
        return rangeMonitor;
    }
}
