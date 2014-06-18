package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.ParseException;
import com.dci.intellij.dbn.language.common.TokenType;
import com.dci.intellij.dbn.language.common.element.IdentifierElementType;
import com.dci.intellij.dbn.language.common.element.LeafElementType;
import com.dci.intellij.dbn.language.common.element.QualifiedIdentifierElementType;
import com.dci.intellij.dbn.language.common.element.TokenElementType;
import com.dci.intellij.dbn.language.common.element.impl.QualifiedIdentifierVariant;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.dci.intellij.dbn.language.common.element.path.PathNode;
import com.dci.intellij.dbn.language.common.element.util.ParseBuilderErrorHandler;
import com.intellij.lang.PsiBuilder;
import gnu.trove.THashSet;

import java.util.Set;

public class QualifiedIdentifierElementTypeParser extends AbstractElementTypeParser<QualifiedIdentifierElementType> {
    public QualifiedIdentifierElementTypeParser(QualifiedIdentifierElementType elementType) {
        super(elementType);
    }

    public ParseResult parse(ParsePathNode parentNode, boolean optional, int depth, ParserContext context) throws ParseException {
        PsiBuilder builder = context.getBuilder();
        logBegin(builder, optional, depth);
        ParsePathNode node = createParseNode(parentNode, builder.getCurrentOffset());

        TokenElementType separatorToken = getElementType().getSeparatorToken();

        PsiBuilder.Marker marker = builder.mark();
        int matchedTokens = 0;

        QualifiedIdentifierVariant variant = getMostProbableParseVariant(builder, node);
        if (variant != null) {
            LeafElementType[] elementTypes = variant.getLeafs();

            int currentSiblingPosition = 0;
            for (LeafElementType elementType : elementTypes) {
                node.setCurrentSiblingPosition(currentSiblingPosition);
                ParseResult result = elementType.getParser().parse(node, true, depth + 1, context);
                if (result.isNoMatch()) break;  else matchedTokens = matchedTokens + result.getMatchedTokens();

                if (elementType != elementTypes[elementTypes.length -1])  {
                    result = separatorToken.getParser().parse(node, true, depth + 1, context);
                    if (result.isNoMatch()) break; else matchedTokens = matchedTokens + result.getMatchedTokens();
                }
                currentSiblingPosition++;
            }

            if (variant.isIncomplete()) {
                Set<TokenType> expected = new THashSet<TokenType>();
                expected.add(separatorToken.getTokenType());
                ParseBuilderErrorHandler.updateBuilderError(expected, context);
                return stepOut(marker, depth, ParseResultType.PARTIAL_MATCH, matchedTokens, node, context);
            } else {
                return stepOut(marker, depth, ParseResultType.FULL_MATCH, matchedTokens, node, context);
            }


        } else {
            return stepOut(marker, depth, ParseResultType.NO_MATCH, matchedTokens, node, context);
        }
    }

    private QualifiedIdentifierVariant getMostProbableParseVariant(PsiBuilder builder, PathNode node) {
        TokenElementType separatorToken = getElementType().getSeparatorToken();

        QualifiedIdentifierVariant mostProbableVariant = null;
        for (LeafElementType[] elementTypes : getElementType().getVariants()) {
            PsiBuilder.Marker marker = builder.mark();
            int matchedTokens = 0;
            for (int i=0; i< elementTypes.length; i++) {
                TokenType tokenType = (TokenType) builder.getTokenType();
                // if no mach -> consider as partial if not first element

                if (match(elementTypes[i], tokenType, node, true)) {
                    matchedTokens++;
                    builder.advanceLexer();

                    tokenType = (TokenType) builder.getTokenType();
                    boolean isSeparator = tokenType == separatorToken.getTokenType();
                    boolean isFullMatch = matchedTokens == elementTypes.length;
                    boolean isLastElement = i == elementTypes.length - 1;

                    if (isLastElement) {
                        QualifiedIdentifierVariant variant = new QualifiedIdentifierVariant(elementTypes, matchedTokens);
                        if ((isFullMatch && !isSeparator) || variant.containsNonIdentifierTokens()) {
                            markerRollbackTo(marker);
                            return variant;
                        }

                        if (mostProbableVariant == null || matchedTokens > mostProbableVariant.getMatchedTokens()) {
                            mostProbableVariant = variant;
                        }
                        break;
                    } else {
                        if (!isSeparator) {  // is not separator token
                            QualifiedIdentifierVariant variant = new QualifiedIdentifierVariant(elementTypes, matchedTokens);
                            if (mostProbableVariant == null || mostProbableVariant.getMatchedTokens() < variant.getMatchedTokens()) {
                                mostProbableVariant = variant;
                            }
                            break;
                        }
                    }
                    builder.advanceLexer();
                } else {
                    if (matchedTokens > 0)  {
                        QualifiedIdentifierVariant variant = new QualifiedIdentifierVariant(elementTypes, matchedTokens);
                        if (variant.containsNonIdentifierTokens()) {
                            markerRollbackTo(marker);
                            return variant;
                        }
                        if (mostProbableVariant == null || mostProbableVariant.getMatchedTokens() < variant.getMatchedTokens()) {
                            mostProbableVariant = variant;
                        }
                    }
                    break;
                }
            }
            markerRollbackTo(marker);

        }
        return mostProbableVariant;
    }

    private boolean match(LeafElementType leafElementType, TokenType tokenType, PathNode node, boolean leniant) {
        if (leafElementType instanceof IdentifierElementType) {
            return tokenType != null && (tokenType.isIdentifier() || (leniant && isSuppressibleReservedWord(tokenType, node)));
        } else if (leafElementType instanceof TokenElementType) {
            TokenElementType tokenElementType = (TokenElementType) leafElementType;
            return tokenElementType.getTokenType() == tokenType;
        }
        return false;
    }
}