package com.dci.intellij.dbn.connection.config.ui;

import com.dci.intellij.dbn.common.environment.EnvironmentChangeListener;
import com.dci.intellij.dbn.common.environment.EnvironmentType;
import com.dci.intellij.dbn.common.environment.EnvironmentTypeBundle;
import com.dci.intellij.dbn.common.environment.options.EnvironmentPresentationChangeListener;
import com.dci.intellij.dbn.common.environment.options.EnvironmentSettings;
import com.dci.intellij.dbn.common.event.EventManager;
import com.dci.intellij.dbn.common.options.ui.ConfigurationEditorForm;
import com.dci.intellij.dbn.common.properties.ui.PropertiesEditorForm;
import com.dci.intellij.dbn.common.ui.ComboBoxUtil;
import com.dci.intellij.dbn.connection.ConnectionSettingsChangeListener;
import com.dci.intellij.dbn.connection.config.ConnectionDetailSettings;
import com.dci.intellij.dbn.options.general.GeneralProjectSettings;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.util.ui.ColorIcon;
import com.intellij.util.ui.UIUtil;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class ConnectionDetailSettingsForm extends ConfigurationEditorForm<ConnectionDetailSettings> implements ConnectionPresentationChangeListener, EnvironmentPresentationChangeListener {
    private JComboBox encodingComboBox;
    private JCheckBox autoCommitCheckBox;
    private JPanel propertiesPanel;
    private JPanel mainPanel;
    private JLabel connectionLabel;
    private JComboBox environmentTypesComboBox;
    private JPanel headerPanel;

    private PropertiesEditorForm propertiesEditorForm;
    private String connectionId;
    
    public ConnectionDetailSettingsForm(final ConnectionDetailSettings configuration) {
        super(configuration);
        final Project project = configuration.getProject();

        Map<String, String> properties = new HashMap<String, String>();
        properties.putAll(configuration.getProperties());

        propertiesEditorForm = new PropertiesEditorForm(properties);
        propertiesEditorForm.setMoveButtonsVisible(false);
        propertiesPanel.add(propertiesEditorForm.getComponent(), BorderLayout.CENTER);
        for (Charset charset : Charset.availableCharsets().values()) {
            encodingComboBox.addItem(charset);
        }

        DefaultComboBoxModel environmentTypesModel = createEnvironmentTypesModel(getEnvironmentTypes());
        environmentTypesComboBox.setModel(environmentTypesModel);
        resetChanges();

        registerComponent(propertiesPanel);
        registerComponent(encodingComboBox);
        registerComponent(autoCommitCheckBox);
        registerComponent(environmentTypesComboBox);

        environmentTypesComboBox.setRenderer(environmentTypeCellRenderer);
        environmentTypesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyPresentationChanges();
            }
        });

        
        EventManager.subscribe(project, ConnectionPresentationChangeListener.TOPIC, this);
        EventManager.subscribe(project, EnvironmentPresentationChangeListener.TOPIC, this);
    }

    public void notifyPresentationChanges() {
        Project project = getConfiguration().getProject();
        ConnectionPresentationChangeListener listener = EventManager.syncPublisher(project, ConnectionPresentationChangeListener.TOPIC);
        EnvironmentType environmentType = (EnvironmentType) environmentTypesComboBox.getSelectedItem();
        Color color = environmentType == null ? null : environmentType.getColor();
        listener.presentationChanged(null, null, color, connectionId);
    }

    @Override
    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    private final ColoredListCellRenderer environmentTypeCellRenderer = new ColoredListCellRenderer() {
        @Override
        protected void customizeCellRenderer(JList list, Object value, int index, boolean selected, boolean hasFocus) {
            EnvironmentType environmentType = (EnvironmentType) value;
            if (environmentType != null) {
                Color color = environmentType.getColor();
                String name = environmentType.getName();

                if (color != null) {
                    setIcon(new ColorIcon(12, color));
                }

                if (name != null) {
                    append(name);
                }
                
            }
        }
    };

    private EnvironmentTypeBundle getEnvironmentTypes() {
        Project project = getConfiguration().getProject();
        EnvironmentSettings environmentSettings = GeneralProjectSettings.getInstance(project).getEnvironmentSettings();
        return environmentSettings.getEnvironmentTypes();
    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    @Override
    public void presentationChanged(String name, Icon icon, Color color, String connectionId) {
        if (this.connectionId.equals(connectionId)) {
            if (name != null) connectionLabel.setText(name);
            if (icon != null) connectionLabel.setIcon(icon);
            headerPanel.setBackground(color == null ? UIUtil.getPanelBackground() :color);
        }
    }

    @Override
    public void applyChanges() throws ConfigurationException {
        ConnectionDetailSettings configuration = getConfiguration();

        Map<String, String> newProperties = propertiesEditorForm.getProperties();
        Charset newCharset = (Charset) encodingComboBox.getSelectedItem();
        boolean newAutoCommit = autoCommitCheckBox.isSelected();
        EnvironmentType newEnvironmentType = (EnvironmentType) environmentTypesComboBox.getSelectedItem();

        boolean settingsChanged =
                !configuration.getProperties().equals(newProperties) ||
                !configuration.getCharset().equals(newCharset) ||
                configuration.isAutoCommit() != newAutoCommit;

        boolean environmentChanged =
                !configuration.getEnvironmentType().equals(newEnvironmentType);


        configuration.setProperties(newProperties);
        configuration.setCharset(newCharset);
        configuration.setAutoCommit(newAutoCommit);
        configuration.setEnvironmentType(newEnvironmentType);

        Project project = getConfiguration().getProject();
        if (environmentChanged) {
            EnvironmentChangeListener listener = EventManager.syncPublisher(project, EnvironmentChangeListener.TOPIC);
            listener.environmentTypeChanged(newEnvironmentType);
        }

        if (settingsChanged) {
            ConnectionSettingsChangeListener listener = EventManager.syncPublisher(project, ConnectionSettingsChangeListener.TOPIC);
            listener.connectionSettingsChanged(connectionId);
        }

    }

    @Override
    public void resetChanges() {
        ConnectionDetailSettings configuration = getConfiguration();
        encodingComboBox.setSelectedItem(configuration.getCharset());
        propertiesEditorForm.setProperties(configuration.getProperties());
        autoCommitCheckBox.setSelected(configuration.isAutoCommit());
        environmentTypesComboBox.setSelectedItem(configuration.getEnvironmentType());
    }

    @Override
    public void dispose() {
        EventManager.unsubscribe(getConfiguration().getProject(), this);
        super.dispose();
    }

    @Override
    public void settingsChanged(EnvironmentTypeBundle environmentTypes) {
        EnvironmentType selectedItem = (EnvironmentType) environmentTypesComboBox.getSelectedItem();
        String selectedName = selectedItem == null ? "" : selectedItem.getName();
        selectedItem = environmentTypes.get(selectedName);

        DefaultComboBoxModel model = createEnvironmentTypesModel(environmentTypes);
        environmentTypesComboBox.setModel(model);
        environmentTypesComboBox.setSelectedItem(selectedItem);
        notifyPresentationChanges();
    }

    private DefaultComboBoxModel createEnvironmentTypesModel(EnvironmentTypeBundle environmentTypes) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(EnvironmentType.DEFAULT);
        ComboBoxUtil.addItems(model, environmentTypes.clone());
        return model;
    }

}
