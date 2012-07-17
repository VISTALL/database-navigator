package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.common.AbstractProjectComponent;
import com.dci.intellij.dbn.common.event.EventManager;
import com.dci.intellij.dbn.common.ui.MessageDialog;
import com.dci.intellij.dbn.connection.config.ConnectionBundleSettingsListener;
import com.dci.intellij.dbn.connection.config.ConnectionDatabaseSettings;
import com.dci.intellij.dbn.connection.config.ConnectionDetailSettings;
import com.dci.intellij.dbn.connection.config.ConnectionSettings;
import com.intellij.ProjectTopics;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.ModuleAdapter;
import com.intellij.openapi.project.ModuleListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConnectionManager extends AbstractProjectComponent implements ProjectManagerListener{
    private List<ConnectionBundle> connectionBundles = new ArrayList<ConnectionBundle>();;

    public static ConnectionManager getInstance(Project project) {
        return project.getComponent(ConnectionManager.class);
    }

    private ConnectionManager(Project project) {
        super(project);
        ProjectManager projectManager = ProjectManager.getInstance();
        projectManager.addProjectManagerListener(project, this);
    }

    @Override
    public void initComponent() {
        super.initComponent();
        getEventManager().subscribe(ProjectTopics.MODULES, moduleListener);
        getEventManager().subscribe(ConnectionBundleSettingsListener.TOPIC, connectionBundleSettingsListener);
        initConnectionBundles();
    }

    @Override
    public void disposeComponent() {
        getEventManager().unsubscribe(
                moduleListener,
                connectionBundleSettingsListener);
    }

    /*********************************************************
    *                       Listeners                        *
    *********************************************************/
    private ModuleListener moduleListener = new ModuleAdapter() {
        public void moduleAdded(Project project, Module module) {
            ModuleConnectionBundle connectionBundle = ModuleConnectionBundle.getInstance(module);
            if (connectionBundle.getConnectionHandlers().size() > 0) {
                connectionBundles = null;
            }
        }

        public void moduleRemoved(Project project, Module module) {
            ModuleConnectionBundle connectionManager = ModuleConnectionBundle.getInstance(module);
            if (connectionBundles.remove(connectionManager)) {
                connectionBundles = null;
            }
        }

        public void modulesRenamed(Project project, List<Module> modules) {
            for (Module module : modules) {
                ModuleConnectionBundle connectionManager = ModuleConnectionBundle.getInstance(module);
                if (connectionManager.getConnectionHandlers().size() > 0) {
                    initConnectionBundles();
                    break;
                }
            }
        }
    };

    private ConnectionBundleSettingsListener connectionBundleSettingsListener = new ConnectionBundleSettingsListener() {
        @Override
        public void settingsChanged() {
            initConnectionBundles();
        }
    };

    /*********************************************************
    *                        Custom                         *
    *********************************************************/
    public List<ConnectionBundle> getConnectionBundles() {
        return connectionBundles;
    }

    private synchronized void initConnectionBundles() {
        Project project = getProject();
        connectionBundles.clear();
        ProjectConnectionBundle projectConnectionBundle = ProjectConnectionBundle.getInstance(project);
        if (projectConnectionBundle.getConnectionHandlers().size() > 0) {
            connectionBundles.add(projectConnectionBundle);
        }
        Module[] modules = ModuleManager.getInstance(project).getModules();
        for (Module module : modules) {
            ModuleConnectionBundle moduleConnectionBundle = ModuleConnectionBundle.getInstance(module);
            if (moduleConnectionBundle.getConnectionHandlers().size() > 0) {
                connectionBundles.add(moduleConnectionBundle);
            }
        }
        Collections.sort(connectionBundles);
        EventManager.notify(project, ConnectionManagerListener.TOPIC).connectionsChanged();
    }

    public void testConnection(ConnectionHandler connectionHandler, boolean showMessageDialog) {
        Project project = getProject();
        ConnectionDatabaseSettings databaseSettings = connectionHandler.getSettings().getDatabaseSettings();
        try {
            connectionHandler.getStandaloneConnection();
            if (showMessageDialog) {
                MessageDialog.showInfoDialog(
                        project,
                        "Successfully connected to \"" + connectionHandler.getName() + "\".",
                        databaseSettings.getConnectionDetails(),
                        false);
            }
        } catch (Exception e) {
            if (showMessageDialog) {
                MessageDialog.showErrorDialog(
                        project,
                        "Could not connect to \"" + connectionHandler.getName() + "\".",
                        databaseSettings.getConnectionDetails() + "\n\n" + e.getMessage(),
                        false);
            }
        }
    }

    public void testConfigConnection(ConnectionDatabaseSettings databaseSettings, boolean showMessageDialog) {
        Project project = getProject();
        try {
            Connection connection = ConnectionUtil.connect(databaseSettings, null, false, null);
            ConnectionUtil.closeConnection(connection);
            databaseSettings.setConnectivityStatus(ConnectivityStatus.VALID);
            if (showMessageDialog) {
                MessageDialog.showInfoDialog(
                        project,
                        "Test connection to \"" + databaseSettings.getName() + "\" succeeded. Configuration is valid.",
                        databaseSettings.getConnectionDetails(),
                        false);
            }

        } catch (Exception e) {
            databaseSettings.setConnectivityStatus(ConnectivityStatus.INVALID);
            if (showMessageDialog) {
                MessageDialog.showErrorDialog(
                        project,
                        "Could not connect to \"" + databaseSettings.getName() + "\".",
                        databaseSettings.getConnectionDetails() + "\n\n" + e.getMessage(),
                        false);
            }
        }
    }

    public ConnectionInfo showConnectionInfo(ConnectionSettings connectionSettings) {
        ConnectionDatabaseSettings databaseSettings = connectionSettings.getDatabaseSettings();
        ConnectionDetailSettings detailSettings = connectionSettings.getDetailSettings();
        return showConnectionInfo(databaseSettings, detailSettings);
    }

    public ConnectionInfo showConnectionInfo(ConnectionDatabaseSettings databaseSettings, @Nullable ConnectionDetailSettings detailSettings) {
        try {
            Map<String, String> connectionProperties = detailSettings == null ? null : detailSettings.getProperties();
            Connection connection = ConnectionUtil.connect(databaseSettings, connectionProperties, false, null);
            ConnectionInfo connectionInfo = new ConnectionInfo(connection.getMetaData());
            ConnectionUtil.closeConnection(connection);
            MessageDialog.showInfoDialog(
                    getProject(),
                    "Database details for connection \"" + databaseSettings.getName() + "\"",
                    connectionInfo.toString(),
                    false);
            return connectionInfo;

        } catch (Exception e) {
            MessageDialog.showErrorDialog(
                    getProject(),
                    "Could not connect to \"" + databaseSettings.getName() + "\".",
                    databaseSettings.getConnectionDetails() + "\n\n" + e.getMessage(),
                    false);
            return null;
        }
    }


    /**********************************************
    *            ProjectManagerListener           *
    ***********************************************/

    @Override
    public void projectOpened(Project project) {

    }

    @Override
    public boolean canCloseProject(Project project) {
        return true;
    }

    @Override
    public void projectClosed(Project project) {
    }

    @Override
    public void projectClosing(Project project) {
    }

    /**********************************************
    *                ProjectComponent             *
    ***********************************************/
    @NonNls
    @NotNull
    public String getComponentName() {
        return "DBNavigator.Project.DatabaseConnectionManager";
    }
}