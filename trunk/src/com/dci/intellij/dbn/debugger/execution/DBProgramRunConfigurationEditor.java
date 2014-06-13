package com.dci.intellij.dbn.debugger.execution;

import com.dci.intellij.dbn.debugger.execution.ui.DBProgramRunConfigurationEditorForm;
import com.dci.intellij.dbn.execution.method.MethodExecutionInput;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;

public class DBProgramRunConfigurationEditor extends SettingsEditor<DBProgramRunConfiguration> {
    private DBProgramRunConfigurationEditorForm configurationEditorForm;
    private DBProgramRunConfiguration configuration;

    public DBProgramRunConfigurationEditor(DBProgramRunConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void resetEditorFrom(DBProgramRunConfiguration configuration) {
        if (configurationEditorForm == null) {
            configurationEditorForm = new DBProgramRunConfigurationEditorForm(configuration);
        }
        configurationEditorForm.readConfiguration(configuration);
    }

    @Override
    protected void applyEditorTo(DBProgramRunConfiguration configuration) throws ConfigurationException {
        if (configurationEditorForm != null) {
            configurationEditorForm.writeConfiguration(configuration);
        }
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        configurationEditorForm = new DBProgramRunConfigurationEditorForm(configuration);
        return configurationEditorForm.getComponent();
    }

    @Override
    protected void disposeEditor() {
        configurationEditorForm.dispose();
        configurationEditorForm = null;
    }

    public void setExecutionInput(MethodExecutionInput executionInput) {
        if (configurationEditorForm != null) {
            configurationEditorForm.setExecutionInput(executionInput, true);
        }

    }
}
