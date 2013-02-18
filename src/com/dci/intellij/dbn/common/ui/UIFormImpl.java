package com.dci.intellij.dbn.common.ui;

import com.dci.intellij.dbn.common.environment.options.EnvironmentSettings;
import com.dci.intellij.dbn.options.general.GeneralProjectSettings;
import com.intellij.openapi.project.Project;

public abstract class UIFormImpl extends UIUtil implements UIForm {
    boolean disposed;

    public EnvironmentSettings getEnvironmentSettings(Project project) {
        return GeneralProjectSettings.getInstance(project).getEnvironmentSettings();
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public void dispose() {
        disposed = true;
    }

}
