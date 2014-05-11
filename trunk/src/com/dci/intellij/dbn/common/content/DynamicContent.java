package com.dci.intellij.dbn.common.content;

import com.dci.intellij.dbn.common.content.dependency.ContentDependencyAdapter;
import com.dci.intellij.dbn.common.content.loader.DynamicContentLoadInterruptedException;
import com.dci.intellij.dbn.common.content.loader.DynamicContentLoader;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.GenericDatabaseElement;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DynamicContent<T extends DynamicContentElement> extends Disposable {
    /**
     * Checks if the loading of the content is required.
     * e.g. after the content is once loaded, it only has to be loaded again if dependencies are dirty.
     */
    boolean shouldLoad();

    /**
     * Loads the content. It is typically called every time the content is queried.
     * The check shouldLoad() is made before to avoid pointless loads.
     */
    void load();

    /**
     * Rebuilds the content. This method is called when reloading the content
     * is triggered deliberately by the user directly or by a ddl change.
     * @param recursive
     */
    void reload(boolean recursive);

    /**
     * The timestamp of the last change on the content.
     */
    long getChangeTimestamp();

    /**
     * A load attempt has been made already
     */
    boolean isLoaded();

    boolean isSubContent();

    boolean areDependenciesLoaded();

    /**
     * Content is currently loading
     */
    boolean isLoading();

    /**
     * The content has been loaded but with errors (e.g. because of database connectivity problems)
     */
    boolean isDirty();

    boolean isDisposed();

    void setDirty(boolean dirty);

    Project getProject();
    String getContentDescription();

    @NotNull List<T> getElements();
    T getElement(String name);
    void setElements(@Nullable List<T> elements);
    int size();

    GenericDatabaseElement getParent();
    DynamicContentLoader getLoader();
    ContentDependencyAdapter getDependencyAdapter();
    ConnectionHandler getConnectionHandler();

    void loadInBackground();

    void updateChangeTimestamp();

    void removeElements(List<T> elements);
    void addElements(List<T> elements);
    String getName();

    boolean accepts(T element);

    void check() throws DynamicContentLoadInterruptedException;
}
