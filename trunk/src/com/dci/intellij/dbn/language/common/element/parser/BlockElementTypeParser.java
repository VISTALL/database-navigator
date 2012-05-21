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

    public ParseResult parse(ParsePathNode parentNode, PsiBuilder builder, boolean optional, int depth, long timestamp) throws ParseException {
        PsiBuilder.Marker marker = builder.mark();
        ParseResult result = super.parse(parentNode, builder, optional, depth, timestamp);
        if (result.getType() == ParseResultType.NO_MATCH) {
            marker.drop();
        } else {
            marker.done((IElementType) getElementType());
        }
        return result.getType() == ParseResultType.NO_MATCH ?
                ParseResult.createNoMatchResult() :
                ParseResult.createFullMatchResult(result.getMatchedTokens());
    }
}