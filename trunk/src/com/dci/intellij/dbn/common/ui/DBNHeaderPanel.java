package com.dci.intellij.dbn.common.ui;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;

public class DBNHeaderPanel extends DBNFormImpl{
    private JLabel objectLabel;
    private JPanel mainPanel;

    public DBNHeaderPanel(String title, Icon icon, Color background) {
        objectLabel.setText(title);
        objectLabel.setIcon(icon);
        mainPanel.setBackground(background);
    }

    public void setBackground(Color background) {
        mainPanel.setBackground(background);
    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }
}
