package com.dci.intellij.dbn.common.content;

import com.dci.intellij.dbn.common.content.dependency.ContentDependencyAdapter;
import com.dci.intellij.dbn.common.content.loader.DynamicContentLoader;
import com.dci.intellij.dbn.common.content.loader.DynamicContentLoaderException;
import com.dci.intellij.dbn.common.dispose.DisposeUtil;
import com.dci.intellij.dbn.common.filter.Filter;
import com.dci.intellij.dbn.common.util.CollectionUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.GenericDatabaseElement;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class DynamicContentImpl<T extends DynamicContentElement> implements DynamicContent<T> {
    public static final List EMPTY_LIST = new ArrayList(0);

    private long changeTimestamp = 0;
    private volatile boolean isLoading = false;
    private volatile boolean isLoaded = false;
    private volatile boolean isDirty = false;
    private volatile boolean isDisposed = false;
    private ConnectionHandler connectionHandler;

    private GenericDatabaseElement parent;
    protected DynamicContentLoader<T> loader;
    protected ContentDependencyAdapter dependencyAdapter;
    private boolean indexed;
    private Map<String, T> index;
    private int filterHashCode = 0;

    protected List<T> elements = EMPTY_LIST;

    protected DynamicContentImpl(GenericDatabaseElement parent, DynamicContentLoader<T> loader, ContentDependencyAdapter dependencyAdapter, boolean indexed) {
        this.parent = parent;
        this.loader = loader;
        this.dependencyAdapter = dependencyAdapter;
        this.connectionHandler = parent.getConnectionHandler();
        this.indexed = indexed;
    }

    public boolean accepts(T element) {
        Filter filter = getFilter();
        return filter == null || filter.accepts(element);
    }

    public abstract Filter getFilter();

    public GenericDatabaseElement getParent() {
        return parent;
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public DynamicContentLoader<T> getLoader() {
        return loader;
    }

    public ContentDependencyAdapter getDependencyAdapter() {
        return dependencyAdapter;
    }

    public long getChangeTimestamp() {
        return changeTimestamp;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public boolean isSourceContentLoaded() {
        return getDependencyAdapter().isSourceContentLoaded();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isDirty() {
        if (isDirty) {
            return true;
        }

        Filter filter = getFilter();
        return filter == null ?
                filterHashCode != 0 :
                filterHashCode != filter.hashCode();

        //return isDirty /*|| (elements.size() > 0 && elements.get(0).isDisposed())*/;
    }

    public boolean isDisposed() {
        return isDisposed;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public final synchronized void load() {
        if (!isLoading && shouldLoad()) {
            isLoading = true;
            performLoad();
            isLoaded = true;
            updateChangeTimestamp();
            isLoading = false;
        }
    }

    public void updateChangeTimestamp() {
        changeTimestamp = System.currentTimeMillis();
    }

    private void performLoad() {
        if (isDisposed) return;
        dependencyAdapter.beforeLoad();
        if (isDisposed) return;
        try {
            // mark first the dirty status since dirty dependencies may
            // become valid due to parallel background load
            isDirty = dependencyAdapter.hasDirtyDependencies();
            getLoader().loadContent(this);
        } catch (DynamicContentLoaderException e) {
            isDirty = true;
        }

        if (!isDisposed()) {
            dependencyAdapter.afterLoad();
        }
    }

    public final synchronized void reload(boolean recursive) {
        if (!isDisposed) {
            isLoading = true;
            performReload(recursive);
            isLoaded = true;
            isLoading = false;
            updateChangeTimestamp();
        }
    }

    private void performReload(boolean recursive) {
        dependencyAdapter.beforeReload(this);
        try {
            if (isDisposed()) return;
            getLoader().reloadContent(this);
            if (recursive) {
                for (T element : getElements()) {
                    element.reload();
                }
            }
        } catch (DynamicContentLoaderException e) {
            isDirty = true;
        } finally {
        }

        if (!isDisposed()) {
            dependencyAdapter.afterReload(this);
        }

    }


    /**
     * do whatever is needed after the content is loaded (e.g. refresh browser tree..)
     */
    public abstract void notifyChangeListeners();

    public synchronized void setElements(List<T> elements) {
        filterHashCode = getFilter() == null ? 0 : getFilter().hashCode();

        if (isDisposed || elements == null || elements.size() == 0) {
            elements = EMPTY_LIST;
            index = null;
        } else {
            Collections.sort(elements);

        }
        List<T> oldElements = this.elements;
        this.elements = elements;
        updateIndex();
        if (oldElements.size() != 0 || elements.size() != 0 ){
            notifyChangeListeners();
        }
        if (!dependencyAdapter.isSubContent() && oldElements.size() > 0 ) {
            DisposeUtil.disposeCollection(oldElements);
        }
    }

    public synchronized void removeElements(List<T> elements) {
        this.elements.removeAll(elements);
        updateIndex();
    }

    public synchronized void addElements(List<T> elements) {
        this.elements.addAll(elements);
        updateIndex();
    }

    @NotNull
    public synchronized List<T> getElements() {
        load();
        return elements;
    }

    protected void updateIndex() {
        if (indexed) {
            if (elements.size() > 0) {
                if (index == null)
                    index = new THashMap<String, T>(); else
                    index.clear();

                for (T element : elements) {
                    String name = element.getName().toUpperCase();
                    index.put(name, element);
                }
            } else {
                index = null;
            }
        }
    }

    public synchronized T getElement(String name) {
        if (indexed && index != null) {
            return index.get(name.toUpperCase());
        } else {
            List<T> elements = getElements();
            for (T element : elements) {
                if (element.getName().equalsIgnoreCase(name)) {
                    return element;
                }
            }
        }
        return null;
    }

    public synchronized int size() {
        return getElements().size();
    }

    public boolean shouldLoad() {
        if (isDisposed) return false;
        return !isLoaded ||
                (isDirty() && dependencyAdapter.shouldLoadIfDirty()) ||
                dependencyAdapter.shouldLoad();
    }

    public void dispose() {
        if (!isDisposed) {
            isDisposed = true;
            if (dependencyAdapter.isSubContent())
                elements.clear(); else
                DisposeUtil.disposeCollection(elements);
            CollectionUtil.clearMap(index);
            dependencyAdapter.dispose();
            connectionHandler = null;
            parent = null;
        }
    }
}
