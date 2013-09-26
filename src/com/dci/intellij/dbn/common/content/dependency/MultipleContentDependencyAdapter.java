package com.dci.intellij.dbn.common.content.dependency;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.dispose.DisposeUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;

import java.util.HashSet;
import java.util.Set;

public class MultipleContentDependencyAdapter extends BasicDependencyAdapter implements ContentDependencyAdapter {
    private Set<ContentDependency> dependencies = new HashSet<ContentDependency>();
    private boolean isDisposed;

    public MultipleContentDependencyAdapter(ConnectionHandler connectionHandler, DynamicContent... sourceContents) {
        super(connectionHandler);
        for (DynamicContent sourceContent : sourceContents) {
            dependencies.add(new BasicContentDependency(sourceContent));
        }
    }

    public boolean shouldLoad() {
        // should reload if at least one dependency has been reloaded and is not dirty
        for (ContentDependency dependency : dependencies) {
            if (dependency.isDirty() && !dependency.getSourceContent().isDirty()) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldLoadIfDirty() {
        if (isConnectionValid()) {
            for (ContentDependency dependency : dependencies) {
                DynamicContent dynamicContent = dependency.getSourceContent();
                if (!dynamicContent.isLoaded() || dynamicContent.isDirty()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean hasDirtyDependencies() {
        for (ContentDependency dependency : dependencies) {
            if (dependency.getSourceContent().isDirty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void beforeLoad() {
        // assuming all dependencies are hard, load them first
        for (ContentDependency dependency : dependencies) {
            dependency.getSourceContent().load();
        }
    }

    @Override
    public void afterLoad() {
        for (ContentDependency dependency : dependencies) {
            dependency.reset();
        }
    }

    @Override
    public void beforeReload(DynamicContent dynamicContent) {
        beforeLoad();
    }

    @Override
    public void afterReload(DynamicContent dynamicContent) {
        afterLoad();
    }

    @Override
    public void dispose() {
        if (!isDisposed) {
            isDisposed = true;
            DisposeUtil.disposeCollection(dependencies);
            super.dispose();
        }
    }
}
