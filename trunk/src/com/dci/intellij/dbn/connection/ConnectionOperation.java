package com.dci.intellij.dbn.connection;

import java.sql.SQLException;

public enum ConnectionOperation {
    COMMIT("Commit", new Executor() {
        @Override
        void execute(ConnectionHandler connectionHandler) throws SQLException {
            connectionHandler.commit();
        }
    }),
    ROLLBACK("Rollback", new Executor() {
        @Override
        void execute(ConnectionHandler connectionHandler) throws SQLException {
            connectionHandler.rollback();
        }
    }),
    DISCONNECT("Disconnect", new Executor() {
        @Override
        void execute(ConnectionHandler connectionHandler) throws SQLException {
            connectionHandler.disconnect();
        }
    }),

    TOGGLE_AUTO_COMMIT("Toggle Auto-Commit", new Executor() {
        @Override
        void execute(ConnectionHandler connectionHandler) throws SQLException {
            boolean isAutoCommit = connectionHandler.isAutoCommit();
            connectionHandler.setAutoCommit(!isAutoCommit);
        }
    });


    private String name;
    private Executor executor;

    private ConnectionOperation(String name, Executor executor) {
        this.name = name;
        this.executor = executor;
    }

    public String getName() {
        return name;
    }

    private abstract static class Executor {
        abstract void execute(ConnectionHandler connectionHandler) throws SQLException;
    }

    public void execute(ConnectionHandler connectionHandler) throws SQLException {
        executor.execute(connectionHandler);
    }

}
