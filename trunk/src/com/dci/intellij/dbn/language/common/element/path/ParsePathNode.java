package com.dci.intellij.dbn.language.common.element.path;

import com.dci.intellij.dbn.language.common.element.ElementType;

public class ParsePathNode extends BasicPathNode {
    private int startOffset;

    public ParsePathNode(ElementType elementType, ParsePathNode parent, int startOffset, int indexInParent) {
        super(elementType, parent, indexInParent);
        this.startOffset = startOffset;
    }

    public ParsePathNode createVariant(int builderOffset, int position) {
        ParsePathNode variant = new ParsePathNode(getElementType(), getParent(), builderOffset, position);
        detach();
        return variant;
    }

    public ParsePathNode getParent() {
        return (ParsePathNode) super.getParent();
    }

    public int getStartOffset() {
        return startOffset;
    }

    public boolean isRecursive() {
        ParsePathNode parseNode = this.getParent();
        while (parseNode != null) {
            if (parseNode.getElementType() == getElementType() &&
                parseNode.getStartOffset() == getStartOffset()) {
                return true;
            }
            parseNode = parseNode.getParent();
        }
        return false;
    }
}

