package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.TokenType;
import com.dci.intellij.dbn.language.common.element.ElementType;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParserBuilder {
    private PsiBuilder builder;
    private NestedRangeMonitor nestingMonitor;


    public ParserBuilder(PsiBuilder builder, DBLanguageDialect languageDialect) {
        this.builder = builder;
        this.nestingMonitor = new NestedRangeMonitor(builder, languageDialect);
    }

    public void advanceLexer(@NotNull ParsePathNode node) {
        advanceLexer(node, false);
    }

    public void advanceLexer(@NotNull ParsePathNode node, boolean mark) {
        nestingMonitor.compute(node, mark);
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
            nestingMonitor.reset();
        }
    }

    public void markerDone(PsiBuilder.Marker marker, ElementType elementType) {
        markerDone(marker, elementType, null);
    }

    public void markerDone(PsiBuilder.Marker marker, ElementType elementType, @Nullable ParsePathNode node) {
        if (marker != null) {
            marker.done((IElementType) elementType);
        }
    }

    public void markerDrop(PsiBuilder.Marker marker) {
        if (marker != null) {
            marker.drop();
        }
    }

    public void settleNesting() {
        nestingMonitor.settle();
    }
}
