package com.dci.intellij.dbn.execution.statement.options.ui;

import com.dci.intellij.dbn.common.options.ui.ConfigurationEditorForm;
import com.dci.intellij.dbn.common.options.ui.ConfigurationEditorUtil;
import com.dci.intellij.dbn.execution.statement.options.StatementExecutionSettings;
import com.intellij.openapi.options.ConfigurationException;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class StatementExecutionSettingsForm extends ConfigurationEditorForm<StatementExecutionSettings> {
    private JPanel mainPanel;
    private JTextField fetchBlockSizeTextField;
    private JTextField executionTimeoutTextField;

    public StatementExecutionSettingsForm(StatementExecutionSettings settings) {
        super(settings);
        resetChanges();

        registerComponent(fetchBlockSizeTextField);
        registerComponent(executionTimeoutTextField);
    }

    public JPanel getComponent() {
        return mainPanel;
    }

    public void applyChanges() throws ConfigurationException {
        StatementExecutionSettings settings = getConfiguration();
        settings.setResultSetFetchBlockSize(ConfigurationEditorUtil.validateIntegerInputValue(fetchBlockSizeTextField, "Fetch block size", 1, 10000, null));
        settings.setExecutionTimeout(ConfigurationEditorUtil.validateIntegerInputValue(executionTimeoutTextField, "Execution timeout", 0, 300, "\nUse value 0 for no timeout"));
    }

    public void resetChanges() {
        StatementExecutionSettings settings = getConfiguration();
        fetchBlockSizeTextField.setText(Integer.toString(settings.getResultSetFetchBlockSize()));
        executionTimeoutTextField.setText(Integer.toString(settings.getExecutionTimeout()));
    }
}
