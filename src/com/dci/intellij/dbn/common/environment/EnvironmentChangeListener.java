package com.dci.intellij.dbn.common.environment;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface EnvironmentChangeListener extends EventListener {
    Topic<EnvironmentChangeListener> TOPIC = Topic.create("Environment type changed", EnvironmentChangeListener.class);
    void environmentTypeChanged(EnvironmentType environmentType);
    void environmentVisibilitySettingsChanged();
}
