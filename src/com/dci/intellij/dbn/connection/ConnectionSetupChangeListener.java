package com.dci.intellij.dbn.connection;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface ConnectionSetupChangeListener extends EventListener {
    public static Topic<ConnectionSetupChangeListener> TOPIC = Topic.create("Connection setup changed", ConnectionSetupChangeListener.class);
    void connectionSetupChanged();
}
