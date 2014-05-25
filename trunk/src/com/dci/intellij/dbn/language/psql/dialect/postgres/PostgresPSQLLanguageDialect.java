package com.dci.intellij.dbn.language.psql.dialect.postgres;

import com.dci.intellij.dbn.language.common.DBLanguageDialectIdentifier;
import com.dci.intellij.dbn.language.common.DBLanguageSyntaxHighlighter;
import com.dci.intellij.dbn.language.psql.dialect.PSQLLanguageDialect;
import com.dci.intellij.dbn.language.psql.dialect.oracle.OraclePLSQLSyntaxHighlighter;

public class PostgresPSQLLanguageDialect extends PSQLLanguageDialect {
    public PostgresPSQLLanguageDialect() {
        super(DBLanguageDialectIdentifier.POSTGRES_PSQL);
    }

    protected DBLanguageSyntaxHighlighter createSyntaxHighlighter() {
        return new OraclePLSQLSyntaxHighlighter(this);
}

    protected PostgresPSQLParserDefinition createParserDefinition() {
        return new PostgresPSQLParserDefinition();
    }

}
