package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.SharedTokenTypeBundle;
import com.dci.intellij.dbn.language.common.SimpleTokenType;
import com.dci.intellij.dbn.language.common.TokenType;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.util.containers.Stack;

public class NestedRangeMonitor {
    private Stack<NestedRangeStartMarker> markers = new Stack<NestedRangeStartMarker>();
    private DBLanguageDialect languageDialect;
    private PsiBuilder builder;

    private SimpleTokenType leftParenthesis;
    private SimpleTokenType rightParenthesis;


    public NestedRangeMonitor(PsiBuilder builder, DBLanguageDialect languageDialect) {
        this.builder = builder;
        this.languageDialect = languageDialect;

        SharedTokenTypeBundle sharedTokenTypes = languageDialect.getParserTokenTypes().getSharedTokenTypes();
        leftParenthesis = sharedTokenTypes.getLeftParenthesis();
        rightParenthesis = sharedTokenTypes.getRightParenthesis();
    }

    public void compute(ParsePathNode parentNode, boolean mark) {
        TokenType tokenType = (TokenType) builder.getTokenType();
        if (tokenType == leftParenthesis) {
            NestedRangeStartMarker last = null;
            int builderOffset = builder.getCurrentOffset();
            while (!markers.empty()) {
                last = markers.peek();
                if (last.getOffset() >= builderOffset) {
                    markers.pop();
                    PsiBuilder.Marker builderMarker = last.getBuilderMarker();
                    if (builderMarker != null) {
                        builderMarker.drop();
                    }
                } else  {
                    break;
                }
            }

            NestedRangeStartMarker marker = new NestedRangeStartMarker(parentNode, builderOffset);
            if (mark) marker.setBuilderMarker(builder.mark());
            markers.push(marker);

        } else if (tokenType == rightParenthesis) {
            if (markers.size() > 0) {
                NestedRangeStartMarker marker = markers.pop();
                PsiBuilder.Marker builderMarker = marker.getBuilderMarker();
                if (builderMarker != null) {
                    builderMarker.done(languageDialect.getNestedRangeElementType());
                }


                //marker.getBuilderMarker().drop();
            }
        }

    }

}
