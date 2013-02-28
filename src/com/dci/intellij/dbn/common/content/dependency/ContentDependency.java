package com.dci.intellij.dbn.common.content.dependency;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentType;
import com.dci.intellij.dbn.connection.GenericDatabaseElement;
import com.intellij.openapi.Disposable;

public class ContentDependency implements Disposable {
    private GenericDatabaseElement sourceContentOwner;
    private DynamicContentType sourceContentType;
    private DynamicContent sourceContent;
    protected long changeTimestamp;

    public ContentDependency(DynamicContent sourceContent) {
        this.sourceContent = sourceContent;
        reset();
    }

    public ContentDependency(GenericDatabaseElement sourceContentOwner, DynamicContentType sourceContentType) {
        this.sourceContentOwner = sourceContentOwner;
        this.sourceContentType = sourceContentType;
        reset();
    }

    public DynamicContent getSourceContent() {
        return sourceContent != null ?  sourceContent :
                sourceContentOwner.getDynamicContent(sourceContentType);
    }

    public void reset() {
        changeTimestamp = getSourceContent().getChangeTimestamp();
    }

    public boolean isDirty() {
        return changeTimestamp != getSourceContent().getChangeTimestamp();
    }

    public void dispose() {
        sourceContent = null;
        sourceContentOwner = null;
    }
}
