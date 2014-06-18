package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.TokenType;
import com.intellij.lang.PsiBuilder;

import java.util.ArrayList;
import java.util.List;

public class NestedRangeMonitor {
    private List<RangeMarker> rangeMarkers = new ArrayList<RangeMarker>();


    public void openRange(PsiBuilder builder, TokenType openingToken, TokenType closingToken) {
        rangeMarkers.add(new RangeMarker(builder.mark(), openingToken, closingToken));
    }

    public void closeRange(DBLanguageDialect languageDialect, PsiBuilder builder, TokenType closingToken) {
        int openRanges = rangeMarkers.size();
        if (openRanges > 0) {
            RangeMarker lastRange = rangeMarkers.get(openRanges -1);
            if (lastRange.closingToken.equals(closingToken)) {
                lastRange.marker.done(languageDialect.getNestedRangeElementType());
            }
        }
    }


    private class RangeMarker {
        PsiBuilder.Marker marker;
        TokenType openingToken;
        TokenType closingToken;

        private RangeMarker(PsiBuilder.Marker marker, TokenType openingToken, TokenType closingToken) {
            this.marker = marker;
            this.openingToken = openingToken;
            this.closingToken = closingToken;
        }
    }
}
