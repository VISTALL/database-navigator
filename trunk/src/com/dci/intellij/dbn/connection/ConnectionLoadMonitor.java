package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.common.event.EventManager;
import com.intellij.openapi.Disposable;

public class ConnectionLoadMonitor implements Disposable {
    private ConnectionHandler connectionHandler;
    private boolean loading;

    public ConnectionLoadMonitor(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    private int activeLoaderCount = 0;

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isLoading() {
        return loading || activeLoaderCount > 0;
    }

    public void incrementLoaderCount(){
        activeLoaderCount++;
    }

    public void decrementLoaderCount() {
        activeLoaderCount--;
        if(connectionHandler != null && !connectionHandler.isDisposed() && !connectionHandler.isVirtual()) {
            EventManager.notify(connectionHandler.getProject(), ConnectionLoadListener.TOPIC).contentsLoaded(connectionHandler);
        }
        if (activeLoaderCount == 0) {
            loading = false;
        }
    }

    @Override
    public void dispose() {
        connectionHandler = null;
    }
}
