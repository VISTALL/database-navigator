package com.dci.intellij.dbn.browser.ui;

import com.intellij.util.messages.Topic;

public interface BrowserSelectionChangeListener {
    public static Topic<BrowserSelectionChangeListener> TOPIC = Topic.create("Browser selection changed", BrowserSelectionChangeListener.class);
    void browserSelectionChanged();
}
