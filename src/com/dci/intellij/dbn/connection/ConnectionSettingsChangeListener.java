package com.dci.intellij.dbn.connection;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface ConnectionSettingsChangeListener extends EventListener {
    public static Topic<ConnectionSettingsChangeListener> TOPIC = Topic.create("Connection settings changed", ConnectionSettingsChangeListener.class);
    void connectionSettingsChanged(String connectionId);
}
