package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.ParseException;
import com.dci.intellij.dbn.language.common.TokenType;
import com.dci.intellij.dbn.language.common.element.TokenElementType;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.intellij.lang.PsiBuilder;

public class TokenElementTypeParser extends AbstractElementTypeParser<TokenElementType> {
    public TokenElementTypeParser(TokenElementType elementType) {
        super(elementType);
    }

    public ParseResult parse(ParsePathNode parentNode, PsiBuilder builder, boolean optional, int depth, long timestamp) throws ParseException {
        logBegin(builder, optional, depth);

        TokenType tokenType = (TokenType) builder.getTokenType();
        if (tokenType == getElementType().getTokenType() || isDummyToken(builder.getTokenText())) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            return stepOut(builder, marker, depth, ParseResultType.FULL_MATCH, 1, null);
        }
        return stepOut(builder, null, depth, ParseResultType.NO_MATCH, 0, null);
    }
}
