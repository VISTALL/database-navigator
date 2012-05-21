package com.dci.intellij.dbn.connection.config.ui;

import com.dci.intellij.dbn.browser.ui.ModuleListCellRenderer;
import com.dci.intellij.dbn.common.options.ui.ConfigurationEditorForm;
import com.dci.intellij.dbn.connection.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Set;

public class GlobalConnectionSettingsForm extends ConfigurationEditorForm<GlobalConnectionSettings> implements ItemListener {
    private Set<String> cachedPanelIds = new HashSet<String>();

    private JPanel mainPanel;
    private JPanel connectionsPanel;
    private JComboBox modulesComboBox;

    public GlobalConnectionSettingsForm(GlobalConnectionSettings connectionSettings) {
        super(connectionSettings);
        Project project = connectionSettings.getProject();

        updateModulesChooser(project);
        modulesComboBox.setRenderer(new ModuleListCellRenderer());
        modulesComboBox.addItemListener(this);
        switchSettingsPanel(ProjectConnectionManager.getInstance(project));
    }

    public JPanel getComponent() {
        return mainPanel;
    }

    public void applyChanges() throws ConfigurationException {
        Project project = getConfiguration().getProject();
        ProjectConnectionManager projectConnectionManager = ProjectConnectionManager.getInstance(project);
        projectConnectionManager.apply();

        ModuleManager moduleManager = ModuleManager.getInstance(project);
        Module[] modules = moduleManager.getModules();
        for (Module module : modules) {
            ModuleConnectionManager.getInstance(module).apply();
        }
    }

    public void resetChanges() {
        Project project = getConfiguration().getProject();
        ProjectConnectionManager projectConnectionManager = ProjectConnectionManager.getInstance(project);
        projectConnectionManager.reset();

        ModuleManager moduleManager = ModuleManager.getInstance(project);
        Module[] modules = moduleManager.getModules();
        for (Module module : modules) {
            ModuleConnectionManager.getInstance(module).reset();
        }
    }

    private void updateModulesChooser(Project project) {
        modulesComboBox.addItem(ProjectConnectionManager.getInstance(project));

        Module[] modules = ModuleManager.getInstance(project).getModules();
        for (Module module : modules) {
            modulesComboBox.addItem(ModuleConnectionManager.getInstance(module));
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            ConnectionManager connectionManager = (ConnectionManager) e.getItem();
            switchSettingsPanel(connectionManager);
        }
    }

    private void switchSettingsPanel(ConnectionManager connectionManager) {
        String id = connectionManager.toString();
        if (!cachedPanelIds.contains(id)) {
            JComponent connectionsSetupPanel = connectionManager.createComponent();
            connectionsPanel.add(connectionsSetupPanel, id);
            cachedPanelIds.add(id);
        }
        CardLayout cardLayout = (CardLayout) connectionsPanel.getLayout();
        cardLayout.show(connectionsPanel, id);
    }

    public void focusConnectionSettings(ConnectionHandler connectionHandler) {
        ConnectionManager connectionManager = connectionHandler.getConnectionManager();
        modulesComboBox.setSelectedItem(connectionManager);
        ConnectionManagerSettingsForm settingsEditor = connectionManager.getSettingsEditor();
        if (settingsEditor != null) {
            settingsEditor.selectConnection(connectionHandler);
        }
    }
}
