package com.dci.intellij.dbn.language.sql.template;

import com.dci.intellij.dbn.language.common.psi.LeafPsiElement;
import com.dci.intellij.dbn.language.common.psi.PsiUtil;
import com.dci.intellij.dbn.language.sql.SQLLanguage;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SQLTemplateContextType extends TemplateContextType {
    protected SQLTemplateContextType() {
        super("SQL", "SQL (DBN)");
    }

    @Override
    public boolean isInContext(@NotNull PsiFile file, int offset) {
        LeafPsiElement leafPsiElement = PsiUtil.lookupLeafBeforeOffset(file, offset);
        if (leafPsiElement != null) {
            return leafPsiElement.getLanguage() instanceof SQLLanguage;
        }
        return file.getLanguage() instanceof SQLLanguage;
    }

    @Nullable
    @Override
    public SyntaxHighlighter createHighlighter() {
        return SQLLanguage.INSTANCE.getMainLanguageDialect().getSyntaxHighlighter();
    }
}
