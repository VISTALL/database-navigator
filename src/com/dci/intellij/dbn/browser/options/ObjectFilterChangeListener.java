package com.dci.intellij.dbn.browser.options;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.common.filter.Filter;
import com.intellij.util.messages.Topic;

public interface ObjectFilterChangeListener {
    public static Topic<ObjectFilterChangeListener> TOPIC = Topic.create("Object filter changed", ObjectFilterChangeListener.class);
    void filterChanged(Filter<BrowserTreeElement> filter);
}
