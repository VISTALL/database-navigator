package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.connection.config.ConnectionConfig;
import com.dci.intellij.dbn.database.DatabaseInterfaceProvider;
import com.dci.intellij.dbn.database.generic.GenericInterfaceProvider;
import com.dci.intellij.dbn.database.mysql.MySqlInterfaceProvider;
import com.dci.intellij.dbn.database.oracle.OracleInterfaceProvider;

import java.sql.SQLException;

public class DatabaseInterfaceProviderFactory {
    // fixme replace with generic data dictionary
    public static final DatabaseInterfaceProvider GENERIC_INTERFACE_PROVIDER = new GenericInterfaceProvider();
    public static final DatabaseInterfaceProvider ORACLE_INTERFACE_PROVIDER = new OracleInterfaceProvider();
    public static final DatabaseInterfaceProvider MYAQL_INTERFACE_PROVIDER = new MySqlInterfaceProvider();

    public static DatabaseInterfaceProvider createInterfaceProvider(ConnectionHandler connectionHandler) throws SQLException {
        DatabaseType databaseType = null;
        if (connectionHandler != null && connectionHandler.isVirtual()) {
            databaseType = connectionHandler.getDatabaseType();
        } else {
            ConnectionConfig connectionConfig = connectionHandler.getSettings().getDatabaseSettings();
            databaseType = connectionConfig.getDatabaseType();
            if (databaseType == null) {
                try {
                    databaseType = ConnectionUtil.getDatabaseType(connectionHandler.getStandaloneConnection());
                    connectionConfig.setDatabaseType(databaseType);
                } catch (SQLException e) {
                    connectionConfig.setDatabaseType(DatabaseType.UNKNOWN);
                    throw e;
                }
            }
        }


        if (DatabaseType.ORACLE.equals(databaseType)) {
            return ORACLE_INTERFACE_PROVIDER;
        } else if (DatabaseType.MYSQL.equals(databaseType)) {
            return MYAQL_INTERFACE_PROVIDER;
        }
        return GENERIC_INTERFACE_PROVIDER;

    }
}
