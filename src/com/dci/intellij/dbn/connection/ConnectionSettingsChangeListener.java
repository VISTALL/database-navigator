package com.dci.intellij.dbn.connection;

import com.intellij.util.messages.Topic;

public interface ConnectionSettingsChangeListener {
    public static Topic<ConnectionSettingsChangeListener> TOPIC = Topic.create("Connection settings changed", ConnectionSettingsChangeListener.class);
    void connectionSettingsChanged(String connectionId);
}
