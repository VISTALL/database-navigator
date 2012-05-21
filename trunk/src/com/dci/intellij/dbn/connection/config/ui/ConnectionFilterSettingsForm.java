package com.dci.intellij.dbn.connection.config.ui;

import com.dci.intellij.dbn.common.event.EventManager;
import com.dci.intellij.dbn.common.options.ui.CompositeConfigurationEditorForm;
import com.dci.intellij.dbn.connection.config.ConnectionFilterSettings;
import com.intellij.util.ui.UIUtil;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

public class ConnectionFilterSettingsForm extends CompositeConfigurationEditorForm<ConnectionFilterSettings> implements ConnectionPresentationChangeListener{
    private JPanel mainPanel;
    private JPanel objectTypesFilterPanel;
    private JPanel objectNameFiltersPanel;
    private JLabel connectionLabel;
    private JPanel headerPanel;
    
    private String connectionId;

    public ConnectionFilterSettingsForm(ConnectionFilterSettings settings) {
        super(settings);
        objectTypesFilterPanel.add(settings.getObjectTypeFilterSettings().createComponent(), BorderLayout.CENTER);
        objectNameFiltersPanel.add(settings.getObjectNameFilterSettings().createComponent(), BorderLayout.CENTER);
        EventManager.subscribe(settings.getProject(), ConnectionPresentationChangeListener.TOPIC, this);
    }

    public JComponent getComponent() {
        return mainPanel;
    }

    @Override
    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
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
    public void dispose() {
        EventManager.unsubscribe(getConfiguration().getProject(), this);
        super.dispose();
    }
}
