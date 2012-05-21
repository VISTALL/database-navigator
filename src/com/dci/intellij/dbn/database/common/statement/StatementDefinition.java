package com.dci.intellij.dbn.database.common.statement;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatementDefinition {
    
    private String[] segments;
    private Integer[] placeholders;

    private int connectionSignature;
    private int executionTrials;
    private long lastExecutionTimestamp;
    private boolean hasFallback;
    private boolean disabled;

    public StatementDefinition(String statementText, String prefix, boolean hasFallback) {
        this.hasFallback = hasFallback;
        statementText = statementText.trim();
        statementText = StringUtil.replace(statementText, "\n", " ");
        String initialStatement = null;
        while (!statementText.equals(initialStatement)) {
            initialStatement = statementText;
            statementText = StringUtil.replace(statementText, "  ", " ");
        }

        if (prefix != null) {
            statementText = StringUtil.replace(statementText, "[PREFIX]", prefix);
        }

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

    public String createStatement(@Nullable Object[] arguments) {
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

    public boolean canExecute(Connection connection) throws SQLException {
        // do not allow more then three calls
        int newConnectionSignature = createConnectionSignature(connection);
        long currentTimestamp = System.currentTimeMillis();
        boolean allowRetrial = !disabled && !hasFallback && currentTimestamp - lastExecutionTimestamp > 5000;
        if (executionTrials < 3 || newConnectionSignature != connectionSignature || allowRetrial) {
            if (connectionSignature != newConnectionSignature || allowRetrial) {
                connectionSignature = newConnectionSignature;
                lastExecutionTimestamp = currentTimestamp;
                executionTrials = 0;
            }
            return true;
        }
        return false;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void updateExecutionStatus(boolean success) {
        if (success) {
            executionTrials = 0;
        } else {
            executionTrials++;
        }
    }

    private int createConnectionSignature(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        String driver = metaData.getDriverName();
        String url = metaData.getURL();
        String userName = metaData.getUserName();
        return (driver + url + userName).hashCode();
    }
}
