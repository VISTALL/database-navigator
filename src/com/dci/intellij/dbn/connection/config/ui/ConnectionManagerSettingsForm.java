package com.dci.intellij.dbn.connection.config.ui;

import com.dci.intellij.dbn.common.event.EventManager;
import com.dci.intellij.dbn.common.options.ui.ConfigurationEditorForm;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.UIUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionHandlerImpl;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.config.ConnectionConfigListCellRenderer;
import com.dci.intellij.dbn.connection.config.ConnectionDatabaseSettings;
import com.dci.intellij.dbn.connection.config.ConnectionManagerSettingsListener;
import com.dci.intellij.dbn.connection.config.ConnectionSettings;
import com.dci.intellij.dbn.connection.config.action.AddConnectionAction;
import com.dci.intellij.dbn.connection.config.action.DuplicateConnectionAction;
import com.dci.intellij.dbn.connection.config.action.MoveConnectionDownAction;
import com.dci.intellij.dbn.connection.config.action.MoveConnectionUpAction;
import com.dci.intellij.dbn.connection.config.action.RemoveConnectionAction;
import com.dci.intellij.dbn.connection.config.action.SortConnectionsAction;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.GuiUtils;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionManagerSettingsForm extends ConfigurationEditorForm<ConnectionManager> implements ListSelectionListener {
    private static final String BLANK_PANEL_ID = "BLANK_PANEL";

    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JPanel connectionSetupPanel;
    private JList connectionsList;
    private JSplitPane mainSplitPane;
    
    private String currentPanelId;
    
    private Map<String, ConnectionSettingsForm> cachedForms = new HashMap<String, ConnectionSettingsForm>();

    public JList getList() {
        return connectionsList;
    }

    public ConnectionManagerSettingsForm(ConnectionManager connectionManager) {
        super(connectionManager);
        ActionToolbar actionToolbar = ActionUtil.createActionToolbar(
                "DBNavigator.Connections.Setup", true,
                new AddConnectionAction(connectionsList, connectionManager),
                new RemoveConnectionAction(connectionsList, connectionManager),
                new DuplicateConnectionAction(connectionsList, connectionManager),
                ActionUtil.SEPARATOR,
                new MoveConnectionUpAction(connectionsList, connectionManager),
                new MoveConnectionDownAction(connectionsList, connectionManager),
                new SortConnectionsAction(connectionsList, connectionManager));
        actionsPanel.add(actionToolbar.getComponent(), BorderLayout.CENTER);

        connectionsList.setModel(new ConnectionListModel(connectionManager));
        connectionsList.addListSelectionListener(this);
        connectionsList.setCellRenderer(new ConnectionConfigListCellRenderer());
        if (connectionManager.getConnectionHandlers().size() > 0) {
            selectConnection(connectionManager.getConnectionHandlers().get(0));
        }
        JPanel emptyPanel = new JPanel();
        connectionSetupPanel.setPreferredSize(new Dimension(500, -1));
        connectionSetupPanel.add(emptyPanel, BLANK_PANEL_ID);
        GuiUtils.replaceJSplitPaneWithIDEASplitter(mainPanel);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIUtil.updateSplitterProportion(mainPanel, (float) 0.3);
            }
        });
    }

    public JPanel getComponent() {
        return mainPanel;
    }

    public void applyChanges() throws ConfigurationException {
        ConnectionManager connectionManager = getConfiguration();

        List<ConnectionHandler> oldConnections = new ArrayList<ConnectionHandler>(connectionManager.getConnectionHandlers().getFullList());
        List<ConnectionHandler> newConnections = new ArrayList<ConnectionHandler>();

        boolean listChanged = false;
        ConnectionListModel listModel = (ConnectionListModel) connectionsList.getModel();
        if (oldConnections.size() == listModel.getSize()) {
            for (int i=0; i<oldConnections.size(); i++) {
                ConnectionDatabaseSettings oldConfig = oldConnections.get(i).getSettings().getDatabaseSettings();
                ConnectionDatabaseSettings newConfig = ((ConnectionSettings) listModel.get(i)).getDatabaseSettings();
                if (!oldConfig.getId().equals(newConfig.getId()) ||
                        (newConfig.getSettingsEditor() != null && newConfig.getSettingsEditor().isConnectionActive() != oldConfig.isActive())) {
                    listChanged = true;
                    break;
                }
            }
        } else {
            listChanged = true;
        }

        for (int i=0; i< listModel.getSize(); i++) {
            ConnectionSettings connectionSettings = (ConnectionSettings) listModel.getElementAt(i);
            connectionSettings.apply();

            ConnectionHandler connectionHandler = connectionManager.getConnection(connectionSettings.getDatabaseSettings().getId());
            if (connectionHandler == null) {
                connectionHandler = new ConnectionHandlerImpl(connectionManager, connectionSettings);
                connectionSettings.getDatabaseSettings().setNew(false);
            } else {
                oldConnections.remove(connectionHandler);
                ((ConnectionHandlerImpl)connectionHandler).setConnectionConfig(connectionSettings);
            }

            newConnections.add(connectionHandler);

        }
        connectionManager.setConnectionHandlers(newConnections);

        // dispose old list
        for (ConnectionHandler connectionHandler : oldConnections) {
            connectionHandler.disconnect();
            connectionHandler.dispose();
        }

        if (listChanged) {
            EventManager.syncPublisher(connectionManager.getProject(), ConnectionManagerSettingsListener.TOPIC).settingsChanged();
        }
    }

    public void resetChanges() {
        ConnectionListModel listModel = (ConnectionListModel) connectionsList.getModel();
        for (int i=0; i< listModel.getSize(); i++) {
            ConnectionSettings connectionSettings = (ConnectionSettings) listModel.getElementAt(i);
            connectionSettings.reset();
        }
    }

    public void selectConnection(ConnectionHandler connectionHandler) {
        connectionsList.setSelectedValue(connectionHandler.getSettings(), true);
    }

    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        try {
            Object[] selectedValues = connectionsList.getSelectedValues();
            if (selectedValues.length == 1) {
                ConnectionSettings connectionSettings = (ConnectionSettings) selectedValues[0];
                switchSettingsPanel(connectionSettings);
            } else {
                switchSettingsPanel(null);
            }
        } catch (IndexOutOfBoundsException e) {
            // fixme find out why
        }
    }

    @Override
    public void dispose() {
        for (ConnectionSettingsForm settingsForm : cachedForms.values()) {
            settingsForm.dispose();
        }
        cachedForms.clear();
    }

    private void switchSettingsPanel(ConnectionSettings connectionSettings) {
        CardLayout cardLayout = (CardLayout) connectionSetupPanel.getLayout();
        if (connectionSettings == null) {
            cardLayout.show(connectionSetupPanel, BLANK_PANEL_ID);
        } else {

            ConnectionSettingsForm currentForm = cachedForms.get(currentPanelId);
            String selectedTabName = currentForm == null ? null : currentForm.getSelectedTabName();

            currentPanelId = connectionSettings.getDatabaseSettings().getId();
            if (!cachedForms.keySet().contains(currentPanelId)) {
                JComponent setupPanel = connectionSettings.createComponent();
                this.connectionSetupPanel.add(setupPanel, currentPanelId);
                cachedForms.put(currentPanelId, connectionSettings.getSettingsEditor());
            }

            ConnectionSettingsForm settingsEditor = connectionSettings.getSettingsEditor();
            if (settingsEditor != null) {
                settingsEditor.selectTab(selectedTabName);
            }

            cardLayout.show(connectionSetupPanel, currentPanelId);
        }
    }
}
