package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.ParseException;
import com.dci.intellij.dbn.language.common.TokenType;
import com.dci.intellij.dbn.language.common.element.BlockElementType;
import com.dci.intellij.dbn.language.common.element.ElementType;
import com.dci.intellij.dbn.language.common.element.IdentifierElementType;
import com.dci.intellij.dbn.language.common.element.IterationElementType;
import com.dci.intellij.dbn.language.common.element.SequenceElementType;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.dci.intellij.dbn.language.common.element.path.PathNode;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.element.util.ParseBuilderErrorHandler;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

public class SequenceElementTypeParser<ET extends SequenceElementType> extends AbstractElementTypeParser<ET> {
    public SequenceElementTypeParser(ET elementType) {
        super(elementType);
    }

    public ParseResult parse(ParsePathNode parentNode, PsiBuilder builder, boolean optional, int depth, long timestamp) throws ParseException {
        logBegin(builder, optional, depth);
        ParsePathNode node = createParseNode(parentNode);
        SequenceElementType elementType = getElementType();
        ElementType[] elementTypes = elementType.getElementTypes();

        PsiBuilder.Marker marker = builder.mark();
        int matches = 0;
        int matchedTokens = 0;

        TokenType tokenType = (TokenType) builder.getTokenType();
        boolean isDummyToken = isDummyToken(builder.getTokenText());
        boolean isSuppressibleReservedWord =
                tokenType instanceof TokenType &&
                !elementType.is(ElementTypeAttribute.STATEMENT) &&
                isSuppressibleReservedWord(tokenType, node);


        if (tokenType != null && !tokenType.isChameleon() && (isDummyToken || isSuppressibleReservedWord || elementType.getLookupCache().canStartWithToken(tokenType))) {
            for (int i = 0; i < elementTypes.length; i++) {
                // is end of document
                if (tokenType == null || tokenType.isChameleon()) {
                    ParseResultType resultType =
                            elementType.isOptional(i) && (elementType.isLast(i) || elementType.isOptionalFromIndex(i)) ? ParseResultType.FULL_MATCH :
                                    !elementType.isFirst(i) && !elementType.isOptionalFromIndex(i) && !elementType.isExitIndex(i) ? ParseResultType.PARTIAL_MATCH : ParseResultType.NO_MATCH;
                    return stepOut(builder, marker, depth, resultType, matchedTokens, node);
                }

                ParseResult result = ParseResult.createNoMatchResult();
                // current token can still be part of the iterated element.
                //if (elementTypes[i].containsToken(tokenType)) {
                if (isDummyToken || elementTypes[i].getLookupCache().canStartWithToken(tokenType) || isSuppressibleReservedWord(tokenType, node)) {

                    // /ParseNode pathNode = new ParseNode(parentPath, this, i, builder.getCurrentOffset());
                    node = node.createVariant(builder.getCurrentOffset(), i);
                    result = elementTypes[i].getParser().parse(node, builder, elementType.isOptional(i), depth + 1, timestamp);

                    if (result.isMatch()) {
                        matchedTokens = matchedTokens + result.getMatchedTokens();
                        tokenType = (TokenType) builder.getTokenType();
                        isDummyToken = isDummyToken(builder.getTokenText());
                        matches++;
                    }
                }

                // not matched and not optional
                if (result.isNoMatch() && !elementType.isOptional(i)) {
                    boolean isWeakMatch = matches < 2 && matchedTokens < 3 && i > 1 && ignoreFirstMatch();
                    
                    if (elementType.isFirst(i) || elementType.isExitIndex(i) || isWeakMatch || matches == 0) {
                        //if (isFirst(i) || isExitIndex(i)) {
                        return stepOut(builder, marker, depth, ParseResultType.NO_MATCH, matchedTokens, node);
                    }

                    int offset = advanceLexerToNextLandmark(builder, i, parentNode, timestamp);

                    // no landmarks found or landmark in parent found
                    if (offset == 0 || offset < 0) {
                        /*if (offset == i) {
                            elementTypes[i].parse(node, builder, isOptional(i), depth + 1, timestamp);
                        }*/
                        return stepOut(builder, marker, depth, ParseResultType.PARTIAL_MATCH, matchedTokens, node);
                    }


                    tokenType = (TokenType) builder.getTokenType();
                    isDummyToken = isDummyToken(builder.getTokenText());
                    // local landmarks found
                    if (offset > 0) {
                        i = offset - 1;
                        continue;
                    }
                }

                // if is last element
                if (elementType.isLast(i)) {
                    //matches == 0 reaches this stage only if all sequence elements are optional
                    return stepOut(builder, marker, depth,
                            matches == 0 ? ParseResultType.NO_MATCH : ParseResultType.FULL_MATCH, matchedTokens, node);
                }
            }
        }

        return stepOut(builder, marker, depth, ParseResultType.NO_MATCH, matchedTokens, node);
    }

