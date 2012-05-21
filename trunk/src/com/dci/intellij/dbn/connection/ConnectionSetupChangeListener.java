package com.dci.intellij.dbn.connection;

import com.intellij.util.messages.Topic;

public interface ConnectionSetupChangeListener {
    public static Topic<ConnectionSetupChangeListener> TOPIC = Topic.create("Connection setup changed", ConnectionSetupChangeListener.class);
    void connectionSetupChanged();
}
