package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.intellij.lang.PsiBuilder;

public class NestedRangeStartMarker {
    private PsiBuilder.Marker builderMarker;
    private ParsePathNode parentNode;
    private int offset;

    public NestedRangeStartMarker(ParsePathNode parentNode, int offset) {
        this.parentNode = parentNode;
        this.offset = offset;
    }

    public NestedRangeStartMarker(ParsePathNode parentNode, int offset, PsiBuilder.Marker builderMarker) {
        this.builderMarker = builderMarker;
        this.parentNode = parentNode;
        this.offset = offset;
    }

    public void setBuilderMarker(PsiBuilder.Marker builderMarker) {
        this.builderMarker = builderMarker;
    }

    public PsiBuilder.Marker getBuilderMarker() {
        return builderMarker;
    }

    public NestedRangeStartMarker(int offset) {
        this.offset = offset;
    }

    public ParsePathNode getParentNode() {
        return parentNode;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return offset + "";
    }
}
