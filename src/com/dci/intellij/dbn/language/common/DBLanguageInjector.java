package com.dci.intellij.dbn.language.common;

import com.dci.intellij.dbn.language.common.psi.InjectedLanguagePsiElement;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;

/**
 * @deprecated
 */
public class DBLanguageInjector implements LanguageInjector {
    public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces injectionPlacesRegistrar) {
        if (host instanceof InjectedLanguagePsiElement) {
            InjectedLanguagePsiElement psiElement = (InjectedLanguagePsiElement) host;
            injectionPlacesRegistrar.addPlace(psiElement.getLanguage(), new TextRange(0, psiElement.getTextLength()), null, null);
        }
    }
}
