package com.dci.intellij.dbn.common.environment;

import com.intellij.util.messages.Topic;

public interface EnvironmentChangeListener {
    Topic<EnvironmentChangeListener> TOPIC = Topic.create("Environment type changed", EnvironmentChangeListener.class);
    void environmentTypeChanged(EnvironmentType environmentType);
    void environmentVisibilitySettingsChanged();
}
