package com.dci.intellij.dbn.debugger.execution;

import com.dci.intellij.dbn.common.Icons;
import com.intellij.execution.LocatableConfigurationType;
import com.intellij.execution.Location;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DBProgramRunConfigurationType implements ConfigurationType, LocatableConfigurationType {
    private ConfigurationFactory[] configurationFactories = new ConfigurationFactory[]{new DBProgramRunConfigurationFactory(this)};


    public String getDisplayName() {
        return "DB-Program";
    }

    public String getConfigurationTypeDescription() {
        return null;
    }

    public Icon getIcon() {
        return Icons.EXEC_CONFIG;
    }

    @NotNull
    public String getId() {
        return "DBProgramDebugSession";
    }

    public ConfigurationFactory[] getConfigurationFactories() {
        return configurationFactories;
    }

    public DBProgramRunConfigurationFactory getConfigurationFactory() {
        return (DBProgramRunConfigurationFactory) configurationFactories[0];
    }


    public RunnerAndConfigurationSettings createConfigurationByLocation(Location location) {
        return null;
    }

    public boolean isConfigurationByLocation(RunConfiguration configuration, Location location) {
        return false;
    }
}
