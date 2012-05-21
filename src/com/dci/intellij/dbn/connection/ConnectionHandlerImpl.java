package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.dispose.DisposeUtil;
import com.dci.intellij.dbn.common.environment.EnvironmentType;
import com.dci.intellij.dbn.common.filter.Filter;
import com.dci.intellij.dbn.common.thread.BackgroundTask;
import com.dci.intellij.dbn.common.ui.tree.TreeUtil;
import com.dci.intellij.dbn.connection.config.ConnectionSettings;
import com.dci.intellij.dbn.database.DatabaseInterfaceProvider;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.navigation.psi.NavigationPsiCache;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.DBObjectBundle;
import com.dci.intellij.dbn.object.common.DBObjectBundleImpl;
import com.dci.intellij.dbn.vfs.SQLConsoleFile;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConnectionHandlerImpl implements ConnectionHandler {
    private Set<TransactionListener> transactionListeners = new HashSet<TransactionListener>();

    private ConnectionSettings connectionSettings;
    private ConnectionManager connectionManager;
    private ConnectionStatus connectionStatus;
    private ConnectionPool connectionPool;
    private ConnectionInfo connectionInfo;
    private DBObjectBundle objectBundle;
    private DatabaseInterfaceProvider interfaceProvider;

    LoadMonitor loadMonitor = new LoadMonitor();
    private boolean hasOpenTransactions;
    private boolean isDisposed;

    private SQLConsoleFile sqlConsoleFile;
    private NavigationPsiCache psiCache = new NavigationPsiCache(this);

    public ConnectionHandlerImpl(ConnectionManager connectionManager, ConnectionSettings connectionSettings) {
        this.connectionManager = connectionManager;
        this.connectionSettings = connectionSettings;
        connectionStatus = new ConnectionStatus();
        connectionPool = new ConnectionPool(this);
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public ConnectionSettings getSettings() {
        return connectionSettings;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public boolean isActive() {
        return connectionSettings.getDatabaseSettings().isActive();
    }

    public DatabaseType getDatabaseType() {
        return connectionSettings.getDatabaseSettings().getDatabaseType();
    }

    public Filter<BrowserTreeElement> getObjectFilter() {
        return getSettings().getFilterSettings().getObjectTypeFilterSettings().getElementFilter();
    }

    public LoadMonitor getLoadMonitor() {
        return loadMonitor;
    }

    public SQLConsoleFile getSQLConsoleFile() {
        if (sqlConsoleFile == null) {
            sqlConsoleFile = new SQLConsoleFile(this);
        }
        return sqlConsoleFile;
    }

    @Override
    public NavigationPsiCache getPsiCache() {
        return psiCache;
    }

    @Override
    public EnvironmentType getEnvironmentType() {
        return getSettings().getDetailSettings().getEnvironmentType();
    }

    public void addTransactionListener(TransactionListener transactionListener) {
        transactionListeners.add(transactionListener);
    }

    public void removeTransactionListener(TransactionListener transactionListener) {
        transactionListeners.remove(transactionListener);
    }

    private void notifyPreCommitRollback(int operation) throws SQLException {
        for (TransactionListener transactionListener : transactionListeners) {
            switch(operation) {
                case TransactionListener.COMMIT : transactionListener.beforeCommit(); break;
                case TransactionListener.ROLLBACK : transactionListener.beforeRollback(); break;
            }
        }
    }

    private void notifyPostCommitRollback(int operation, boolean successful) throws SQLException {
        for (TransactionListener transactionListener : transactionListeners) {
            switch(operation) {
                case TransactionListener.COMMIT : transactionListener.afterCommit(successful); break;
                case TransactionListener.ROLLBACK : transactionListener.afterRollback(successful); break;
            }
        }
    }

    public boolean hasOpenTransactions() {
        return hasOpenTransactions;
    }

    public void commit() throws SQLException {
        boolean success = true;
        try {
            notifyPreCommitRollback(TransactionListener.COMMIT);
            connectionPool.getStandaloneConnection(false).commit();
            hasOpenTransactions = false;
        } catch (SQLException e) {
            success = false;
            throw e;
        } finally {
            notifyPostCommitRollback(TransactionListener.COMMIT, success);
        }
    }

    public void rollback() throws SQLException {
        boolean success = true;
        try {
            notifyPreCommitRollback(TransactionListener.ROLLBACK);
            connectionPool.getStandaloneConnection(false).rollback();
            hasOpenTransactions = false;
        } catch (SQLException e) {
            success = false;
            throw e;
        } finally {
            notifyPostCommitRollback(TransactionListener.ROLLBACK, success);
        }
    }

    public void notifyOpenTransactions(boolean openTransactions) {
        hasOpenTransactions = openTransactions;
    }

    public String toString() {
        return getPresentableText();
    }

    public Project getProject() {
        return connectionManager.getProject();
    }

    public Module getModule() {
        if (connectionManager instanceof ModuleConnectionManager) {
            ModuleConnectionManager moduleConnectionManager = (ModuleConnectionManager) connectionManager;
            return moduleConnectionManager.getModule();
        }
        return null;
    }

    public boolean isValid(boolean check) {
        if (check) {
            try {
                getStandaloneConnection();
            } catch (SQLException e) {
                return false;
            }
        }
        return isValid();

    }

    public boolean isValid() {
        return connectionStatus.isValid() && connectionManager.containsConnection(this);
    }

    public boolean isVirtual() {
        return false;
    }

    public ConnectionInfo testConnectivity(boolean showMessageDialog) {
        return connectionManager.testConnectivity(
                connectionSettings.getDatabaseSettings(), 
                connectionSettings.getDetailSettings().getProperties(), connectionStatus, showMessageDialog);
    }

    public void disconnect() {
        connectionPool.closeConnections();
    }

    public String getId() {
        return connectionSettings.getDatabaseSettings().getId();
    }

    public String getUserName() {
        return connectionSettings.getDatabaseSettings().getUser() == null ? "" : connectionSettings.getDatabaseSettings().getUser();
    }

    public ConnectionInfo getConnectionInfo() throws SQLException {
        if (connectionInfo == null) {
            Connection connection = getStandaloneConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            connectionInfo = new ConnectionInfo(databaseMetaData);
        }

        //System.out.println(ResultSetLister.list("Catalogs", getStandaloneConnection().getMetaData().getTypeInfo()));
        return connectionInfo;
    }

    public DBObjectBundle getObjectBundle() {
        if (objectBundle == null) {
            objectBundle = new DBObjectBundleImpl(this, connectionManager);
        }
        return objectBundle;
    }

    public DBSchema getUserSchema() {
        String userName = getUserName().toUpperCase();
        DBSchema defaultSchema = getObjectBundle().getSchema(userName);
        if (defaultSchema == null) {
            List<DBSchema> schemas = getObjectBundle().getSchemas();
            if (schemas.size() > 0) {
                return schemas.get(0);
            }
        }

        return defaultSchema;
    }

    public Connection getStandaloneConnection() throws SQLException {
        return connectionPool.getStandaloneConnection(true);
    }

    public Connection getStandaloneConnection(DBSchema schema) throws SQLException {
        Connection connection = connectionPool.getStandaloneConnection(true);
        if (!schema.isPublicSchema()) {
            getInterfaceProvider().getMetadataInterface().setCurrentSchema(schema.getName(), connection);
        }
        return connection;
    }

    @Nullable
    public Connection getPoolConnection() throws SQLException {
        return connectionPool.allocateConnection();
    }

    public Connection getPoolConnection(DBSchema schema) throws SQLException {
        Connection connection = connectionPool.allocateConnection();
        if (!schema.isPublicSchema()) {
            getInterfaceProvider().getMetadataInterface().setCurrentSchema(schema.getName(), connection);
        }
        return connection;
    }

    public void freePoolConnection(Connection connection) {
        if (!isDisposed)
            connectionPool.releaseConnection(connection);
    }

    public void closePoolConnection(Connection connection) {
        if (!isDisposed)
            connectionPool.releaseConnection(connection);
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public DatabaseInterfaceProvider getInterfaceProvider() {
        if (interfaceProvider == null || interfaceProvider.getDatabaseType() != getDatabaseType()) {
            try {
                interfaceProvider = DatabaseInterfaceProviderFactory.createInterfaceProvider(this);
            } catch (SQLException e) {
                // do not initialize 
                return DatabaseInterfaceProviderFactory.GENERIC_INTERFACE_PROVIDER;
            }
        }
        return interfaceProvider;
    }

    public DBLanguageDialect getLanguageDialect(DBLanguage language) {
        return getInterfaceProvider().getLanguageDialect(language);
    }

    public static Comparator<ConnectionHandler> getComparator(boolean asc) {
        return asc ? ASC_COMPARATOR : DESC_COMPARATOR;
    }

    public static final Comparator<ConnectionHandler> ASC_COMPARATOR = new Comparator<ConnectionHandler>() {
        public int compare(ConnectionHandler connection1, ConnectionHandler connection2) {
            return connection1.getPresentableText().toLowerCase().compareTo(connection2.getPresentableText().toLowerCase());
        }
    };

    public static final Comparator<ConnectionHandler> DESC_COMPARATOR = new Comparator<ConnectionHandler>() {
        public int compare(ConnectionHandler connection1, ConnectionHandler connection2) {
            return connection2.getPresentableText().toLowerCase().compareTo(connection1.getPresentableText().toLowerCase());
        }
    };

    /*********************************************************
     *                       TreeElement                     *
     *********************************************************/
    public String getQualifiedName() {
        if (connectionManager instanceof ProjectConnectionManager) {
            return "Project - " + getPresentableText();
        } else {
            ModuleConnectionManager connectionManager = (ModuleConnectionManager) this.connectionManager;
            Module module = connectionManager.getModule();
            return module.getName() + " - " + getPresentableText();
        }
    }

    public String getName() {
        return connectionSettings.getDatabaseSettings().getName();
    }

    public String getDescription() {
        return connectionSettings.getDatabaseSettings().getDescription();
    }

    public String getPresentableText(){
        return connectionSettings.getDatabaseSettings().getName();
    }

    public Icon getIcon(){
        return connectionStatus.isConnected() ? Icons.CONNECTION_ACTIVE : 
               connectionStatus.isValid() ? Icons.CONNECTION_INACTIVE :
                        Icons.CONNECTION_INVALID;


    }

   /*********************************************************
    *                      Disposable                       *
    *********************************************************/
    public void dispose() {
        if (!isDisposed) {
            isDisposed = true;
            disconnect();
            DisposeUtil.dispose(objectBundle);
            DisposeUtil.dispose(connectionPool);
            connectionPool = null;
        }
    }

    public void setConnectionConfig(final ConnectionSettings connectionSettings) {
        boolean refresh = this.connectionSettings.getDatabaseSettings().hashCode() != connectionSettings.getDatabaseSettings().hashCode();
        this.connectionSettings = connectionSettings;
        if (refresh) {
            connectionSettings.getDatabaseSettings().setDatabaseType(null);
            disconnect();

            new BackgroundTask(getProject(), "Trying to connect to " + getName(), false) {
                @Override
                public void execute(@NotNull ProgressIndicator progressIndicator) {
                    initProgressIndicator(progressIndicator, true);
                    connectionManager.testConnectivity(
                            connectionSettings.getDatabaseSettings(), 
                            connectionSettings.getDetailSettings().getProperties(), connectionStatus, false);
                    //fixme check if the connection is pointing to a new database and reload if this is the case
                    //objectBundle.checkForDatabaseChange();
                    DatabaseBrowserManager.updateTree(getObjectBundle(), TreeUtil.NODES_CHANGED);
                }
            }.start();
        }
    }
}
