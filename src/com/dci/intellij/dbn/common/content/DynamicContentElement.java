package com.dci.intellij.dbn.common.content;

import com.intellij.openapi.Disposable;

public interface DynamicContentElement extends Disposable, Comparable {
    boolean isDisposed();
    String getName();
    String getInternalName();
    String getDescription();
    void reload();
}
