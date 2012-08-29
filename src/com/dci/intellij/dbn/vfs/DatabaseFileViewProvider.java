package com.dci.intellij.dbn.vfs;

import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.navigation.psi.NavigationPsiCache;
import com.dci.intellij.dbn.object.common.DBObject;
import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.SingleRootFileViewProvider;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.NotNull;

public class DatabaseFileViewProvider extends SingleRootFileViewProvider {
    public DatabaseFileViewProvider(@NotNull PsiManager manager, @NotNull VirtualFile virtualFile, boolean physical) {
        super(manager, virtualFile, physical);
    }

    public DatabaseFileViewProvider(@NotNull PsiManager psiManager, @NotNull VirtualFile virtualFile, boolean physical, @NotNull Language language) {
        super(psiManager, virtualFile, physical, language);
    }

    @Override
    public boolean isPhysical() {
        return false;
    }

    @Override
    protected PsiFile getPsiInner(Language language) {
        if (language instanceof DBLanguage) {
            VirtualFile virtualFile = getVirtualFile();
            if (virtualFile instanceof DatabaseObjectFile) {
                DatabaseObjectFile objectFile = (DatabaseObjectFile) virtualFile;
                DBObject object = objectFile.getObject();
                NavigationPsiCache psiCache = object.getConnectionHandler().getPsiCache();
                return psiCache.getPsiFile(object);
            }

            PsiFile psiFile = super.getPsiInner(language);
            if (psiFile == null) {
                DatabaseFile databaseFile = getDatabaseFile(virtualFile);
                if (databaseFile != null) {
                    return databaseFile.initializePsiFile(this, (DBLanguage) language);
                }
            } else {
                return psiFile;
            }
        }

        return super.getPsiInner(language);
    }

    private DatabaseFile getDatabaseFile(VirtualFile virtualFile) {
        if (virtualFile instanceof DatabaseFile) {
            return (DatabaseFile) virtualFile;
        }

        if (virtualFile instanceof LightVirtualFile) {
            LightVirtualFile lightVirtualFile = (LightVirtualFile) virtualFile;
            VirtualFile originalFile = lightVirtualFile.getOriginalFile();
            if (originalFile != null && originalFile != virtualFile) {
                return getDatabaseFile(virtualFile);
            }
        }
        return null;
    }

    @NotNull
    @Override
    public SingleRootFileViewProvider createCopy(VirtualFile copy) {
        return new DatabaseFileViewProvider(getManager(), copy, false, getBaseLanguage());
    }

    @NotNull
    @Override
    public VirtualFile getVirtualFile() {
        VirtualFile virtualFile = super.getVirtualFile();
/*
        if (virtualFile instanceof SourceCodeFile)  {
            SourceCodeFile sourceCodeFile = (SourceCodeFile) virtualFile;
            return sourceCodeFile.getDatabaseFile();
        }
*/
        return virtualFile;
    }
}
