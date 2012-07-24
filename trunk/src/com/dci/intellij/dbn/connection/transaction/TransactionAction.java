package com.dci.intellij.dbn.connection.transaction;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.notification.NotificationType;

import java.sql.SQLException;

public enum TransactionAction {
    COMMIT(
            "Commit",
            "Connection \"{0}\" committed",
            "Error committing connection \"{0}\". Details: {1}",
            false,
            new Executor() {
                @Override
                void execute(ConnectionHandler connectionHandler) throws SQLException {
                    connectionHandler.commit();
                }
            }),
    ROLLBACK(
            "Rollback",
            "Connection \"{0}\" rolled back.",
            "Error rolling back connection \"{0}\". Details: {1}",
            false,
            new Executor() {
                @Override
                void execute(ConnectionHandler connectionHandler) throws SQLException {
                    connectionHandler.rollback();
                }
            }),
    DISCONNECT("Disconnect",
            "Disconnected from \"{0}\"",
            "Error disconnecting from \"{0}\". Details: {1}",
            true,
            new Executor() {
                @Override
                void execute(ConnectionHandler connectionHandler) throws SQLException {
                    connectionHandler.disconnect();
                }
            }),

    TURN_AUTO_COMMIT_ON(
            "Auto-Commit",
            "Auto-Commit turned ON for \"{0}\".", NotificationType.WARNING,
            "Error turning Auto-Commit ON for \"{0}\". Details: {1}",
            true,
            new Executor() {
        @Override
        void execute(ConnectionHandler connectionHandler) throws SQLException {
            assert !connectionHandler.isAutoCommit();
            connectionHandler.setAutoCommit(true);
        }
    }),

    TURN_AUTO_COMMIT_OFF(
            "Auto-Commit",
            "Auto-Commit turned OFF for \"{0}\".",
            "Error turning Auto-Commit OFF for \"{0}\". Details: {1}",
            true,
            new Executor() {
        @Override
        void execute(ConnectionHandler connectionHandler) throws SQLException {
            assert connectionHandler.isAutoCommit();
            connectionHandler.setAutoCommit(false);
        }
    });


    private String name;
    private String successNotificationMessage;
    private String errorNotificationMessage;
    private NotificationType successNotificationType = NotificationType.INFORMATION;
    private Executor executor;
    private boolean isStatusChange;

    private TransactionAction(String name, String successNotificationMessage, String errorNotificationMessage, boolean isStatusChange, Executor executor) {
        this(name, successNotificationMessage, null, errorNotificationMessage, isStatusChange, executor);

    }
    private TransactionAction(String name, String successNotificationMessage, NotificationType successNotificationType, String errorNotificationMessage, boolean isStatusChange, Executor executor) {
        this.name = name;
        this.errorNotificationMessage = errorNotificationMessage;
        this.successNotificationMessage = successNotificationMessage;
        this.executor = executor;
        this.isStatusChange = isStatusChange;
        if (successNotificationType != null){
            this.successNotificationType = successNotificationType;
        }
    }

    public String getName() {
        return name;
    }

    public String getSuccessNotificationMessage() {
        return successNotificationMessage;
    }

    public String getErrorNotificationMessage() {
        return errorNotificationMessage;
    }

    public NotificationType getSuccessNotificationType() {
        return successNotificationType;
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
