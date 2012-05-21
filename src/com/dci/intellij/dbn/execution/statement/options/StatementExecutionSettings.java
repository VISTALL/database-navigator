package com.dci.intellij.dbn.execution.statement.options;

import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.common.options.setting.SettingsUtil;
import com.dci.intellij.dbn.common.options.ui.ConfigurationEditorForm;
import com.dci.intellij.dbn.execution.statement.options.ui.StatementExecutionSettingsForm;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

public class StatementExecutionSettings extends Configuration{
    private int resultSetFetchBlockSize = 100;
    private int executionTimeout = 20;

    public String getDisplayName() {
        return "Data editor general settings";
    }

    public String getHelpTopic() {
        return "executionEngine";
    }

    /*********************************************************
    *                       Settings                        *
    *********************************************************/

    public int getResultSetFetchBlockSize() {
        return resultSetFetchBlockSize;
    }

    public void setResultSetFetchBlockSize(int resultSetFetchBlockSize) {
        this.resultSetFetchBlockSize = resultSetFetchBlockSize;
    }

    public int getExecutionTimeout() {
        return executionTimeout;
    }

    public void setExecutionTimeout(int executionTimeout) {
        this.executionTimeout = executionTimeout;
    }

    /****************************************************
     *                   Configuration                  *
     ****************************************************/
    public ConfigurationEditorForm createConfigurationEditor() {
        return new StatementExecutionSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "statement-execution";
    }

    public void readConfiguration(Element element) throws InvalidDataException {
        resultSetFetchBlockSize = SettingsUtil.getInteger(element, "fetch-block-size", resultSetFetchBlockSize);
        executionTimeout = SettingsUtil.getInteger(element, "execution-timeout", executionTimeout);

    }

    public void writeConfiguration(Element element) throws WriteExternalException {
        SettingsUtil.setInteger(element, "fetch-block-size", resultSetFetchBlockSize);
        SettingsUtil.setInteger(element, "execution-timeout", executionTimeout);
    }
}
