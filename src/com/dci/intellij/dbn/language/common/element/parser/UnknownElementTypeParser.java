package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.ParseException;
import com.dci.intellij.dbn.language.common.element.UnknownElementType;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;

public class UnknownElementTypeParser extends AbstractElementTypeParser<UnknownElementType> {
    public UnknownElementTypeParser(UnknownElementType elementType) {
        super(elementType);
    }

    public ParseResult parse(ParsePathNode parentNode, boolean optional, int depth, ParserContext context) throws ParseException {
        return ParseResult.createNoMatchResult();
    }
}