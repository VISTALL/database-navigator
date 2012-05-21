package com.dci.intellij.dbn.common.content.dependency;

import com.dci.intellij.dbn.common.content.DynamicContent;

public class BasicContentDependency implements ContentDependency {
    private DynamicContent dynamicContent;
    private long changeTimestamp;
    private boolean isWeak;

    public BasicContentDependency(DynamicContent dynamicContent, boolean weak) {
        this.dynamicContent = dynamicContent;
        this.isWeak = weak;
        changeTimestamp = dynamicContent.getChangeTimestamp();
    }

    public void reset() {
        changeTimestamp = dynamicContent.getChangeTimestamp();        
    }

    public boolean isDirty() {
        return changeTimestamp != dynamicContent.getChangeTimestamp();
    }

    public DynamicContent getDynamicContent() {
        return dynamicContent;
    }

    public boolean isWeak() {
        return isWeak;
    }

    public void setWeak(boolean weak) {
        isWeak = weak;
    }

    public void dispose() {
        dynamicContent = null;
    }
}
