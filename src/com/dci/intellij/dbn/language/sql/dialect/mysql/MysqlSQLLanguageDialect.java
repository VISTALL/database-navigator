package com.dci.intellij.dbn.language.sql.dialect.mysql;

import com.dci.intellij.dbn.language.common.DBLanguageDialectIdentifier;
import com.dci.intellij.dbn.language.common.DBLanguageSyntaxHighlighter;
import com.dci.intellij.dbn.language.common.InjectedLanguageTokenType;
import com.dci.intellij.dbn.language.sql.dialect.SQLLanguageDialect;

import java.util.Set;

public class MysqlSQLLanguageDialect extends SQLLanguageDialect {

    public MysqlSQLLanguageDialect() {
        super(DBLanguageDialectIdentifier.MYSQL_SQL);
    }

    @Override
    protected Set<InjectedLanguageTokenType> createInjectedLanguageTokens() {
        return null;
    }

    protected DBLanguageSyntaxHighlighter createSyntaxHighlighter() {
        return new MysqlSQLSyntaxHighlighter(this);
}
    protected MysqlSQLParserDefinition createParserDefinition() {
        MysqlSQLParser parser = new MysqlSQLParser(this);
        return new MysqlSQLParserDefinition(parser);
    }

}