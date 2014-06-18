package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.element.nesting.MatchingPairsMonitor;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.intellij.lang.PsiBuilder;

public class ParserContext {
    private long timestamp = System.currentTimeMillis();
    private ParserBuilder builder;
    private MatchingPairsMonitor nesting;

    public ParserContext(DBLanguageDialect languageDialect, PsiBuilder builder) {
        this.nesting = new MatchingPairsMonitor(languageDialect, builder);
        this.builder = new ParserBuilder(builder);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ParserBuilder getBuilder() {
        return builder;
    }

    public MatchingPairsMonitor getNesting() {
        return nesting;
    }

    public void computeTokenPairs(ParsePathNode node) {
        nesting.compute(node);
    }
}
