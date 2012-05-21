package com.dci.intellij.dbn.common.content.dependency;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.intellij.openapi.Disposable;

public interface ContentDependency extends Disposable {
    void reset();

    boolean isDirty();

    DynamicContent getDynamicContent();

    boolean isWeak();

    void setWeak(boolean weak);
}
