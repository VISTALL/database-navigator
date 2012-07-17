package com.dci.intellij.dbn.connection.config;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface ConnectionManagerSettingsListener extends EventListener {
    public static Topic<ConnectionManagerSettingsListener> TOPIC = Topic.create("Connections changed", ConnectionManagerSettingsListener.class);
    void settingsChanged();
}
