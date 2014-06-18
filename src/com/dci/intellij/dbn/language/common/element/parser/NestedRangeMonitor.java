package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.TokenType;
import com.dci.intellij.dbn.language.common.element.DBNElementType;
import com.dci.intellij.dbn.language.common.element.impl.WrappingDefinition;
import com.intellij.lang.PsiBuilder;

import java.util.ArrayList;
import java.util.List;

public class NestedRangeMonitor {
    private DBLanguageDialect languageDialect;
    private List<RangeMarker> rangeMarkers = new ArrayList<RangeMarker>();
    private PsiBuilder builder;

    public NestedRangeMonitor(DBLanguageDialect languageDialect, PsiBuilder builder) {
        this.languageDialect = languageDialect;
        this.builder = builder;
    }

    public void start(DBNElementType elementType) {
        WrappingDefinition wrapping = elementType.getWrapping();
        if (wrapping != null) {
            TokenType beginTokenType = wrapping.getBeginElementType().getTokenType();
            TokenType endTokenType = wrapping.getEndElementType().getTokenType();
            while (beginTokenType.equals(builder.getTokenType())) {
                rangeMarkers.add(new RangeMarker(builder.mark(), beginTokenType, endTokenType));
                builder.advanceLexer();
            }
        }
    }
    public void rollback(DBNElementType elementType) {
        WrappingDefinition wrapping = elementType.getWrapping();
        if (wrapping != null) {
            int openRanges = rangeMarkers.size();
            int lastMarkerIndex = openRanges - 1;
            RangeMarker lastRange = rangeMarkers.get(lastMarkerIndex);
            lastRange.marker.rollbackTo();
            rangeMarkers.remove(lastMarkerIndex);
        }
    }

    public RangeMarker check() {
        int openRanges = rangeMarkers.size();
        if (openRanges > 0) {
            int lastMarkerIndex = openRanges - 1;
            RangeMarker lastRange = rangeMarkers.get(lastMarkerIndex);
            if (lastRange.endTokenType.equals(builder.getTokenType())) {
                return lastRange;
            }
        }
        return null;
    }

    public void close(RangeMarker rangeMarker) {
        if (rangeMarker != null) {
            rangeMarker.marker.done(languageDialect.getNestedRangeElementType());
            rangeMarkers.remove(rangeMarker);
            builder.advanceLexer();
        }
    }


    public class RangeMarker {
        PsiBuilder.Marker marker;
        TokenType beginTokenType;
        TokenType endTokenType;

        private RangeMarker(PsiBuilder.Marker marker, TokenType beginTokenType, TokenType endTokenType) {
            this.marker = marker;
            this.beginTokenType = beginTokenType;
            this.endTokenType = endTokenType;
        }
    }
}
