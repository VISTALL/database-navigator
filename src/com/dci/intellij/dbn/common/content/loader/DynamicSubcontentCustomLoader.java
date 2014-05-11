package com.dci.intellij.dbn.common.content.loader;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentElement;
import com.dci.intellij.dbn.common.content.dependency.SubcontentDependencyAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class DynamicSubcontentCustomLoader<T extends DynamicContentElement> implements DynamicContentLoader<T> {
    public abstract T resolveElement(DynamicContent<T> dynamicContent, DynamicContentElement sourceElement);

    public void reloadContent(DynamicContent<T> dynamicContent) throws DynamicContentLoadException, DynamicContentLoadInterruptedException {
        loadContent(dynamicContent);
    }

    public void loadContent(DynamicContent<T> dynamicContent) throws DynamicContentLoadException, DynamicContentLoadInterruptedException {
        List<T> list = null;
        SubcontentDependencyAdapter dependencyAdapter = (SubcontentDependencyAdapter) dynamicContent.getDependencyAdapter();
        for (Object object : dependencyAdapter.getSourceContent().getElements()) {
            dynamicContent.check();
            DynamicContentElement sourceElement = (DynamicContentElement) object;
            T element = resolveElement(dynamicContent, sourceElement);
            if (element != null && dynamicContent.accepts(element)) {
                dynamicContent.check();
                if (list == null) list = new ArrayList<T>();
                list.add(element);
            }
        }
        dynamicContent.setElements(list);
    }
}
