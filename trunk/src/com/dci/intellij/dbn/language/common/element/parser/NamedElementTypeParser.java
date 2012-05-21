package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.ParseException;
import com.dci.intellij.dbn.language.common.element.NamedElementType;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.dci.intellij.dbn.language.common.element.path.PathNode;
import com.intellij.lang.PsiBuilder;

public class NamedElementTypeParser extends SequenceElementTypeParser<NamedElementType>{
    private long matches;

    public NamedElementTypeParser(NamedElementType elementType) {
        super(elementType);
    }

    public ParseResult parse(ParsePathNode parentNode, PsiBuilder builder, boolean optional, int depth, long timestamp) throws ParseException {
        if (isRecursive(parentNode, builder.getCurrentOffset(), 2)) {
            return ParseResult.createNoMatchResult();
        }
        ParseResult result = super.parse(parentNode, builder, optional, depth, timestamp);
        if (result.isMatch()) matches++;
        return result;
    }

    protected boolean isRecursive(ParsePathNode parseNode, int builderOffset, int iterations){
        while (parseNode != null &&  iterations > 0) {
            if (parseNode.getElementType() == getElementType() &&
                    parseNode.getBuilderOffset() == builderOffset) {
                iterations--;
            }
            parseNode = parseNode.getParent();
        }
        return iterations == 0;
    }

    private int countRecurences(PathNode node) {
        PathNode parent = node;
        int count = 0;
        while(parent != null) {
            if (parent.getElementType() == getElementType()) {
                count++;
            }
            parent = parent.getParent();
        }
        return count;
    }
}