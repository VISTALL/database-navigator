package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.TokenType;
import com.intellij.lang.PsiBuilder;

import java.util.ArrayList;
import java.util.List;

public class NestedRangeMonitor {
    private List<RangeMarker> rangeMarkers = new ArrayList<RangeMarker>();


    public void openRange(PsiBuilder builder, TokenType openingToken, TokenType closingToken) {

    }

    public void closeRange(PsiBuilder builder, TokenType closingToken) {

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
