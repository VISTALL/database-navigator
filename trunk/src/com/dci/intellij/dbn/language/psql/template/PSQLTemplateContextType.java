package com.dci.intellij.dbn.language.psql.template;

import com.dci.intellij.dbn.language.common.psi.LeafPsiElement;
import com.dci.intellij.dbn.language.common.psi.PsiUtil;
import com.dci.intellij.dbn.language.psql.PSQLLanguage;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PSQLTemplateContextType extends TemplateContextType {
    protected PSQLTemplateContextType() {
        super("PL/SQL", "PL/SQL (DBN)");
    }

    @Override
    public boolean isInContext(@NotNull PsiFile file, int offset) {
        LeafPsiElement leafPsiElement = PsiUtil.lookupLeafBeforeOffset(file, offset);
        if (leafPsiElement != null) {
            return leafPsiElement.getLanguage() instanceof PSQLLanguage;
        }
        return file.getLanguage() instanceof PSQLLanguage;
    }

    @Nullable
    @Override
    public SyntaxHighlighter createHighlighter() {
        return PSQLLanguage.INSTANCE.getMainLanguageDialect().getSyntaxHighlighter();
    }
}
