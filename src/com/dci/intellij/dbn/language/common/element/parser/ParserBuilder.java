package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.TokenType;
import com.dci.intellij.dbn.language.common.element.ElementType;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

public class ParserBuilder {
    private PsiBuilder builder;
    private NestedRangeMonitor monitor;


    public ParserBuilder(PsiBuilder builder, DBLanguageDialect languageDialect) {
        this.builder = builder;
        this.monitor = new NestedRangeMonitor(builder, languageDialect);
    }

    public void advanceLexer() {
        advanceLexer(null);
    }
    public void advanceLexer(ParsePathNode parentNode) {
        advanceLexer(parentNode, false);
    }

    public void advanceLexer(ParsePathNode parentNode, boolean mark) {
        monitor.compute(parentNode, mark);
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

    public void markerRollbackTo(PsiBuilder.Marker marker) {
        if (marker != null) {
            marker.rollbackTo();
        }
    }

    public void markerDone(PsiBuilder.Marker marker, ElementType elementType) {
        if (marker != null) {
            marker.done((IElementType) elementType);
        }
    }

    public void markerDrop(PsiBuilder.Marker marker) {
        if (marker != null) {
            marker.drop();
        }
    }
}
