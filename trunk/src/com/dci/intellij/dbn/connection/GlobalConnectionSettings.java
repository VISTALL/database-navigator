package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.common.options.ProjectConfiguration;
import com.dci.intellij.dbn.connection.config.ui.GlobalConnectionSettingsForm;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public class GlobalConnectionSettings extends ProjectConfiguration<GlobalConnectionSettingsForm> {

    public GlobalConnectionSettings(Project project) {
        super(project);
    }

    public static GlobalConnectionSettings getInstance(Project project) {
        return getGlobalProjectSettings(project).getConnectionSettings();
    }

    @NotNull
    @Override
    public String getId() {
        return "DBNavigator.Project.ConnectionSettings";
    }

    public String getDisplayName() {
        return "Connections";
    }

    public String getHelpTopic() {
        return "connectionManager";
    }

    /*********************************************************
    *                   UnnamedConfigurable                 *
    *********************************************************/
    public GlobalConnectionSettingsForm createConfigurationEditor() {
        return new GlobalConnectionSettingsForm(this);
    }

    public boolean isModified() {
        ProjectConnectionManager projectConnectionManager = ProjectConnectionManager.getInstance(getProject());
        if (projectConnectionManager.isModified()) return true;

        Module[] modules =  ModuleManager.getInstance(getProject()).getModules();
        for (Module module : modules) {
            ModuleConnectionManager moduleConnectionManager = ModuleConnectionManager.getInstance(module);
            if (moduleConnectionManager.isModified()) return true;
        }
        return false;
    }

    public void apply() throws ConfigurationException {
        ProjectConnectionManager projectConnectionManager = ProjectConnectionManager.getInstance(getProject());
        projectConnectionManager.apply();

        Module[] modules =  ModuleManager.getInstance(getProject()).getModules();
        for (Module module : modules) {
            ModuleConnectionManager moduleConnectionManager = ModuleConnectionManager.getInstance(module);
            moduleConnectionManager.apply();
        }
    }

    public void reset() {
        ProjectConnectionManager projectConnectionManager = ProjectConnectionManager.getInstance(getProject());
        projectConnectionManager.reset();

        Module[] modules =  ModuleManager.getInstance(getProject()).getModules();
        for (Module module : modules) {
            ModuleConnectionManager moduleConnectionManager = ModuleConnectionManager.getInstance(module);
            moduleConnectionManager.reset();
        }
    }

    @Override
    public void disposeUIResources() {
        ProjectConnectionManager projectConnectionManager = ProjectConnectionManager.getInstance(getProject());
        projectConnectionManager.disposeUIResources();

        Module[] modules =  ModuleManager.getInstance(getProject()).getModules();
        for (Module module : modules) {
            ModuleConnectionManager moduleConnectionManager = ModuleConnectionManager.getInstance(module);
            moduleConnectionManager.disposeUIResources();
        }
        super.disposeUIResources();
    }

    public void readConfiguration(Element element) throws InvalidDataException {

    }

    public void writeConfiguration(Element element) throws WriteExternalException {
        
    }
}
