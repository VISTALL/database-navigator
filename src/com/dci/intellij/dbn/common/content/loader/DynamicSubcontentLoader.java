package com.dci.intellij.dbn.common.content.loader;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentElement;
import com.dci.intellij.dbn.common.content.dependency.SubcontentDependencyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * This loader is to be used from building the elements of a dynamic content, based on a source content.
 * e.g. Constraints of a table are loaded from the complete list of constraints of a Schema.
 */
public abstract class DynamicSubcontentLoader<T extends DynamicContentElement> implements DynamicContentLoader<T> {
    private boolean optimized;

    protected DynamicSubcontentLoader(boolean optimized) {
        this.optimized = optimized;
    }

    /**
     * Check if the source element matches the criteria of the dynamic content.
     * If it matches, it will be added as
     */
    public abstract boolean match(T sourceElement, DynamicContent dynamicContent);

    public void loadContent(DynamicContent<T> dynamicContent) throws DynamicContentLoaderException {
        List<T> list = null;
        boolean matchedOnce = false;
        SubcontentDependencyAdapter dependencyAdapter = (SubcontentDependencyAdapter) dynamicContent.getDependencyAdapter();
        for (Object object : dependencyAdapter.getSourceContent().getElements()) {
            if (dynamicContent.isDisposed()) return; // stop if disposed during loading

            T element = (T) object;
            if (match(element, dynamicContent) && dynamicContent.accepts(element)) {
                matchedOnce = true;
                if (list == null) list = new ArrayList<T>();
                list.add(element);
            }
            else if (matchedOnce && optimized) {
                // the optimization check assumes that source content is sorted
                // such as all matching elements are building a consecutive segment in the source content.
                // If at least one match occurred and current element does not match any more,
                // => there are no matching elements left in the source content, hence break the loop
                break;
            }
        }
        dynamicContent.setElements(list);
        //dynamicContent.setTemporary(false);
    }

    public void reloadContent(DynamicContent<T> dynamicContent) throws DynamicContentLoaderException {

    }
}
