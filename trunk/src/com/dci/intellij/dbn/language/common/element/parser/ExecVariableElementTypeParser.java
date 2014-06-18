package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.ParseException;
import com.dci.intellij.dbn.language.common.TokenType;
import com.dci.intellij.dbn.language.common.element.ExecVariableElementType;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.intellij.lang.PsiBuilder;

public class ExecVariableElementTypeParser extends AbstractElementTypeParser<ExecVariableElementType> {
    public ExecVariableElementTypeParser(ExecVariableElementType elementType) {
        super(elementType);
    }

    public ParseResult parse(ParsePathNode parentNode, boolean optional, int depth, ParserContext context) throws ParseException {
        PsiBuilder builder = context.getBuilder();
        logBegin(builder, optional, depth);
        TokenType tokenType = (TokenType) builder.getTokenType();
        if (tokenType != null && !tokenType.isChameleon()){
            if (tokenType.isVariable()) {
                PsiBuilder.Marker marker = builder.mark();
                builder.advanceLexer();
                return stepOut(marker, depth, ParseResultType.FULL_MATCH, 1, null, context);
            }
        }
        return stepOut(null, depth, ParseResultType.NO_MATCH, 0, null, context);
    }  
}
