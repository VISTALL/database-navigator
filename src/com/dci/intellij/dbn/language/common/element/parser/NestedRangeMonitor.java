package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.SharedTokenTypeBundle;
import com.dci.intellij.dbn.language.common.SimpleTokenType;
import com.dci.intellij.dbn.language.common.TokenType;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.intellij.lang.PsiBuilder;

import java.util.ArrayList;
import java.util.List;

public class NestedRangeMonitor {
    private List<NestedRangeStartMarker> markers = new ArrayList<NestedRangeStartMarker>();
    private PsiBuilder builder;

    private SimpleTokenType leftParenthesis;
    private SimpleTokenType rightParenthesis;


    public NestedRangeMonitor(PsiBuilder builder, DBLanguageDialect languageDialect) {
        this.builder = builder;

        SharedTokenTypeBundle sharedTokenTypes = languageDialect.getParserTokenTypes().getSharedTokenTypes();
        leftParenthesis = sharedTokenTypes.getLeftParenthesis();
        rightParenthesis = sharedTokenTypes.getRightParenthesis();
    }

    public void compute(ParsePathNode node) {
        TokenType tokenType = (TokenType) builder.getTokenType();
        if (tokenType == leftParenthesis) {
            markers.add(new NestedRangeStartMarker(builder.mark(), leftParenthesis, rightParenthesis));
        } else if (tokenType == rightParenthesis) {
            if (markers.size() > 0) {
                NestedRangeStartMarker marker = markers.remove(markers.size() - 1);
                marker.getBuilderMarker().drop();
            }
        }

    }

}
