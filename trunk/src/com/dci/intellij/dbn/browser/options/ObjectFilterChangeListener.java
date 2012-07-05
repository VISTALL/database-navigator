package com.dci.intellij.dbn.browser.options;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.common.filter.Filter;
import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface ObjectFilterChangeListener extends EventListener {
    public static Topic<ObjectFilterChangeListener> TOPIC = Topic.create("Object filter changed", ObjectFilterChangeListener.class);
    void filterChanged(Filter<BrowserTreeElement> filter);
}
