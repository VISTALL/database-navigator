package com.dci.intellij.dbn.language.sql.dialect.postgres;

import com.dci.intellij.dbn.language.sql.SQLParserDefinition;
import com.dci.intellij.dbn.language.sql.dialect.mysql.MysqlSQLParser;
import com.dci.intellij.dbn.language.sql.dialect.mysql.MysqlSQLParserFlexLexer;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


public class PostgresSQLParserDefinition extends SQLParserDefinition {

    public PostgresSQLParserDefinition(MysqlSQLParser parser) {
        super(parser);
    }

    @NotNull
    public Lexer createLexer(Project project) {
        return new FlexAdapter(new MysqlSQLParserFlexLexer(getTokenTypes()));
    }

}