package com.dci.intellij.dbn.language.common.element.path;

import com.dci.intellij.dbn.language.common.element.DBNElementType;

public class BasicPathNode implements PathNode {
    private int currentSiblingPosition;
    private PathNode parent;
    private DBNElementType elementType;

    public BasicPathNode(DBNElementType elementType, PathNode parent, int currentSiblingPosition) {
        this.elementType = elementType;
        this.parent = parent;
        this.currentSiblingPosition = currentSiblingPosition;
    }

    public PathNode getParent() {
        return parent;
    }

    public void setParent(PathNode parent) {
        this.parent = parent;
    }

    public int getCurrentSiblingPosition() {
        return currentSiblingPosition;
    }

    public void setCurrentSiblingPosition(int currentSiblingPosition) {
        this.currentSiblingPosition = currentSiblingPosition;
    }

    public DBNElementType getElementType() {
        return elementType;
    }

    public PathNode getRootPathNode() {
        PathNode pathNode = parent;
        while (pathNode != null) {
            PathNode parentPathNode = pathNode.getParent();
            if (parentPathNode == null) {
                return pathNode;
            }
            pathNode = parentPathNode;
        }
        return this;
    }

    public void setElementType(DBNElementType elementType) {
        this.elementType = elementType;
    }

    public boolean isRecursive() {
        PathNode node = this.getParent();
        while (node != null) {
            if (node.getElementType() == getElementType()) {
                return true;
            }
            node = node.getParent();
        }
        return false;
    }

    public boolean isRecursive(DBNElementType elementType) {
        if (getElementType() == elementType) {
            return true;
        }
        PathNode node = this.getParent();
        while (node != null) {
            if (node.getElementType() == elementType) {
                return true;
            }
            node = node.getParent();
        }
        return false;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        PathNode parent = this;
        while (parent != null) {
            buffer.insert(0, '/');
            buffer.insert(0, parent.getElementType().getId());
            parent = parent.getParent();
        }
        return buffer.toString();
    }

    public void detach() {
        parent = null;
    }
}
