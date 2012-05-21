package com.dci.intellij.dbn.language.common.element.path;

import com.dci.intellij.dbn.language.common.element.ElementType;

public class ParsePathNode extends BasicPathNode {
    private int builderOffset;
    private boolean isVirtual;

    public ParsePathNode(ElementType elementType, ParsePathNode parent, int position) {
        super(elementType, parent, position);
    }

    public ParsePathNode createVariant(int builderOffset, int position) {
        ParsePathNode variant = new ParsePathNode(getElementType(), getParent(), position);
        variant.builderOffset  = builderOffset;
        variant.isVirtual = isVirtual;
        return variant;
    }

    public ParsePathNode getParent() {
        return (ParsePathNode) super.getParent();
    }

    public int getBuilderOffset() {
        return builderOffset;
    }

    public void markVirtual() {
        if (!isVirtual) {
            isVirtual = true;
            ParsePathNode parent = getParent();
            if (parent != null) {
                parent.markVirtual();
            }
        }
    }

    public boolean isVirtual() {
        return isVirtual;
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

