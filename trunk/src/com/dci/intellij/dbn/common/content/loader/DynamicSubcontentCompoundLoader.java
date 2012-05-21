package com.dci.intellij.dbn.common.content.loader;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentElement;
import com.dci.intellij.dbn.common.content.dependency.SubcontentDependencyAdapter;

public abstract class DynamicSubcontentCompoundLoader<T extends DynamicContentElement>extends DynamicSubcontentLoader<T> {
    protected DynamicSubcontentCompoundLoader(boolean optimized) {
        super(optimized);
    }

    public abstract DynamicContentLoader<T> getAlternativeLoader();

    public void loadContent(DynamicContent<T> dynamicContent) throws DynamicContentLoaderException {
        SubcontentDependencyAdapter dependencyAdapter = (SubcontentDependencyAdapter) dynamicContent.getDependencyAdapter();
        DynamicContent sourceContent = dependencyAdapter.getSourceContent();

        boolean sourceContentShouldLoad = !sourceContent.isLoaded() || sourceContent.isDirty();
        boolean performAlternativeLoading = sourceContentShouldLoad && !DynamicContentLoaderRegistry.isBulkLoad();

        if (performAlternativeLoading) {
            // queue this for reload since alternative loader creates temporary elements
            //sourceContent.queueForLoading(dynamicContent);

            sourceContent.loadInBackground();
            //sourceContent.load();

            DynamicContentLoader<T> alternativeLoader = getAlternativeLoader();
            alternativeLoader.loadContent(dynamicContent);
            //dynamicContent.setDirty(true);

        } else {
            // if master list is loaded, run inexpensive nested loading
            super.loadContent(dynamicContent);
        }
    }

    public void reloadContent(DynamicContent<T> dynamicContent) throws DynamicContentLoaderException {
        DynamicContentLoader<T> alternativeLoader = getAlternativeLoader();
        alternativeLoader.loadContent(dynamicContent);
    }


}
