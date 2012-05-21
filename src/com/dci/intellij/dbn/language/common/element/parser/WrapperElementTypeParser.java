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
        ParsePathNode node = createParseNode(parentNode);
        PsiBuilder.Marker marker = builder.mark();

        boolean isWrappingOptional = getElementType().isWrappingOptional();
        ElementType wrappedElement = getElementType().getWrappedElement();
        ElementType beginTokenElement = getElementType().getBeginTokenElement();
        ElementType endTokenElement = getElementType().getEndTokenElement();

        int matchedTokens = 0;
        boolean isWrapped = false;

        // first try to parse the wrapped element directly, for supporting wrapped elements nesting
        if (isWrappingOptional) {
            ParseResult result = wrappedElement.getParser().parse(node, builder, optional, depth + 1, timestamp);
            if (result.isFullMatch()) {
                matchedTokens = matchedTokens + result.getMatchedTokens();
                return stepOut(builder, marker, depth, ParseResultType.FULL_MATCH, matchedTokens);
            } else {
                marker.rollbackTo();
                marker = builder.mark();
            }
        }

        // parse begin token
        ParseResult result = beginTokenElement.getParser().parse(node, builder, optional, depth + 1, timestamp);

        if (result.isMatch()) {
            isWrapped = true;
            matchedTokens++;
        }

        if (result.isMatch() || isWrappingOptional) {
            result = wrappedElement.getParser().parse(node, builder, false, depth -1, timestamp);

            if (result.isMatch()) {
                matchedTokens = matchedTokens + result.getMatchedTokens();

                if (isWrapped) {
                    // check the end element => exit with partial match if not available
                    result = endTokenElement.getParser().parse(node, builder, false, depth -1, timestamp);

                    if (result.isMatch()) {
                        matchedTokens++;
                    } else {
                        return stepOut(builder, marker, depth, ParseResultType.PARTIAL_MATCH, matchedTokens);
                    }
                }

                return stepOut(builder, marker, depth, ParseResultType.FULL_MATCH, matchedTokens);
            }
        }


        return stepOut(builder, marker, depth, ParseResultType.NO_MATCH, matchedTokens);
    }
}