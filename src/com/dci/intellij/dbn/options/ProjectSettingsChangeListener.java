package com.dci.intellij.dbn.options;

import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;

public interface ProjectSettingsChangeListener {
    public static Topic<ProjectSettingsChangeListener> TOPIC = Topic.create("Setting changes notification", ProjectSettingsChangeListener.class);
    void projectSettingsChanged(Project project);
}
