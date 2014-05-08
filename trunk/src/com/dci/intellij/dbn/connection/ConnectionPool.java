package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.common.Constants;
import com.dci.intellij.dbn.common.TimeUtil;
import com.dci.intellij.dbn.common.event.EventManager;
import com.dci.intellij.dbn.common.notification.NotificationUtil;
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
    private int peakSize = 0;
    private boolean isDisposed;

    protected final Logger log = Logger.getInstance(getClass().getName());
    private ConnectionHandler connectionHandler;

    private Vector<ConnectionWrapper> poolConnections = new Vector<ConnectionWrapper>();
    private Vector<ConnectionWrapper> busyPoolConnections = new Vector<ConnectionWrapper>();
    private ConnectionWrapper standaloneConnection;
    private Timer poolCleaner;

    public ConnectionPool(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        poolCleaner = new Timer("Connection pool cleaner [" + connectionHandler.getName() + "]");
        poolCleaner.schedule(new PoolCleanerTask(), TimeUtil.ONE_MINUTE, TimeUtil.ONE_MINUTE);
    }

    public Connection getStandaloneConnection(boolean recover) throws SQLException {
        synchronized (this) {
            if (standaloneConnection != null) {
                if (recover && !standaloneConnection.isValid()) {
                    standaloneConnection = null;
                }
            }

            if (standaloneConnection == null) {
                try {
                    Connection connection = ConnectionUtil.connect(connectionHandler);
                    standaloneConnection = new ConnectionWrapper(connection);
                    NotificationUtil.sendInfoNotification(
                            connectionHandler.getProject(),
                            Constants.DBN_TITLE_PREFIX + "Connected",
                            "Connected to database \"{0}\"",
                            connectionHandler.getName());
                } finally {
                    notifyStatusChange();
                }
            }

            return standaloneConnection.getConnection();
        }
    }

    private void notifyStatusChange() {
        ConnectionStatusListener changeListener = EventManager.notify(connectionHandler.getProject(), ConnectionStatusListener.TOPIC);
        changeListener.statusChanged(connectionHandler.getId());
    }

    public Connection allocateConnection() throws SQLException {
        synchronized (this) {
            ConnectionStatus connectionStatus = connectionHandler.getConnectionStatus();
            while (poolConnections.size() > 0) {
                ConnectionWrapper connectionWrapper = poolConnections.remove(0);
                if (connectionWrapper.isValid()) {
                    connectionStatus.setConnected(true);
                    connectionStatus.setValid(true);
                    busyPoolConnections.add(connectionWrapper);
                    return connectionWrapper.getConnection();
                }
            }

            logger.debug("[DBN-INFO] Attempt to create new pool connection for '" + connectionHandler.getName() + "'");
            Connection connection = ConnectionUtil.connect(connectionHandler);

            //connectionHandler.getConnectionBundle().notifyConnectionStatusListeners(connectionHandler);

            // pool connections do not need to have current schema set
            //connectionHandler.getDataDictionary().setCurrentSchema(connectionHandler.getCurrentSchemaName(), connection);
            ConnectionWrapper connectionWrapper = new ConnectionWrapper(connection);
            busyPoolConnections.add(connectionWrapper);
            int size = busyPoolConnections.size();
            if (size > peakSize) peakSize = size;
            logger.debug("[DBN-INFO] Pool connection for '" + connectionHandler.getName() + "' created. Pool size = " + getSize());
            return connection;
        }
    }

    public void releaseConnection(Connection connection) {
        synchronized (this) {
            if (connection != null) {
                for (ConnectionWrapper connectionWrapper : busyPoolConnections) {
                    if (connectionWrapper.getConnection() == connection) {
                        busyPoolConnections.remove(connectionWrapper);
                        poolConnections.add(connectionWrapper);
                        break;
                    }
                }
            }
        }
    }

    public synchronized void closeConnectionsSilently() {
        synchronized (this) {
            while (poolConnections.size() > 0) {
                ConnectionWrapper connectionWrapper = poolConnections.remove(0);
                connectionWrapper.closeConnection();
            }

            if (standaloneConnection != null) {
                standaloneConnection.closeConnection();
                standaloneConnection = null;
            }
        }
    }

    public synchronized void closeConnections() throws SQLException {
        synchronized (this) {
            SQLException exception = null;
            while (poolConnections.size() > 0) {
                ConnectionWrapper connectionWrapper = poolConnections.remove(0);
                try {
                    connectionWrapper.getConnection().close();
                } catch (SQLException e) {
                    exception = e;
                }
            }

            if (standaloneConnection != null) {
                try {
                    standaloneConnection.getConnection().close();
                } catch (SQLException e) {
                    exception = e;
                }
                standaloneConnection = null;
            }
            if (exception != null) {
                throw exception;
            }
        }
    }

    public int getIdleMinutes() {
        return standaloneConnection == null ? 0 : standaloneConnection.getIdleMinutes();
    }

    public void keepAlive(boolean check) {
        if (standaloneConnection != null) {
            if (check) standaloneConnection.isValid();
            standaloneConnection.keepAlive();
        }
    }

    public int getSize() {
        return busyPoolConnections.size();
    }

    public int getPeakSize() {
        return peakSize;
    }

    public void dispose() {
        if (!isDisposed) {
            isDisposed = true;
            poolCleaner.cancel();
            poolCleaner.purge();
            closeConnectionsSilently();
            connectionHandler = null;
        }
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if (standaloneConnection != null && !standaloneConnection.isClosed()) {
            standaloneConnection.setAutoCommit(autoCommit);
        }

        for (ConnectionWrapper connection : poolConnections) {
            if (!connection.isClosed()) {
                connection.setAutoCommit(autoCommit);
            }
        }
    }

    private class PoolCleanerTask extends TimerTask {
        public void run() {
            // close connections only if pool is passive
            synchronized (ConnectionPool.this) {
                if (busyPoolConnections.isEmpty() && !poolConnections.isEmpty()) {
                    for (ConnectionWrapper connectionWrapper : poolConnections) {
                        connectionWrapper.closeConnection();
                    }
                    poolConnections.clear();
                    logger.debug("[DBN-INFO] Clearing pool for connection '" + connectionHandler.getName() + "'");
                }
            }
        }
    }


    private class ConnectionWrapper {
        private Connection connection;
        private long lastCheckTimestamp;
        private long lastAccessTimestamp;
        private boolean isValid;

        public ConnectionWrapper(Connection connection) {
            this.connection = connection;
            long currentTimeMillis = System.currentTimeMillis();
            lastCheckTimestamp = currentTimeMillis;
            lastAccessTimestamp = currentTimeMillis;
        }

        public boolean isValid() {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastCheckTimestamp > 10000) {
                lastCheckTimestamp = currentTimeMillis;
                DatabaseMetadataInterface metadataInterface = connectionHandler.getInterfaceProvider().getMetadataInterface();
                isValid = metadataInterface.isValid(connection);
                return isValid;
            }
            return isValid;
        }

        public int getIdleMinutes() {
            long idleTimeMillis = System.currentTimeMillis() - lastAccessTimestamp;
            return (int) (idleTimeMillis / TimeUtil.ONE_MINUTE);
        }

        public Connection getConnection() {
            lastAccessTimestamp = System.currentTimeMillis();
            return connection;
        }

        public void closeConnection() {
            ConnectionUtil.closeConnection(connection);
        }

        public void setAutoCommit(boolean autoCommit) throws SQLException {
            connection.setAutoCommit(autoCommit);
        }

        public boolean isClosed() throws SQLException {
            return connection.isClosed();
        }

        public void keepAlive() {
            lastAccessTimestamp = System.currentTimeMillis();
        }
    }
}
