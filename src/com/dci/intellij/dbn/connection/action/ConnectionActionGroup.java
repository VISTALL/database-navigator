package com.dci.intellij.dbn.connection.action;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

public class ConnectionActionGroup extends DefaultActionGroup {

    public ConnectionActionGroup(ConnectionHandler connectionHandler) {
        //add(new ConnectAction(connectionHandler));
        add(new TransactionCommitAction(connectionHandler));
        add(new TransactionRollbackAction(connectionHandler));
        add(new ToggleAutoCommitAction(connectionHandler));
        add(new OpenSQLConsoleAction(connectionHandler));
        addSeparator();
        add(new ShowDatabaseInformationAction(connectionHandler));
        add(new DisconnectAction(connectionHandler));
        add(new TestConnectivityAction(connectionHandler));
        addSeparator();
        add(new OpenConnectionSettingsAction(connectionHandler));
    }
}
