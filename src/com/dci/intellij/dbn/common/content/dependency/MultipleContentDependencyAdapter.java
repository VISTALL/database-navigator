package com.dci.intellij.dbn.common.content.dependency;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.connection.ConnectionHandler;

import java.util.HashSet;
import java.util.Set;

public class MultipleContentDependencyAdapter extends BasicDependencyAdapter implements ContentDependencyAdapter {
    private Set<BasicContentDependency> dependencies = new HashSet<BasicContentDependency>();

    public MultipleContentDependencyAdapter(ConnectionHandler connectionHandler, DynamicContent[] sourceContents) {
        super(connectionHandler);
        for (DynamicContent sourceContent : sourceContents) {
            dependencies.add(new BasicContentDependency(sourceContent, false));
        }
    }

    public boolean shouldLoad() {
        // should reload if at least one dependency has been reloaded and is not dirty
        for (BasicContentDependency dependency : dependencies) {
            if (dependency.isDirty() && !dependency.getDynamicContent().isDirty()) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldLoadIfDirty() {
        if (isConnectionValid()) {
            for (BasicContentDependency dependency : dependencies) {
                DynamicContent dynamicContent = dependency.getDynamicContent();
                if (!dynamicContent.isLoaded() || dynamicContent.isDirty()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean hasDirtyDependencies() {
        for (BasicContentDependency dependency : dependencies) {
            if (dependency.getDynamicContent().isDirty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void beforeLoad() {
        // assuming all dependencies are hard, load them first
        for (BasicContentDependency dependency : dependencies) {
            dependency.getDynamicContent().load();
        }
    }

    @Override
    public void afterLoad() {
        for (BasicContentDependency dependency : dependencies) {
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
}
