package com.dci.intellij.dbn.language.sql.dialect.oracle;

import com.dci.intellij.dbn.language.common.*;
import com.dci.intellij.dbn.language.common.element.ChameleonTokenType;
import com.dci.intellij.dbn.language.sql.dialect.SQLLanguageDialect;

import java.util.HashSet;
import java.util.Set;

public class OracleSQLLanguageDialect extends SQLLanguageDialect {
    ChameleonTokenType plsqlChameleonTokenType;
    public OracleSQLLanguageDialect() {
        super(DBLanguageDialectIdentifier.ORACLE_SQL);
    }

    @Override
    protected Set<InjectedLanguageTokenType> createInjectedLanguageTokens() {
        Set<InjectedLanguageTokenType> tokenTypes = new HashSet<InjectedLanguageTokenType>();
        DBLanguageDialect plsql = DBLanguageDialect.getLanguageDialect(DBLanguageDialectIdentifier.ORACLE_PLSQL);
        tokenTypes.add(new InjectedLanguageTokenType(this, plsql));
        return tokenTypes;
    }

    @Override
    public ChameleonTokenType getChameleonTokenType(DBLanguageDialectIdentifier dialectIdentifier) {
        if (dialectIdentifier == DBLanguageDialectIdentifier.ORACLE_PLSQL) {
            if (plsqlChameleonTokenType == null) {
                DBLanguageDialect plsqlDialect = DBLanguageDialect.getLanguageDialect(DBLanguageDialectIdentifier.ORACLE_PLSQL);
                plsqlChameleonTokenType = new ChameleonTokenType(plsqlDialect);
            }
            return plsqlChameleonTokenType;
        }
        return super.getChameleonTokenType(dialectIdentifier);
    }

    protected DBLanguageSyntaxHighlighter createSyntaxHighlighter() {
        return new OracleSQLSyntaxHighlighter(this);
}

    protected OracleSQLParserDefinition createParserDefinition() {
        OracleSQLParser parser = new OracleSQLParser(this);
        return new OracleSQLParserDefinition(parser);
    }

}
