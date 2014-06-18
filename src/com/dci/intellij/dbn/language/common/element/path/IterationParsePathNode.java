package com.dci.intellij.dbn.language.common.element.path;

import com.dci.intellij.dbn.language.common.element.ElementType;

public class IterationParsePathNode extends ParsePathNode{
    private int currentOffset;
    public IterationParsePathNode(ElementType elementType, ParsePathNode parent, int startOffset, int position) {
        super(elementType, parent, startOffset, position);
        currentOffset = startOffset;
    }

    public int getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(int currentOffset) {
        this.currentOffset = currentOffset;
    }

    public boolean isRecursive(int currentOffset) {
        ParsePathNode parseNode = this.getParent();
        while (parseNode != null) {
            if (parseNode instanceof IterationParsePathNode) {
                IterationParsePathNode iterationParsePathNode = (IterationParsePathNode) parseNode;
                if (iterationParsePathNode.getElementType() == getElementType() &&
                        iterationParsePathNode.getCurrentOffset() == currentOffset) {
                    return true;
                }

            }
            parseNode = parseNode.getParent();
        }
        return false;
    }
}
