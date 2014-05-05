package com.dci.intellij.dbn.database.common.statement;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public abstract class StatementDefinition {
    protected String statementText;
    protected Integer[] placeholders;

    private int connectionSignature;
    private int executionTrials;
    private long lastExecutionTimestamp;
    private boolean hasFallback;
    private boolean disabled;

    public StatementDefinition(String statementText, String prefix, boolean hasFallback) {
        this.hasFallback = hasFallback;
        this.statementText = statementText.replaceAll("\\s+", " ").trim();
        if (prefix != null) {
            this.statementText = this.statementText.replaceAll("\\[PREFIX\\]", prefix);
        }
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
