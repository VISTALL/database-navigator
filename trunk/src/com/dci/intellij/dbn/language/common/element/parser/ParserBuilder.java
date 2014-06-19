package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.TokenType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;

public class ParserBuilder {
    private PsiBuilder builder;
    private NestedRangeMonitor monitor;


    public ParserBuilder(PsiBuilder builder, DBLanguageDialect languageDialect) {
        this.builder = builder;
        this.monitor = new NestedRangeMonitor(builder, languageDialect);
    }

    public void advanceLexer() {
        monitor.compute();
        builder.advanceLexer();
    }

    public PsiBuilder.Marker mark() {
        return builder.mark();
    }

    public String getTokenText() {
        return builder.getTokenText();
    }

    public TokenType getTokenType() {
        return (TokenType) builder.getTokenType();
    }

    public boolean eof() {
        return builder.eof();
    }

    public int getCurrentOffset() {
        return builder.getCurrentOffset();
    }

    public TokenType lookAhead(int steps) {
        return (TokenType) builder.lookAhead(steps);
    }

    public void error(String messageText) {
        builder.error(messageText);
    }

    public ASTNode getTreeBuilt() {
        return builder.getTreeBuilt();
    }

    public void setDebugMode(boolean debugMode) {
        builder.setDebugMode(debugMode);
    }
}
