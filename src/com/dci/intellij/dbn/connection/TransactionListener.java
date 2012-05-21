package com.dci.intellij.dbn.connection;

import java.sql.SQLException;

public interface TransactionListener {
    int COMMIT = 0;
    int ROLLBACK = 1;

    /**
     * This is typically called before the connection has been operationally committed
     */
    void beforeCommit() throws SQLException;

    /**
     * This is typically called before the connection has been operationally rolledback
     */
    void beforeRollback() throws SQLException;

    /**
     * This is typically called after the connection has been operationally committed
     * @param succeeded indicates if the commit operation was successful or not
     */
    void afterCommit(boolean succeeded) throws SQLException;

    /**
     * This is typically called after the connection has been operationally rolledback
     * @param succeeded indicates if the rollback operation was successful or not
     */
    void afterRollback(boolean succeeded) throws SQLException;
}
