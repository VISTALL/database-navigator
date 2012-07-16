package com.dci.intellij.dbn.connection.transaction;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.util.messages.Topic;

import java.sql.SQLException;
import java.util.EventListener;

public interface TransactionListener extends EventListener{
    public static final Topic<TransactionListener> TOPIC = Topic.create("Transaction event fired", TransactionListener.class);
    int COMMIT = 0;
    int ROLLBACK = 1;

    /**
     * This is typically called before the connection has been operationally committed
     * @param connectionHandler
     */
    void beforeCommit(ConnectionHandler connectionHandler) throws SQLException;

    /**
     * This is typically called before the connection has been operationally rolledback
     * @param connectionHandler
     */
    void beforeRollback(ConnectionHandler connectionHandler) throws SQLException;

    /**
     * This is typically called after the connection has been operationally committed
     * @param connectionHandler indicates if the commit operation was successful or not
     * @param succeeded indicates if the commit operation was successful or not
     */
    void afterCommit(ConnectionHandler connectionHandler, boolean succeeded) throws SQLException;

    /**
     * This is typically called after the connection has been operationally rolledback
     * @param succeeded indicates if the rollback operation was successful or not
     */
    void afterRollback(ConnectionHandler connectionHandler, boolean succeeded) throws SQLException;
}
