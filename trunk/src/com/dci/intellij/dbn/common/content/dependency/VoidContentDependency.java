package com.dci.intellij.dbn.common.content.dependency;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.VoidDynamicContent;

public class VoidContentDependency extends ContentDependency{
    public static final VoidContentDependency INSTANCE = new VoidContentDependency();

    private VoidContentDependency() {

    }

    @Override
    public DynamicContent getSourceContent() {
        return VoidDynamicContent.INSTANCE;
    }

    @Override
    public void dispose() {

    }
}
