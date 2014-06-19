package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.SharedTokenTypeBundle;
import com.dci.intellij.dbn.language.common.SimpleTokenType;
import com.dci.intellij.dbn.language.common.TokenType;
import com.intellij.lang.PsiBuilder;
import com.intellij.util.containers.Stack;

public class NestedRangeMonitor {
    private Stack<NestedRangeStartMarker> markers = new Stack<NestedRangeStartMarker>();
    private PsiBuilder builder;

    private SimpleTokenType leftParenthesis;
    private SimpleTokenType rightParenthesis;


    public NestedRangeMonitor(PsiBuilder builder, DBLanguageDialect languageDialect) {
        this.builder = builder;

        SharedTokenTypeBundle sharedTokenTypes = languageDialect.getParserTokenTypes().getSharedTokenTypes();
        leftParenthesis = sharedTokenTypes.getLeftParenthesis();
        rightParenthesis = sharedTokenTypes.getRightParenthesis();
    }

    public void compute() {
        TokenType tokenType = (TokenType) builder.getTokenType();
        if (tokenType == leftParenthesis) {
            NestedRangeStartMarker last = null;
            int builderOffset = builder.getCurrentOffset();
            while (!markers.empty()) {
                last = markers.peek();
                if (last.getOffset() > builderOffset) {
                    markers.pop();
                } else  {
                    break;
                }
            }

            if (last == null || last.getOffset() < builderOffset) {
                NestedRangeStartMarker marker = new NestedRangeStartMarker(builderOffset, leftParenthesis, rightParenthesis);
                markers.push(marker);
            }

        } else if (tokenType == rightParenthesis) {
            if (markers.size() > 0) {
                NestedRangeStartMarker marker = markers.pop();
                //marker.getBuilderMarker().drop();
            }
        }

    }

}
