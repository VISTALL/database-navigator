package com.dci.intellij.dbn.database.common.statement;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BasicStatementDefinition extends StatementDefinition{

    private String[] segments;
    private Integer[] placeholders;

    public BasicStatementDefinition(String text, String prefix, boolean hasFallback) {
        super(text, prefix, hasFallback);

        List<String> segments = new ArrayList<String>();
        List<Integer> placeholders = new ArrayList<Integer>();
        int startIndex = statementText.indexOf('{');
        if (startIndex == -1) {
            segments.add(statementText);
        } else {
            int endIndex = 0;
            while (startIndex > -1) {
                String segment = statementText.substring(endIndex, startIndex);
                endIndex = statementText.indexOf('}', startIndex);

                String placeholder = statementText.substring(startIndex + 1, endIndex);
                segments.add(segment);

                placeholders.add(new Integer(placeholder));
                startIndex = statementText.indexOf('{', endIndex);
                endIndex = endIndex + 1;
            }
            if (endIndex < statementText.length()) {
                segments.add(statementText.substring(endIndex));
            }
        }

        this.segments = segments.toArray(new String[segments.size()]);
        this.placeholders = placeholders.toArray(new Integer[placeholders.size()]);
    }


    public String getStatementText(@Nullable Object[] arguments) {
        if (segments.length == 1 && placeholders.length == 0) {
            return segments[0];
        }
        StringBuilder buffer = new StringBuilder();
        for (int i=0; i<segments.length; i++) {
            buffer.append(segments[i]);
            if (i < placeholders.length) {
                int placeholderIndex = placeholders[i];
                buffer.append(arguments[placeholderIndex]);
            }
        }
        return buffer.toString();
    }
}
