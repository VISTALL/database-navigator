package com.dci.intellij.dbn.object.common.list;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectType;

import java.util.List;

public interface DBObjectList<T extends DBObject> extends BrowserTreeElement, DynamicContent<T> {
    String getName();
    DBObjectType getObjectType();
    boolean isHidden();
    void setHidden(boolean hidden);
    void addObject(T object);
    List<T> getObjects();
    T getObject(String name);
    T getObject(String name, String parentName);
}
