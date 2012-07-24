package com.dci.intellij.dbn.connection;

import java.sql.SQLException;

public enum ConnectionOperation {
    COMMIT(
            "Commit",
            "Connection \"{0}\" successfully committed",
            "Commit on connection \"{0}\" failed. Cause: {1}",
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
            new Executor() {
                @Override
                void execute(ConnectionHandler connectionHandler) throws SQLException {
                    connectionHandler.rollback();
                }
            }),
    DISCONNECT("Disconnect",
            "Successfully disconnected from \"{0}\"",
            "Error disconnecting from \"{0}\". Cause: {1}",
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

    private ConnectionOperation(String name, String successMessage, String failureMessage, Executor executor) {
        this.name = name;
        this.successMessage = successMessage;
        this.failureMessage = failureMessage;
        this.executor = executor;
    }

    private ConnectionOperation(String name, Executor executor) {
        this.name = name;
        this.executor = executor;
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

    private abstract static class Executor {
        abstract void execute(ConnectionHandler connectionHandler) throws SQLException;
    }

    public void execute(ConnectionHandler connectionHandler) throws SQLException {
        executor.execute(connectionHandler);
    }

}
