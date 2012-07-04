package com.dci.intellij.dbn.code.common.style.presets;

import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.language.common.element.ElementType;
import com.intellij.psi.PsiElement;

public abstract class CodeStylePresetImpl implements CodeStylePreset {
    private String id;
    private String name;

    protected CodeStylePresetImpl(String id, String name) {
        this.id = id;
        this.name = name;
        //CodeStylePresetsRegister.registerWrapPreset(this);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    protected static BasePsiElement getParentPsiElement(PsiElement psiElement) {
        PsiElement parentPsiElement = psiElement.getParent();
        if (parentPsiElement instanceof BasePsiElement) {
            return (BasePsiElement) parentPsiElement;
        }
        return null;
    }

    protected static ElementType getParentElementType(PsiElement psiElement) {
        BasePsiElement parentPsiElement = getParentPsiElement(psiElement);
        if (parentPsiElement != null) {
            return parentPsiElement.getElementType();
        }
        return null;
    }

}