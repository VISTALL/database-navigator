package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.connection.config.ConnectionConfig;
import com.dci.intellij.dbn.connection.config.ConnectionDatabaseSettings;
import com.dci.intellij.dbn.connection.config.ConnectionDetailSettings;
import com.dci.intellij.dbn.connection.config.ConnectionSettings;
import com.dci.intellij.dbn.driver.DatabaseDriverManager;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.Map;
import java.util.Properties;

public class ConnectionUtil {
    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                Statement statement = resultSet.getStatement();
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
            try {
                resultSet.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeConnection(Connection connection)  {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }

    public static Connection connect(ConnectionHandler connectionHandler) throws SQLException {
        ConnectionStatus connectionStatus = connectionHandler.getConnectionStatus();
        ConnectionSettings connectionSettings = connectionHandler.getSettings();
        ConnectionDatabaseSettings databaseSettings = connectionSettings.getDatabaseSettings();
        ConnectionDetailSettings detailSettings = connectionSettings.getDetailSettings();
        return connect(databaseSettings, detailSettings.getProperties(), detailSettings.isAutoCommit(), connectionStatus);
    }

    public static Connection connect(ConnectionConfig connectionConfig, @Nullable Map<String, String> connectionProperties, boolean autoCommit, @Nullable ConnectionStatus connectionStatus) throws SQLException {
        try {
            Driver driver = DatabaseDriverManager.getInstance().getDriver(
                    connectionConfig.getDriverLibrary(),
                    connectionConfig.getDriver());

            Properties properties = new Properties();
            if (!connectionConfig.isOsAuthentication()) {
                properties.put("user", connectionConfig.getUser());
                properties.put("password", connectionConfig.getPassword());
            }
            if (connectionProperties != null) {
                properties.putAll(connectionProperties);
            }

            Connection connection = driver.connect(connectionConfig.getUrl(), properties);
            if (connection == null) {
                throw new SQLException("Unknown reason.");
            }
            connection.setAutoCommit(autoCommit);
            if (connectionStatus != null) {
                connectionStatus.setStatusMessage(null);
                connectionStatus.setConnected(true);
                connectionStatus.setValid(true);
            }

            DatabaseType databaseType = connectionConfig.getDatabaseType();
            if (databaseType == null || databaseType.equals(DatabaseType.UNKNOWN)) {
                databaseType = getDatabaseType(connection);
                connectionConfig.setDatabaseType(databaseType);
            }

            connectionConfig.setConnectivityStatus(ConnectivityStatus.VALID);

            return connection;
        } catch (Exception e) {
            connectionConfig.setConnectivityStatus(ConnectivityStatus.INVALID);
            if (connectionStatus != null) {
                connectionStatus.setStatusMessage(e.getMessage());
                connectionStatus.setConnected(false);
                connectionStatus.setValid(false);
            }
            if (e instanceof SQLException)
                throw (SQLException) e;  else
                throw new SQLException(e.getMessage());
        }
    }

    public static DatabaseType getDatabaseType(Connection connection) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        String productName = databaseMetaData.getDatabaseProductName();
        if (productName.toUpperCase().contains("ORACLE")) {
            return DatabaseType.ORACLE;
        } else if (productName.toUpperCase().contains("MYSQL")) {
            return DatabaseType.MYSQL;
        }
        return DatabaseType.UNKNOWN;
    }
}
