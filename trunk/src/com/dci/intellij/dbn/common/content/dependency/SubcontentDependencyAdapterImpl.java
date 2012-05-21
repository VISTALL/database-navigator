package com.dci.intellij.dbn.common.content.dependency;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentType;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.GenericDatabaseElement;

public class SubcontentDependencyAdapterImpl extends BasicDependencyAdapter implements SubcontentDependencyAdapter {
    private ContentDependency contentDependency;
    private boolean isDisposed;

    public SubcontentDependencyAdapterImpl(ConnectionHandler connectionHandler, DynamicContent sourceContent, boolean isWeakDependency) {
        super(connectionHandler);
        contentDependency = new BasicContentDependency(sourceContent, isWeakDependency);
    }

    public SubcontentDependencyAdapterImpl(GenericDatabaseElement sourceContentHolder, DynamicContentType sourceContentType, boolean isWeakDependency) {
        super(sourceContentHolder.getConnectionHandler());
        contentDependency = new DynamicContentDependency(sourceContentHolder, sourceContentType, isWeakDependency);
    }


    public DynamicContent getSourceContent() {
        return contentDependency.getDynamicContent();
    }

    @Override
    public boolean shouldLoad() {
        DynamicContent sourceContent = contentDependency.getDynamicContent();
        if (!isDisposed && sourceContent.shouldLoad()) {
            if (contentDependency.isWeak()) {
                sourceContent.loadInBackground();
            } else {
                sourceContent.load();    
            }
        }
        // should reload if the source has been reloaded and is not dirty
        return !sourceContent.isDirty() && contentDependency.isDirty();
    }

    @Override
    public boolean shouldLoadIfDirty() {
        DynamicContent sourceContent = contentDependency.getDynamicContent();

        boolean isLoadedAndNotDirty = isConnectionValid() && sourceContent.isLoaded() && !sourceContent.isDirty();
        boolean shouldReloadAndIsNotLoading = !sourceContent.isLoading() && sourceContent.shouldLoad();

        return isLoadedAndNotDirty || shouldReloadAndIsNotLoading;
    }

    public boolean hasDirtyDependencies() {
        DynamicContent sourceContent = contentDependency.getDynamicContent();
        return sourceContent.isLoading() || sourceContent.isDirty();
    }

    @Override
    public void beforeLoad() {
        if (!contentDependency.isWeak()) {
            contentDependency.getDynamicContent().load();
        }
    }

    @Override
    public void afterLoad() {
        contentDependency.reset();
    }

    public void beforeReload(DynamicContent dynamicContent) {
        DynamicContent sourceContent = contentDependency.getDynamicContent();
        sourceContent.getDependencyAdapter().beforeReload(sourceContent);
        sourceContent.removeElements(dynamicContent.getElements());
    }

    public void afterReload(DynamicContent dynamicContent) {
        DynamicContent sourceContent = contentDependency.getDynamicContent();
        if (sourceContent.getClass().isAssignableFrom(dynamicContent.getClass())) {
            sourceContent.addElements(dynamicContent.getElements());
            sourceContent.getDependencyAdapter().afterReload(sourceContent);
            sourceContent.updateChangeTimestamp();
            contentDependency.reset();
        }
    }

    @Override
    public boolean isSourceContentLoaded() {
        return getSourceContent().isLoaded();
    }

    public void dispose() {
        if (!isDisposed) {
            isDisposed = true;
            contentDependency.dispose();
            contentDependency = null;
            super.dispose();
        }
    }


}
