package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.ParseException;
import com.dci.intellij.dbn.language.common.element.ElementType;
import com.dci.intellij.dbn.language.common.element.WrapperElementType;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.intellij.lang.PsiBuilder;

public class WrapperElementTypeParser extends AbstractElementTypeParser<WrapperElementType> {
    public WrapperElementTypeParser(WrapperElementType elementType) {
        super(elementType);
    }

    public ParseResult parse(ParsePathNode parentNode, PsiBuilder builder, boolean optional, int depth, long timestamp) throws ParseException {
        logBegin(builder, optional, depth);
        ParsePathNode node = createParseNode(parentNode, builder.getCurrentOffset());
        PsiBuilder.Marker marker = builder.mark();

        boolean isWrappingOptional = getElementType().isWrappingOptional();
        ElementType wrappedElement = getElementType().getWrappedElement();
        ElementType beginTokenElement = getElementType().getBeginTokenElement();
        ElementType endTokenElement = getElementType().getEndTokenElement();

        int matchedTokens = 0;
        boolean isWrapped = false;

        // first try to parse the wrapped element directly, for supporting wrapped elements nesting
        if (isWrappingOptional) {
            ParseResult wrappedResult = wrappedElement.getParser().parse(node, builder, optional, depth + 1, timestamp);
            if (wrappedResult.isMatch()) {
                matchedTokens = matchedTokens + wrappedResult.getMatchedTokens();
                return stepOut(builder, marker, depth, wrappedResult.getType(), matchedTokens, node);
            } else {
                marker.rollbackTo();
                marker = builder.mark();
            }
        }

        // parse begin token
        ParseResult beginTokenResult = beginTokenElement.getParser().parse(node, builder, optional, depth + 1, timestamp);

        if (beginTokenResult.isMatch()) {
            isWrapped = true;
            matchedTokens++;
        }

        if (beginTokenResult.isMatch() || isWrappingOptional) {
            ParseResult wrappedResult = wrappedElement.getParser().parse(node, builder, false, depth -1, timestamp);
            matchedTokens = matchedTokens + wrappedResult.getMatchedTokens();

            if (isWrapped) {
                // check the end element => exit with partial match if not available
                ParseResult endTokenResult = endTokenElement.getParser().parse(node, builder, false, depth -1, timestamp);
                if (endTokenResult.isMatch()) {
                    matchedTokens++;
                    return stepOut(builder, marker, depth, ParseResultType.FULL_MATCH, matchedTokens, node);
                } else {
                    return stepOut(builder, marker, depth, wrappedResult.getType(), matchedTokens, node);
                }
            } else {
                return stepOut(builder, marker, depth, wrappedResult.getType(), matchedTokens, node);
            }
        }

        return stepOut(builder, marker, depth, ParseResultType.NO_MATCH, matchedTokens, node);
    }
}