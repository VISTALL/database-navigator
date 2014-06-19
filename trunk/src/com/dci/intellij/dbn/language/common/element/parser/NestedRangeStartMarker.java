package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.TokenType;

public class NestedRangeStartMarker {
    private int offset;
    private TokenType beginTokenType;
    private TokenType endTokenType;

    public NestedRangeStartMarker(int offset, TokenType beginTokenType, TokenType endTokenType) {
        this.offset = offset;
        this.beginTokenType = beginTokenType;
        this.endTokenType = endTokenType;
    }

    public int getOffset() {
        return offset;
    }

    public TokenType getBeginTokenType() {
        return beginTokenType;
    }

    public TokenType getEndTokenType() {
        return endTokenType;
    }

    @Override
    public String toString() {
        return offset + "";
    }
}
