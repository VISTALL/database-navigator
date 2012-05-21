package com.dci.intellij.dbn.vfs;

import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.FileViewProviderFactory;
import com.intellij.psi.PsiManager;

public class DatabaseFileViewProviderFactory implements FileViewProviderFactory{
    public FileViewProvider createFileViewProvider(VirtualFile file, Language language, PsiManager manager, boolean physical) {
        return  file instanceof DatabaseObjectFile ||
                file instanceof SQLConsoleFile ||
                file instanceof SourceCodeFile?
                        new DatabaseFileViewProvider(manager, file, physical, language) : null;
    }
}
