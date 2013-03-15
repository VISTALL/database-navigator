package com.dci.intellij.dbn.common;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;

public abstract class AbstractProjectComponent implements ProjectComponent{
    private Project project;

    protected AbstractProjectComponent(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    public void initComponent() {
    }

    public void disposeComponent() {
        project = null;
    }
}
