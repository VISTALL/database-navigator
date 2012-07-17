package com.dci.intellij.dbn.browser.model;

import com.dci.intellij.dbn.connection.ConnectionHandler;

public class TabbedBrowserTreeModel extends BrowserTreeModel {
    private ConnectionHandler connectionHandler;

    public TabbedBrowserTreeModel(ConnectionHandler connectionHandler) {
        super(connectionHandler.getObjectBundle());
        this.connectionHandler = connectionHandler;
    }

    @Override
    public boolean contains(BrowserTreeNode node) {
        return connectionHandler == node.getConnectionHandler();
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }
}
