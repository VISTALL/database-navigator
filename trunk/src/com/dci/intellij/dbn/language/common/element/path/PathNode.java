package com.dci.intellij.dbn.language.common.element.path;

import com.dci.intellij.dbn.language.common.element.DBNElementType;

public interface PathNode {
    PathNode getParent();

    int getCurrentSiblingPosition();

    DBNElementType getElementType();

    PathNode getRootPathNode();

    boolean isRecursive();

    boolean isRecursive(DBNElementType elementType);

    void detach();
}
