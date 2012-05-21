package com.dci.intellij.dbn.language.common.element.impl;

import com.dci.intellij.dbn.language.common.element.ElementTypeBundle;
import com.dci.intellij.dbn.language.common.element.UnknownElementType;
import com.dci.intellij.dbn.language.common.element.lookup.UnknownElementTypeLookupCache;
import com.dci.intellij.dbn.language.common.element.parser.UnknownElementTypeParser;
import com.dci.intellij.dbn.language.common.psi.UnknownPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public class UnknownElementTypeImpl extends AbstractElementType implements UnknownElementType {

    public UnknownElementTypeImpl(ElementTypeBundle bundle) {
        super(bundle, null, "UNKNOWN", "Unidentified element type.");
    }

    @Override
    public UnknownElementTypeLookupCache createLookupCache() {
        return new UnknownElementTypeLookupCache(this);
    }

    @Override
    public UnknownElementTypeParser createParser() {
        return new UnknownElementTypeParser(this);
    }

    public boolean isLeaf() {
        return true;
    }

    public String getDebugName() {
        return getId();
    }

    public PsiElement createPsiElement(ASTNode astNode) {
        return new UnknownPsiElement(astNode, this);
    }

}
