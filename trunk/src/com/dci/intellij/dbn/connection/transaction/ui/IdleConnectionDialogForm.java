package com.dci.intellij.dbn.connection.transaction.ui;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.connection.ConnectionHandler;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class IdleConnectionDialogForm extends DBNFormImpl {
    private JPanel mainPanel;
    private JTextArea hintTextArea;
    private JPanel headerPanel;
    private JLabel connectionLabel;

    public IdleConnectionDialogForm(ConnectionHandler connectionHandler, int timeoutMinutes) {
        int idleMinutes = connectionHandler.getIdleMinutes();
        int idleMinutesToDisconnect = connectionHandler.getSettings().getDetailSettings().getIdleTimeToDisconnect();

        String text = "The connection \"" + connectionHandler.getName()+ " \" is been idle for more than " + idleMinutes + " minutes. You have uncommitted changes on this connection. \n" +
                "Please specify whether to commit or rollback the changes. You can chose to keep the connection alive for " + idleMinutesToDisconnect + " more minutes. \n\n" +
                "NOTE: Connection will close automatically if this prompt stays unattended for more than " + timeoutMinutes + " minutes.";
        hintTextArea.setBackground(mainPanel.getBackground());
        hintTextArea.setFont(mainPanel.getFont());
        hintTextArea.setText(text);

        if (getEnvironmentSettings(connectionHandler.getProject()).getVisibilitySettings().getDialogHeaders().value()) {
            headerPanel.setBackground(connectionHandler.getEnvironmentType().getColor());
        }

        connectionLabel.setIcon(connectionHandler.getIcon());
        connectionLabel.setText(connectionHandler.getName());

    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }
}
