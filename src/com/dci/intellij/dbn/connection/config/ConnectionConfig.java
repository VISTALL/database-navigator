package com.dci.intellij.dbn.connection.config;

import com.dci.intellij.dbn.common.options.PersistentConfiguration;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.ConnectivityStatus;
import com.dci.intellij.dbn.connection.DatabaseType;
import com.intellij.openapi.options.UnnamedConfigurable;
import org.jetbrains.annotations.NotNull;

public interface ConnectionConfig extends PersistentConfiguration, UnnamedConfigurable {
    boolean isNew();

    boolean isActive();

    boolean isOsAuthentication();

    @NotNull
    String getId();

    String getName();

    String getDescription();

    String getDriverLibrary();

    String getDriver();

    String getUrl();

    DatabaseType getDatabaseType();

    String getUser();

    String getPassword();

    String getConnectionDetails();

    ConnectionManager getConnectionManager();

    void setDatabaseType(DatabaseType databaseType);

    ConnectivityStatus getConnectivityStatus();

    void setConnectivityStatus(ConnectivityStatus connectivityStatus);
}
