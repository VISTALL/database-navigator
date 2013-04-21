package com.dci.intellij.dbn.language.common;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DBLanguageBraceMatcher implements PairedBraceMatcher {
    private final BracePair[] bracePairs;
    private DBLanguage language;

    public DBLanguageBraceMatcher(DBLanguage language) {
        this.language = language;
        SharedTokenTypeBundle tt = language.getSharedTokenTypes();
        bracePairs = new BracePair[]{
            new BracePair(tt.getLeftParenthesis(), tt.getRightParenthesis(), false),
            new BracePair(tt.getTokenType("CHR_LEFT_BRACKET"), tt.getTokenType("CHR_RIGHT_BRACKET"), false)};
    }

    public BracePair[] getPairs() {
        return bracePairs;
    }

    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType iElementType, @Nullable IElementType iElementType1) {
        if (iElementType1 instanceof SimpleTokenType) {
            SimpleTokenType simpleTokenType = (SimpleTokenType) iElementType1;
            SharedTokenTypeBundle tt = language.getSharedTokenTypes();
            return simpleTokenType == tt.getWhiteSpace();

        }
        return iElementType1 == null;
    }

    public int getCodeConstructStart(PsiFile psiFile, int i) {
        return i;
    }
}
