package com.dci.intellij.dbn.common.content.dependency;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentType;
import com.dci.intellij.dbn.connection.GenericDatabaseElement;

public class DynamicContentDependency implements ContentDependency {
    private GenericDatabaseElement parent;
    private long changeTimestamp;
    private DynamicContentType dynamicContentType;
    private boolean isWeak;

    public DynamicContentDependency(GenericDatabaseElement parent, DynamicContentType dynamicContentType, boolean isWeak) {
        this.parent = parent;
        this.dynamicContentType = dynamicContentType;
        this.isWeak = isWeak;
        changeTimestamp = getDynamicContent().getChangeTimestamp();
    }

    public void reset() {
        changeTimestamp = getDynamicContent().getChangeTimestamp();
    }

    public boolean isDirty() {
        return changeTimestamp != getDynamicContent().getChangeTimestamp();
    }

    public DynamicContent getDynamicContent() {
        return getParent().getDynamicContent(dynamicContentType);
    }

    public GenericDatabaseElement getParent() {
        parent = parent.getUndisposedElement();
        return parent;
    }

    public boolean isWeak() {
        return isWeak;
    }

    public void setWeak(boolean weak) {
        isWeak = weak;
    }

    public void dispose() {
        parent = null;
    }
}