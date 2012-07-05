package com.dci.intellij.dbn.browser.ui;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface BrowserSelectionChangeListener extends EventListener {
    public static Topic<BrowserSelectionChangeListener> TOPIC = Topic.create("Browser selection changed", BrowserSelectionChangeListener.class);
    void browserSelectionChanged();
}
