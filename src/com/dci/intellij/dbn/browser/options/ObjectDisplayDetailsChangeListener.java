package com.dci.intellij.dbn.browser.options;

import com.intellij.util.messages.Topic;

public interface ObjectDisplayDetailsChangeListener {
    public static Topic<ObjectDisplayDetailsChangeListener> TOPIC = Topic.create("Display details changed", ObjectDisplayDetailsChangeListener.class);
    void displayDetailsChanged();
}
