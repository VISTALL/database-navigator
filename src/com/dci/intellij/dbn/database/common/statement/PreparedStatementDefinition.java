package com.dci.intellij.dbn.database.common.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreparedStatementDefinition extends StatementDefinition {

    private Integer[] placeholders;

    public PreparedStatementDefinition(String text, String prefix, boolean hasFallback) {
        super(text, prefix, hasFallback);

        StringBuilder buffer = new StringBuilder();
        List<Integer> placeholders = new ArrayList<Integer>();
        int startIndex = statementText.indexOf('{');
        if (startIndex == -1) {
            buffer.append(statementText);
        } else {
            int endIndex = 0;
            while (startIndex > -1) {
                String segment = statementText.substring(endIndex, startIndex);
                buffer.append(segment).append(" ? ");
                endIndex = statementText.indexOf('}', startIndex);
                String placeholder = statementText.substring(startIndex + 1, endIndex);

                placeholders.add(new Integer(placeholder));
                startIndex = statementText.indexOf('{', endIndex);
                endIndex = endIndex + 1;
            }
            if (endIndex < statementText.length()) {
                buffer.append(statementText.substring(endIndex));
            }
        }
        this.statementText = buffer.toString();
        this.placeholders = placeholders.toArray(new Integer[placeholders.size()]);
    }

    public PreparedStatement prepareStatement(Connection connection, Object[] arguments) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(statementText);
        for (int i = 0; i < placeholders.length; i++) {
            preparedStatement.setObject(i + 1, arguments[placeholders[i]]);
        }
        return preparedStatement;
    }
}
