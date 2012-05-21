package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.common.content.DynamicContent;

import java.util.HashSet;
import java.util.Set;

public class LoadMonitor {
    private Set<DynamicContent> dynamicContents = new HashSet<DynamicContent>();

    public void registerLoadedContent(DynamicContent longRunner) {
        dynamicContents.add(longRunner);
    }

    public void unregisterLoadedContent(DynamicContent longRunner) {
        dynamicContents.remove(longRunner);
    }

    public int getLoadingContentsCount() {
        return dynamicContents.size();
    }
}
