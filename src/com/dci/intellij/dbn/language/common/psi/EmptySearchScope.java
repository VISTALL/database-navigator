package com.dci.intellij.dbn.language.common.psi;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

public class EmptySearchScope extends GlobalSearchScope {
    public static final EmptySearchScope INSTANCE = new EmptySearchScope();    

    public int compare(final VirtualFile file1, final VirtualFile file2) {
        return 0;
    }

    public boolean contains(final VirtualFile file) {
        return false;
    }

    public boolean isSearchInLibraries() {
        return false;
    }

    public boolean isSearchInModuleContent(@NotNull final Module aModule) {
        return false;
    }
}