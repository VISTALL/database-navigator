package com.dci.intellij.dbn.language.common;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

public class ScopeProcessor implements PsiScopeProcessor {
    public boolean execute(PsiElement psiElement, ResolveState resolveState) {
        return false;
    }

    public <T> T getHint(Key<T> tKey) {
        return null;
    }

    public <T> T getHint(Class<T> hintClass) {
        return null;
    }

    public void handleEvent(Event event, Object associated) {

    }
}
