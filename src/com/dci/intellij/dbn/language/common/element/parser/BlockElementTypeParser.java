package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.ParseException;
import com.dci.intellij.dbn.language.common.element.BlockElementType;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

public class BlockElementTypeParser extends SequenceElementTypeParser<BlockElementType>{
    public BlockElementTypeParser(BlockElementType elementType) {
        super(elementType);
    }

    public ParseResult parse(ParsePathNode parentNode, boolean optional, int depth, ParserContext context) throws ParseException {
        PsiBuilder.Marker marker = context.getBuilder().mark();
        ParseResult result = super.parse(parentNode, optional, depth, context);
        if (result.getType() == ParseResultType.NO_MATCH) {
            marker.drop();
        } else {
            marker.done((IElementType) getElementType());
            context.getNesting().check();
        }
        return result.getType() == ParseResultType.NO_MATCH ?
                ParseResult.createNoMatchResult() :
                ParseResult.createFullMatchResult(result.getMatchedTokens());
    }
}