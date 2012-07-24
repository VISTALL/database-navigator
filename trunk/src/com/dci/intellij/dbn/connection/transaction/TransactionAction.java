package com.dci.intellij.dbn.connection.transaction;

import com.dci.intellij.dbn.connection.ConnectionHandler;

import java.sql.SQLException;

public enum TransactionAction {
    COMMIT(
            "Commit",
            "Connection \"{0}\" successfully committed",
            "Commit on connection \"{0}\" failed. Cause: {1}",
            false,
            new Executor() {
                @Override
                void execute(ConnectionHandler connectionHandler) throws SQLException {
                    connectionHandler.commit();
                }
            }),
    ROLLBACK(
            "Rollback",
            "Connection \"{0}\" successfully rolled back.",
            "Rollback on connection \"{0}\" failed. Cause: {1}",
            false,
            new Executor() {
                @Override
                void execute(ConnectionHandler connectionHandler) throws SQLException {
                    connectionHandler.rollback();
                }
            }),
    DISCONNECT("Disconnect",
            "Successfully disconnected from \"{0}\"",
            "Error disconnecting from \"{0}\". Cause: {1}",
            true,
            new Executor() {
                @Override
                void execute(ConnectionHandler connectionHandler) throws SQLException {
                    connectionHandler.disconnect();
                }
            }),

    TOGGLE_AUTO_COMMIT(
            "Toggle Auto-Commit",
            "Successfully switched Auto-Commit option for \"{0}\".",
            "Could not switch Auto-Commit option for \"{0}\". Cause: {1}",
            true,
            new Executor() {
        @Override
        void execute(ConnectionHandler connectionHandler) throws SQLException {
            boolean isAutoCommit = connectionHandler.isAutoCommit();
            connectionHandler.setAutoCommit(!isAutoCommit);
        }
    });


    private String name;
    private String successMessage;
    private String failureMessage;
    private Executor executor;
    private boolean isStatusChange;

    private TransactionAction(String name, String successMessage, String failureMessage, boolean isStatusChange, Executor executor) {
        this.name = name;
        this.successMessage = successMessage;
        this.failureMessage = failureMessage;
        this.executor = executor;
        this.isStatusChange = isStatusChange;
    }

    public String getName() {
        return name;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public boolean isStatusChange() {
        return isStatusChange;
    }

    private abstract static class Executor {
        abstract void execute(ConnectionHandler connectionHandler) throws SQLException;
    }

    public void execute(ConnectionHandler connectionHandler) throws SQLException {
        executor.execute(connectionHandler);
    }

}
