package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.database.DatabaseMetadataInterface;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class ConnectionPool implements Disposable {
    private Logger logger = Logger.getInstance(getClass().getName());
    private static final long ONE_MINUTE = 1000*60;
    private int peakSize = 0;

    protected final Logger log = Logger.getInstance(getClass().getName());
    private ConnectionHandler connectionHandler;

    private Vector<ConnectionWrapper> connections = new Vector<ConnectionWrapper>();
    private Vector<ConnectionWrapper> allocatedConnections = new Vector<ConnectionWrapper>();
    private Connection standaloneConnection;

    public ConnectionPool(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        Timer cleaner = new Timer("Connection pool cleaner [" + connectionHandler.getName() + "]");
        cleaner.schedule(new PoolCleanerTask(this), ONE_MINUTE, ONE_MINUTE);
    }

    public Connection getStandaloneConnection(boolean recover) throws SQLException {
        if (standaloneConnection == null) {
            return createStandaloneConnection();
        }
        DatabaseMetadataInterface metadataInterface = connectionHandler.getInterfaceProvider().getMetadataInterface();
        if (recover && !metadataInterface.isValid(standaloneConnection)) {
            return createStandaloneConnection();
        }
        return standaloneConnection;
    }

    private Connection createStandaloneConnection() throws SQLException {
        try {
            standaloneConnection = ConnectionUtil.connect(
                    connectionHandler.getSettings().getDatabaseSettings(),
                    connectionHandler.getSettings().getDetailSettings().getProperties(),
                    connectionHandler.getConnectionStatus());
            return standaloneConnection;
            //connectionHandler.getDataDictionary().setCurrentSchema(connectionHandler.getCurrentSchemaName(), standaloneConnection);
        } finally {
            //connectionHandler.getConnectionManager().notifyConnectionStatusListeners(connectionHandler);
        }

    }

    public synchronized Connection allocateConnection() throws SQLException {
            ConnectionStatus connectionStatus = connectionHandler.getConnectionStatus();
        while (connections.size() > 0) {
            ConnectionWrapper connectionWrapper = connections.remove(0);
            if (connectionWrapper.isValid()) {
                connectionStatus.setConnected(true);
                connectionStatus.setValid(true);
                allocatedConnections.add(connectionWrapper);
                return connectionWrapper.getConnection();
            }
        }

        logger.debug("[DBN-INFO] Attempt to create new pool connection for '" + connectionHandler.getName() + "'");
        Connection connection = ConnectionUtil.connect(
                connectionHandler.getSettings().getDatabaseSettings(),
                connectionHandler.getSettings().getDetailSettings().getProperties(), connectionStatus);

        //connectionHandler.getConnectionManager().notifyConnectionStatusListeners(connectionHandler);

        // pool connections do not need to have current schema set
        //connectionHandler.getDataDictionary().setCurrentSchema(connectionHandler.getCurrentSchemaName(), connection);
        ConnectionWrapper connectionWrapper = new ConnectionWrapper(connectionHandler.getInterfaceProvider().getMetadataInterface(), connection);
        allocatedConnections.add(connectionWrapper);
        int size = allocatedConnections.size();
        if (size > peakSize) peakSize = size;
        logger.debug("[DBN-INFO] Pool connection for '" + connectionHandler.getName() + "' created. Pool size = " + getSize());
        return connection;
    }

    public synchronized void releaseConnection(Connection connection) {
        if (connection != null) {
            for (ConnectionWrapper connectionWrapper : allocatedConnections) {
                if (connectionWrapper.getConnection() == connection) {
                    allocatedConnections.remove(connectionWrapper);
                    connections.add(connectionWrapper);
                    break;
                }
            }

        }
    }

    public synchronized void disposedConnection(Connection connection) {
        if (connection != null) {
            for (ConnectionWrapper connectionWrapper : allocatedConnections) {
                if (connectionWrapper.getConnection() == connection) {
                    allocatedConnections.remove(connectionWrapper);
                    ConnectionUtil.closeConnection(connection);
                    break;
                }
            }

        }
    }

    public synchronized void closeConnections() {
        while (connections.size() > 0) {
            ConnectionWrapper connectionWrapper = connections.remove(0);
            ConnectionUtil.closeConnection(connectionWrapper.getConnection());
        }
        ConnectionUtil.closeConnection(standaloneConnection);

        connectionHandler.getConnectionStatus().setConnected(false);
        //connectionHandler.getConnectionManager().notifyConnectionStatusListeners(connectionHandler);
    }

    public int getSize() {
        return allocatedConnections.size();
    }

    public int getPeakSize() {
        return peakSize;
    }

    public void dispose() {

    }

    private class PoolCleanerTask extends TimerTask {
        ConnectionPool connectionPool;
        private PoolCleanerTask(ConnectionPool connectionPool) {
            this.connectionPool = connectionPool;
        }

        public void run() {
            // close connections only if pool is passive
            if (connectionPool.allocatedConnections.isEmpty() && !connectionPool.connections.isEmpty()) {
                try {
                    for (ConnectionWrapper connectionWrapper : connectionPool.connections) {
                        ConnectionUtil.closeConnection(connectionWrapper.getConnection());
                    }
                } catch (Exception e) {

                }
                connectionPool.connections.clear();
                logger.debug("[DBN-INFO] Clearing pool for connection '" + connectionHandler.getName() + "'");
            }
        }
    }


    private class ConnectionWrapper {
        private DatabaseMetadataInterface metadataInterface;
        private Connection connection;
        private long lastCheckTimestamp;

        public ConnectionWrapper(DatabaseMetadataInterface metadataInterface, Connection connection) {
            this.metadataInterface = metadataInterface;
            this.connection = connection;
        }

        public boolean isValid() {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastCheckTimestamp > 5000) {
                lastCheckTimestamp = currentTimeMillis;
                return metadataInterface.isValid(connection);
            }
            return true;
        }

        public Connection getConnection() {
            return connection;
        }
    }
}
