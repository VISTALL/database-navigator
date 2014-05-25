package com.dci.intellij.dbn.vfs;

import com.dci.intellij.dbn.common.util.DocumentUtil;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.DBLanguageFile;
import com.dci.intellij.dbn.navigation.psi.NavigationPsiCache;
import com.dci.intellij.dbn.object.common.DBObject;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.SingleRootFileViewProvider;
import com.intellij.psi.impl.PsiDocumentManagerImpl;
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
        return true;
    }

    @Override
    protected PsiFile getPsiInner(@NotNull Language language) {
        if (language instanceof DBLanguage) {
            DBLanguage dbLanguage  = (DBLanguage) language;
            VirtualFile virtualFile = getVirtualFile();
            if (virtualFile instanceof DatabaseObjectFile) {
                DatabaseObjectFile objectFile = (DatabaseObjectFile) virtualFile;
                DBObject object = objectFile.getObject();
                return NavigationPsiCache.getPsiFile(object);
            }

            if (virtualFile instanceof LightVirtualFile) {
                LightVirtualFile lightVirtualFile = (LightVirtualFile) virtualFile;
                VirtualFile originalFile = lightVirtualFile.getOriginalFile();
                if (originalFile != null && originalFile != virtualFile) {
                    if (originalFile instanceof SourceCodeFile) {
                        SourceCodeFile sourceCodeFile = (SourceCodeFile) originalFile;
                        DBLanguageDialect languageDialect = sourceCodeFile.getConnectionHandler().getLanguageDialect(dbLanguage);
                        if (languageDialect != null) {
                            DBLanguageFile file = (DBLanguageFile) languageDialect.getParserDefinition().createFile(this);
                            forceCachedPsi(file);
                            Document document = DocumentUtil.getDocument(getVirtualFile());
                            PsiDocumentManagerImpl.cachePsi(document, file);
                            return file;
                        }
                    }

                }
            }

            DBLanguage baseLanguage = (DBLanguage) getBaseLanguage();
            PsiFile psiFile = super.getPsiInner(baseLanguage);
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
                return getDatabaseFile(originalFile);
            }
        }
        return null;
    }

    @NotNull
    @Override
    public SingleRootFileViewProvider createCopy(@NotNull VirtualFile copy) {
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
