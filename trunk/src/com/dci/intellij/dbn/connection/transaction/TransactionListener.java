package com.dci.intellij.dbn.connection.transaction;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.util.messages.Topic;

import java.sql.SQLException;
import java.util.EventListener;

public interface TransactionListener extends EventListener{
    public static final Topic<TransactionListener> TOPIC = Topic.create("Transaction event fired", TransactionListener.class);

    /**
     * This is typically called before the connection has been operationally committed
     * @param connectionHandler
     * @param action
     */
    void beforeAction(ConnectionHandler connectionHandler, TransactionAction action) throws SQLException;

    /**
     * This is typically called after the connection has been operationally committed
     * @param connectionHandler indicates if the commit operation was successful or not
     * @param succeeded indicates if the commit operation was successful or not
     */
    void afterAction(ConnectionHandler connectionHandler, TransactionAction action, boolean succeeded) throws SQLException;

}
