package com.dci.intellij.dbn.browser.options;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface ObjectDisplayDetailsChangeListener extends EventListener {
    public static Topic<ObjectDisplayDetailsChangeListener> TOPIC = Topic.create("Display details changed", ObjectDisplayDetailsChangeListener.class);
    void displayDetailsChanged();
}
