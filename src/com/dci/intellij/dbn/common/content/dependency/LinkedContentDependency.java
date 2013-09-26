package com.dci.intellij.dbn.common.content.dependency;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentType;
import com.dci.intellij.dbn.common.content.VoidDynamicContent;
import com.dci.intellij.dbn.connection.GenericDatabaseElement;

public class LinkedContentDependency extends ContentDependency {
    private GenericDatabaseElement sourceContentOwner;
    private DynamicContentType sourceContentType;

    public LinkedContentDependency(GenericDatabaseElement sourceContentOwner, DynamicContentType sourceContentType) {
        this.sourceContentOwner = sourceContentOwner;
        this.sourceContentType = sourceContentType;
        reset();
    }

    public DynamicContent getSourceContent() {
        return sourceContentOwner == null ?
                VoidDynamicContent.INSTANCE :
                sourceContentOwner.getDynamicContent(sourceContentType);
    }

    public void dispose() {
        sourceContentOwner = null;
    }
}
