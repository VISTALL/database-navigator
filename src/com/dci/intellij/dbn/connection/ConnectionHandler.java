package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.common.environment.EnvironmentType;
import com.dci.intellij.dbn.common.filter.Filter;
import com.dci.intellij.dbn.connection.config.ConnectionSettings;
import com.dci.intellij.dbn.database.DatabaseInterfaceProvider;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.navigation.psi.NavigationPsiCache;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.DBObjectBundle;
import com.dci.intellij.dbn.vfs.SQLConsoleFile;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;

import javax.swing.Icon;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionHandler extends Disposable{
    Project getProject();
    Module getModule();
    Connection getPoolConnection() throws SQLException;
    Connection getPoolConnection(DBSchema schema) throws SQLException;
    Connection getStandaloneConnection() throws SQLException;
    Connection getStandaloneConnection(DBSchema schema) throws SQLException;
    void freePoolConnection(Connection connection);
    void closePoolConnection(Connection connection);
    ConnectionSettings getSettings();
    ConnectionStatus getConnectionStatus();
    ConnectionManager getConnectionManager();
    ConnectionInfo getConnectionInfo() throws SQLException;
    ConnectionPool getConnectionPool();
    DatabaseInterfaceProvider getInterfaceProvider();
    DBObjectBundle getObjectBundle();
    DBSchema getUserSchema();
    SQLConsoleFile getSQLConsoleFile();

    ConnectionInfo testConnectivity(boolean showMessageDialog);
    boolean isValid(boolean check);
    boolean isValid();
    boolean isVirtual();
    boolean isAutoCommit();
    void setAutoCommit(boolean autoCommit) throws SQLException;
    void disconnect();

    String getId();
    String getUserName();
    String getPresentableText();
    String getQualifiedName();
    String getName();
    String getDescription();
    Icon getIcon();

    LoadMonitor getLoadMonitor();

    void addTransactionListener(TransactionListener transactionListener);
    void removeTransactionListener(TransactionListener transactionListener);
    void notifyOpenTransactions(boolean openTransactions);
    boolean hasOpenTransactions();
    void commit() throws SQLException;
    void rollback() throws SQLException;
    DBLanguageDialect getLanguageDialect(DBLanguage language);

    boolean isActive();
    DatabaseType getDatabaseType();

    Filter<BrowserTreeElement> getObjectFilter();

    NavigationPsiCache getPsiCache();
    EnvironmentType getEnvironmentType();
}