package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.ParseException;
import com.dci.intellij.dbn.language.common.element.DBNElementType;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;

public interface ElementTypeParser<T extends DBNElementType>{
    ParseResult parse(ParsePathNode parentNode, boolean optional, int depth, ParserContext context) throws ParseException;
    T getElementType();
}