    private boolean ignoreFirstMatch() {
        ElementType firstElementType = getElementType().getElementTypes()[0];
        if (firstElementType instanceof IdentifierElementType) {
            IdentifierElementType identifierElementType = (IdentifierElementType) firstElementType;
            return !identifierElementType.isDefinition();
        }
        return false;
    }

    protected ParseResult stepOut(PsiBuilder builder, PsiBuilder.Marker marker, int depth, ParseResultType resultType, int matchedTokens, PathNode node) {
        if (marker != null) {
            if (resultType == ParseResultType.NO_MATCH) {
                marker.rollbackTo();
            } else {
                /*if (this instanceof NamedElementType)
                    marker.done(this); else
                    marker.drop();*/
                if (getElementType() instanceof BlockElementType)
                    marker.drop(); else
                    marker.done((IElementType) getElementType());
            }
        }
        return super.stepOut(builder, null, depth, resultType, matchedTokens, node);
    }    

    private int advanceLexerToNextLandmark(PsiBuilder builder, int index, ParsePathNode parentNode, long timestamp) {
        PsiBuilder.Marker marker = builder.mark();
        SequenceElementType elementType = getElementType();
        ParseBuilderErrorHandler.updateBuilderError(builder, elementType.getFirstPossibleTokensFromIndex(index), timestamp);

        if (!builder.eof()) {
            TokenType tokenType = (TokenType) builder.getTokenType();
            int newIndex = getLandmarkIndex(tokenType, index, parentNode);
            if (newIndex == index) {
                builder.advanceLexer();
            }
        }

        while (!builder.eof()) {
            TokenType tokenType = (TokenType) builder.getTokenType();
            if (tokenType != null) {
                int newIndex = getLandmarkIndex(tokenType, index, parentNode);

                // no landmark hit -> spool the builder
                if (newIndex == 0) {
                    builder.advanceLexer();
                } else {
                    marker.done((IElementType) getElementBundle().getUnknownElementType()); // should close unknown element type
                    return newIndex;
                }
            }
        }
        marker.done((IElementType) getElementBundle().getUnknownElementType()); // should close unknown element type
        return 0;
    }

    protected int getLandmarkIndex(TokenType tokenType, int index, ParsePathNode parentParseNode) {
        if (tokenType.isParserLandmark()) {
            for (int i=index; i<getElementType().getElementTypes().length; i++) {
                // check children landmarks
                if (getElementType().getElementTypes()[i].getLookupCache().canStartWithToken(tokenType)) {
                    return i;
                }
            }

            ParsePathNode parseNode = parentParseNode;
            while (parseNode != null) {
                if (parseNode.getElementType() instanceof SequenceElementType) {
                    SequenceElementType sequenceElementType = (SequenceElementType) parseNode.getElementType();
                    if ( sequenceElementType.containsLandmarkTokenFromIndex(tokenType, parseNode.getIndexInParent() + 1)) {
                        return -1;
                    }
                }
                if (parseNode.getElementType() instanceof IterationElementType) {
                    IterationElementType iterationElementType = (IterationElementType) parseNode.getElementType();
                    if (iterationElementType.isSeparator(tokenType)) {
                        return -1;
                    }
                }
                parseNode = parseNode.getParent();
            }
        }
        return 0;
    }


}
