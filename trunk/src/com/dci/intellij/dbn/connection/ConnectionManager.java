package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.code.sql.color.SQLTextAttributesKeys;
import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentType;
import com.dci.intellij.dbn.common.filter.Filter;
import com.dci.intellij.dbn.common.list.FiltrableList;
import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.common.ui.MessageDialog;
import com.dci.intellij.dbn.common.util.EditorUtil;
import com.dci.intellij.dbn.connection.config.ConnectionDatabaseSettings;
import com.dci.intellij.dbn.connection.config.ConnectionDetailSettings;
import com.dci.intellij.dbn.connection.config.ConnectionSettings;
import com.dci.intellij.dbn.connection.config.ui.ConnectionManagerSettingsForm;
import com.dci.intellij.dbn.connection.mapping.FileConnectionMappingManager;
import com.dci.intellij.dbn.object.common.DBObjectBundle;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vfs.VirtualFile;
import gnu.trove.THashSet;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ConnectionManager
        extends Configuration<ConnectionManagerSettingsForm>
        implements Comparable, BrowserTreeElement, JDOMExternalizable, Disposable {

    protected Logger log = Logger.getInstance(this.getClass().getName());

    public static final Filter<ConnectionHandler> ACTIVE_CONNECTIONS_FILTER = new Filter<ConnectionHandler>() {
        public boolean accepts(ConnectionHandler object) {
            return object.isActive();
        }
    };


    private Project project;
    private FiltrableList<ConnectionHandler> connectionHandlers = new FiltrableList<ConnectionHandler>(ACTIVE_CONNECTIONS_FILTER);

    protected ConnectionManager(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public boolean isDisposed() {
        return false;
    }

    public String getDisplayName() {
        return "Connections";
    }

    public String getHelpTopic() {
        return "connectionManager";
    }

    public GenericDatabaseElement getUndisposedElement() {
        return this;
    }

    public DynamicContent getDynamicContent(DynamicContentType dynamicContentType) {
        return null;
    }

    /*********************************************************
    *                        Custom                         *
    *********************************************************/
    public void testConnection(ConnectionHandler connectionHandler, boolean showMessageDialog) {
        ConnectionDatabaseSettings databaseSettings = connectionHandler.getSettings().getDatabaseSettings();
        try {
            connectionHandler.getStandaloneConnection();
            if (showMessageDialog) {
                MessageDialog.showInfoDialog(
                        getProject(),
                        "Successfully connected to \"" + connectionHandler.getName() + "\".",
                        databaseSettings.getConnectionDetails(),
                        false);
            }
        } catch (Exception e) {
            if (showMessageDialog) {
                MessageDialog.showErrorDialog(
                        getProject(),
                        "Could not connect to \"" + connectionHandler.getName() + "\".",
                        databaseSettings.getConnectionDetails() + "\n\n" + e.getMessage(),
                        false);
            }
        }
    }

    public void testConfigConnection(ConnectionDatabaseSettings databaseSettings, boolean showMessageDialog) {
        try {
            Connection connection = ConnectionUtil.connect(databaseSettings, null, false, null);
            ConnectionUtil.closeConnection(connection);
            databaseSettings.setConnectivityStatus(ConnectivityStatus.VALID);
            if (showMessageDialog) {
                MessageDialog.showInfoDialog(
                        getProject(),
                        "Test connection to \"" + databaseSettings.getName() + "\" succeeded. Configuration is valid.",
                        databaseSettings.getConnectionDetails(),
                        false);
            }

        } catch (Exception e) {
            databaseSettings.setConnectivityStatus(ConnectivityStatus.INVALID);
            if (showMessageDialog) {
                MessageDialog.showErrorDialog(
                        getProject(),
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

    public void addConnections(List<ConnectionHandler> connectionHandlers) {
        this.connectionHandlers.addAll(connectionHandlers);
    }

    public void setConnectionHandlers(List<ConnectionHandler> connectionHandlers) {
        this.connectionHandlers = new FiltrableList<ConnectionHandler>(connectionHandlers, ACTIVE_CONNECTIONS_FILTER);
    }

    public boolean containsConnection(ConnectionHandler connectionHandler) {
        return connectionHandlers.contains(connectionHandler);
    }

    public ConnectionHandler getConnection(String id) {
        for (ConnectionHandler connectionHandler : connectionHandlers){
            if (connectionHandler.getId().equals(id)) return connectionHandler;
        }
        return null;
    }

    public FiltrableList<ConnectionHandler> getConnectionHandlers() {
        return connectionHandlers;
    }


    public void dispose() {
        for (ConnectionHandler connectionHandler : connectionHandlers){
            connectionHandler.dispose();
        }
        connectionHandlers.clear();
        project = null;
    }

    public boolean isModified() {
        if (super.isModified()) return true;

        for (ConnectionHandler connectionHandler : connectionHandlers) {
            if (connectionHandler.getSettings().isModified()) return true;
        }
        return false;
    }


    public ConnectionManagerSettingsForm createConfigurationEditor() {
        return new ConnectionManagerSettingsForm(this);
    }

    /*********************************************************
     *                      Configurable                     *
     *********************************************************/
    public void readConfiguration(Element element) throws InvalidDataException {
        Element connectionsElement = element.getChild("connections");
        if (connectionsElement != null) {
            for (Object o : connectionsElement.getChildren()) {
                Element connectionElement = (Element) o;
                ConnectionSettings connectionConfig = new ConnectionSettings(this);
                connectionConfig.readConfiguration(connectionElement);
                ConnectionHandler connectionHandler = new ConnectionHandlerImpl(this, connectionConfig);
                connectionHandlers.add(connectionHandler);
            }
        }
    }

    public void writeConfiguration(Element element) throws WriteExternalException {
        Element connectionsElement = new Element("connections");
        element.addContent(connectionsElement);
        for (ConnectionHandler connectionHandler : connectionHandlers.getFullList()) {
            Element connectionElement = new Element("connection");
            ConnectionSettings connectionSettings = connectionHandler.getSettings();
            connectionSettings.writeConfiguration(connectionElement);
            connectionsElement.addContent(connectionElement);
        }
    }

    /*********************************************************
     *                   JDOMExternalizable                  *
     *********************************************************/
    public void readExternal(Element element) throws InvalidDataException {
        readConfiguration(element);
    }

    public void writeExternal(Element element) throws WriteExternalException {
        writeConfiguration(element);
    }

    /*********************************************************
    *                    NavigationItem                      *
    *********************************************************/
    public void navigate(boolean requestFocus) {

    }

    public boolean canNavigate() {
        return true;
    }

    public boolean canNavigateToSource() {
        return false;
    }

    public FileStatus getFileStatus() {
        return FileStatus.NOT_CHANGED;
    }


    /*********************************************************
    *                  ItemPresentation                      *
    *********************************************************/
    public String getName() {
        return getPresentableText();
    }

    public TextAttributesKey getTextAttributesKey() {
        return SQLTextAttributesKeys.IDENTIFIER;
    }

    public String getLocationString() {
        return null;
    }

    public ItemPresentation getPresentation() {
        return this;
    }

    public Icon getIcon(boolean open) {
        return getIcon(0);
    }

    /*********************************************************
    *                       TreeElement                     *
    *********************************************************/
    public boolean isTreeStructureLoaded() {
        return true;
    }

    public void initTreeElement() {}

    public boolean canExpand() {
        return true;
    }

    public BrowserTreeElement getTreeParent() {
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(getProject());
        return browserManager.isTabbedMode() ? null : browserManager.getActiveBrowserTree().getModel().getRoot();
    }

    public List<? extends BrowserTreeElement> getTreeChildren() {
        return null;  //should never be used
    }

    public void rebuildTreeChildren() {
        for (ConnectionHandler connectionHandler : getConnectionHandlers()) {
            connectionHandler.getObjectBundle().rebuildTreeChildren();
        }
    }

    public BrowserTreeElement getTreeChild(int index) {
        return getConnectionHandlers().get(index).getObjectBundle();
    }

    public int getTreeChildCount() {
        return getConnectionHandlers().size();
    }

    public boolean isLeafTreeElement() {
        return getConnectionHandlers().size() == 0;
    }

    public int getIndexOfTreeChild(BrowserTreeElement child) {
        DBObjectBundle objectBundle = (DBObjectBundle) child;
        return getConnectionHandlers().indexOf(objectBundle.getConnectionHandler());
    }

    public int getTreeDepth() {
        return 1;
    }

    public String getPresentableText() {
        return "Database connections";
    }

    public String getPresentableTextDetails() {
        int size = getConnectionHandlers().size();
        return size == 0 ? "(no connections)" : "(" + size + ")";
    }

    public String getPresentableTextConditionalDetails() {
        return null;
    }

    public ConnectionHandler getConnectionHandler() {
        return null;
    }

   /*********************************************************
    *                    ToolTipProvider                    *
    *********************************************************/
    public String getToolTip() {
        return "";
    }    

   /*********************************************************
    *                     Miscellaneous                     *
    *********************************************************/
    @Nullable
    public static ConnectionHandler getConnectionHandler(String connectionId) {
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            ConnectionHandler connectionHandler = getConnectionHandler(project, connectionId);
            if(connectionHandler != null) return connectionHandler;
        }
        return null;
    }

    public static ConnectionHandler getConnectionHandler(Project project, String connectionId) {
        ConnectionHandler connectionHandler = ProjectConnectionManager.getInstance(project).getConnection(connectionId);
        if (connectionHandler == null) {
            for (Module module : ModuleManager.getInstance(project).getModules()) {
                connectionHandler = ModuleConnectionManager.getInstance(module).getConnection(connectionId);
                if(connectionHandler != null) return connectionHandler;
            }
        }
        return connectionHandler;
    }

    public static Set<ConnectionHandler> getConnectionHandlers(Project project) {
        Set<ConnectionHandler> connectionHandlers = new THashSet<ConnectionHandler>();
        connectionHandlers.addAll(ProjectConnectionManager.getInstance(project).getConnectionHandlers());
        for (Module module : ModuleManager.getInstance(project).getModules()) {
            connectionHandlers.addAll(ModuleConnectionManager.getInstance(module).getConnectionHandlers());
        }

        return connectionHandlers;
    }

    public static ConnectionHandler getActiveConnection(Project project) {
        ConnectionHandler connectionHandler = null;
        VirtualFile virtualFile = EditorUtil.getSelectedFile(project);
        if (DatabaseBrowserManager.getInstance(project).getBrowserToolWindow().isActive() || virtualFile == null) {
            connectionHandler = DatabaseBrowserManager.getInstance(project).getActiveConnection();
        }

        if (connectionHandler == null && virtualFile!= null) {
            connectionHandler = FileConnectionMappingManager.getInstance(project).getActiveConnection(virtualFile);
        }

        return connectionHandler;
    }



}
