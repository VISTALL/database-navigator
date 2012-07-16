package com.dci.intellij.dbn.connection.transaction.ui;

import com.dci.intellij.dbn.common.ui.UIFormImpl;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class UncommittedChangesForm extends UIFormImpl {
    private JTable changesTable;
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JLabel connectionLabel;
    private JBScrollPane changesTableScrollPane;
    private JTextArea hintTextArea;

    private ConnectionHandler connectionHandler;

    public UncommittedChangesForm(ConnectionHandler connectionHandler, @Nullable String hintText) {
        this.connectionHandler = connectionHandler;
        Project project = connectionHandler.getProject();
        if (getEnvironmentSettings(project).getVisibilitySettings().getDialogHeaders().value()) {
            headerPanel.setBackground(connectionHandler.getEnvironmentType().getColor());
        }

        connectionLabel.setIcon(connectionHandler.getIcon());
        connectionLabel.setText(connectionHandler.getName());
        changesTableScrollPane.getViewport().setBackground(changesTable.getBackground());

        hintTextArea.setBackground(mainPanel.getBackground());
        hintTextArea.setFont(mainPanel.getFont());
        if (StringUtil.isEmpty(hintText)) {
            hintTextArea.setVisible(false);
        } else {
            hintTextArea.setText(StringUtil.wrap(hintText, 90, ": ,."));
            hintTextArea.setVisible(true);
        }
    }

    private void createUIComponents() {
        UncommittedChangesTableModel model = new UncommittedChangesTableModel(connectionHandler);
        changesTable = new UncommittedChangesTable(model);
    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    @Override
    public void dispose() {
        super.dispose();
        connectionHandler = null;
    }
}
