package com.dci.intellij.dbn.language.sql.dialect.postgres;

import com.dci.intellij.dbn.language.common.ChameleonTokenType;
import com.dci.intellij.dbn.language.common.DBLanguageDialectIdentifier;
import com.dci.intellij.dbn.language.common.DBLanguageSyntaxHighlighter;
import com.dci.intellij.dbn.language.sql.dialect.SQLLanguageDialect;
import com.dci.intellij.dbn.language.sql.dialect.mysql.MysqlSQLParser;
import com.dci.intellij.dbn.language.sql.dialect.mysql.MysqlSQLSyntaxHighlighter;

import java.util.Set;

public class PostgresSQLLanguageDialect extends SQLLanguageDialect {

    public PostgresSQLLanguageDialect() {
        super(DBLanguageDialectIdentifier.POSTGRES_SQL);
    }

    @Override
    protected Set<ChameleonTokenType> createChameleonTokenTypes() {
        return null;
    }

    protected DBLanguageSyntaxHighlighter createSyntaxHighlighter() {
        return new MysqlSQLSyntaxHighlighter(this);
}
    protected PostgresSQLParserDefinition createParserDefinition() {
        MysqlSQLParser parser = new MysqlSQLParser(this);
        return new PostgresSQLParserDefinition(parser);
    }

}