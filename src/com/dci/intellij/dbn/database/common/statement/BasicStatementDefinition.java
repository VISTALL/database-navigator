package com.dci.intellij.dbn.database.common.statement;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BasicStatementDefinition extends StatementDefinition{
    public BasicStatementDefinition(String text, String prefix, boolean hasFallback) {
        super(text, prefix, hasFallback);

        List<Integer> placeholders = new ArrayList<Integer>();
        int startIndex = statementText.indexOf('{');
        if (startIndex > -1) {
            int endIndex = 0;
            while (startIndex > -1) {
                endIndex = statementText.indexOf('}', startIndex);
                String placeholder = statementText.substring(startIndex + 1, endIndex);
                placeholders.add(new Integer(placeholder));
                startIndex = statementText.indexOf('{', endIndex);
            }
        }

        this.placeholders = placeholders.toArray(new Integer[placeholders.size()]);
    }


    public String getStatementText(@Nullable Object[] arguments) {
        String statementText = this.statementText;
        for (int i = 0; i < placeholders.length; i++) {
            Integer argumentIndex = placeholders[i];
            Object argumentValue = arguments[argumentIndex];
            statementText = statementText.replaceAll("\\{" + argumentIndex + "\\}", argumentValue.toString());
        }
        return statementText;
    }
}
