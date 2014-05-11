package com.dci.intellij.dbn.common.content.loader;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentElement;

public interface DynamicContentLoader<T extends DynamicContentElement> {
    void loadContent(DynamicContent<T> dynamicContent) throws DynamicContentLoadException, DynamicContentLoadInterruptedException;
    void reloadContent(DynamicContent<T> dynamicContent) throws DynamicContentLoadException, DynamicContentLoadInterruptedException;
}
