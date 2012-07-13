package com.dci.intellij.dbn.connection.transaction.ui;

import com.dci.intellij.dbn.common.ui.UIFormImpl;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class UncommittedChangesForm extends UIFormImpl {
    private JTable changesTable;
    private JPanel mailPanel;
    private JPanel headerPanel;
    private JLabel connectionLabel;
    private JBScrollPane changesTableScrollPane;

    private ConnectionHandler connectionHandler;

    public UncommittedChangesForm(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        Project project = connectionHandler.getProject();
        if (getEnvironmentSettings(project).getVisibilitySettings().getDialogHeaders().value()) {
            headerPanel.setBackground(connectionHandler.getEnvironmentType().getColor());
        }

        connectionLabel.setIcon(connectionHandler.getIcon());
        connectionLabel.setText(connectionHandler.getName());
        changesTableScrollPane.getViewport().setBackground(changesTable.getBackground());
    }

    private void createUIComponents() {
        UncommittedChangesTableModel model = new UncommittedChangesTableModel(connectionHandler);
        changesTable = new UncommittedChangesTable(model);
    }

    @Override
    public JComponent getComponent() {
        return mailPanel;
    }

    @Override
    public void dispose() {
        super.dispose();
        connectionHandler = null;
    }
}
