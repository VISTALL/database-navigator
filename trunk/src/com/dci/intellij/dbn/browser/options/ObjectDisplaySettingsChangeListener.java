package com.dci.intellij.dbn.browser.options;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface ObjectDisplaySettingsChangeListener extends EventListener {
    public static Topic<ObjectDisplaySettingsChangeListener> TOPIC = Topic.create("DBNavigator - Object display settings changed", ObjectDisplaySettingsChangeListener.class);
    void displayDetailsChanged();
}
