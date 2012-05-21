package com.dci.intellij.dbn.language.common.psi;

import com.dci.intellij.dbn.language.common.element.ElementType;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * @deprecated
 */
public class InjectedLanguagePsiElement extends SequencePsiElement implements PsiLanguageInjectionHost {
    public InjectedLanguagePsiElement(ASTNode astNode, ElementType elementType) {
        super(astNode, elementType);
    }

    @Override
    public String getName() {
        return "injected language block (" + getLanguage().getID()+ ")";
    }

    public List<Pair<PsiElement, TextRange>> getInjectedPsi() {
        return InjectedLanguageUtil.getInjectedPsiFiles(this);
    }

    public void processInjectedPsi(@NotNull InjectedPsiVisitor visitor) {
        InjectedLanguageUtil.enumerate(this, visitor);
    }

    public void fixText(@NotNull String text) {

    }

    public boolean isValidHost() {
        return false;
    }

    public PsiLanguageInjectionHost updateText(@NotNull String text) {
        return this;
    }



    @NotNull
    public LiteralTextEscaper<InjectedLanguagePsiElement> createLiteralTextEscaper() {
        return new InjectedLanguageEscaper(this);
    }

    private class InjectedLanguageEscaper extends LiteralTextEscaper<InjectedLanguagePsiElement> {
        protected InjectedLanguageEscaper(@NotNull InjectedLanguagePsiElement host) {
            super(host);
        }

        @Override
        public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
            outChars.append(myHost.getText(), rangeInsideHost.getStartOffset(), rangeInsideHost.getEndOffset());
            return true;
        }

        @Override
        public int getOffsetInHost(int offsetInDecoded, @NotNull TextRange rangeInsideHost) {
            return offsetInDecoded;
        }

        @Override
        public boolean isOneLine() {
            return false;
        }
    }
}
