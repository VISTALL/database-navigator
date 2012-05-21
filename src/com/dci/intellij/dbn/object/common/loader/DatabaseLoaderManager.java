package com.dci.intellij.dbn.object.common.loader;

import com.dci.intellij.dbn.common.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DatabaseLoaderManager extends AbstractProjectComponent {
    private DatabaseLoaderQueue loaderQueue;

    private DatabaseLoaderManager(Project project) {
        super(project);
    }

    public static DatabaseLoaderManager getInstance(Project project) {
        return project.getComponent(DatabaseLoaderManager.class);
    }

    @NonNls
    @NotNull
    public String getComponentName() {
        return "DBNavigator.Project.DatabaseLoaderManager";
    }

    public void disposeComponent() {
        super.disposeComponent();
        if (loaderQueue != null) {
            loaderQueue.dispose();
            loaderQueue = null;
        }
    }
}
