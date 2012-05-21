package com.dci.intellij.dbn.common.environment.options;

import com.dci.intellij.dbn.common.environment.EnvironmentTypeBundle;
import com.intellij.util.messages.Topic;

public interface EnvironmentPresentationChangeListener {
    Topic<EnvironmentPresentationChangeListener> TOPIC = Topic.create("Environment settings changed", EnvironmentPresentationChangeListener.class);
    void settingsChanged(EnvironmentTypeBundle environmentTypes);
}
