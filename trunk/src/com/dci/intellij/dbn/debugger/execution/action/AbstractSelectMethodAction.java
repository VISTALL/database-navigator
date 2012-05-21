package com.dci.intellij.dbn.debugger.execution.action;

import com.dci.intellij.dbn.debugger.execution.DBProgramRunConfiguration;
import com.intellij.openapi.project.DumbAwareAction;

import javax.swing.*;

public abstract class AbstractSelectMethodAction extends DumbAwareAction {
    private DBProgramRunConfiguration configuration;

    public AbstractSelectMethodAction(String text, Icon icon, DBProgramRunConfiguration configuration) {
        super(text, null, icon);
        this.configuration = configuration;
    }

    public DBProgramRunConfiguration getConfiguration() {
        return configuration;
    }
}
