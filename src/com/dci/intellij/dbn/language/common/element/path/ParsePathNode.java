package com.dci.intellij.dbn.language.common.element.path;

import com.dci.intellij.dbn.language.common.element.ElementType;

public class ParsePathNode extends BasicPathNode {
    private int builderOffset;

    public ParsePathNode(ElementType elementType, ParsePathNode parent, int position, int builderOffset) {
        super(elementType, parent, position);
        this.builderOffset = builderOffset;
    }

    public ParsePathNode createVariant(int builderOffset, int position) {
        ParsePathNode variant = new ParsePathNode(getElementType(), getParent(), position, builderOffset);
        detach();
        return variant;
    }

    public ParsePathNode getParent() {
        return (ParsePathNode) super.getParent();
    }

    public int getBuilderOffset() {
        return builderOffset;
    }

    public boolean isRecursive() {
        ParsePathNode parseNode = this.getParent();
        while (parseNode != null) {
            if (parseNode.getElementType() == getElementType() &&
                parseNode.getBuilderOffset() == getBuilderOffset()) {
                return true;
            }
            parseNode = parseNode.getParent();
        }
        return false;
    }
}

