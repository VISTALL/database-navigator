package com.dci.intellij.dbn.language.common;

import com.dci.intellij.dbn.code.common.style.options.CodeStyleCustomSettings;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.mapping.FileConnectionMappingManager;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public abstract class DBLanguage<D extends DBLanguageDialect> extends Language implements DBFileElementTypeProvider {

    private D[] languageDialects;
    private SharedTokenTypeBundle sharedTokenTypes;

    protected DBLanguage(final @NonNls String id, final @NonNls String... mimeTypes){
        super(id, mimeTypes);
        sharedTokenTypes = new SharedTokenTypeBundle(this);
    }

    public SharedTokenTypeBundle getSharedTokenTypes() {
        return sharedTokenTypes;
    }

    protected abstract D[] createLanguageDialects();
    public abstract D getMainLanguageDialect();

    public D getLanguageDialect(Project project, VirtualFile virtualFile) {
        ConnectionHandler connectionHandler = FileConnectionMappingManager.getInstance(project).getActiveConnection(virtualFile);
        if (connectionHandler != null) {
            return (D) connectionHandler.getLanguageDialect(this);
        }
        return getMainLanguageDialect();
    }

    @NotNull
    public D[] getAvailableLanguageDialects() {
        synchronized (this) {
            if (languageDialects == null) {
                languageDialects = createLanguageDialects();
            }
            return languageDialects;
        }
    }

    public D getLanguageDialect(String id) {
        for (D languageDialect: getAvailableLanguageDialects()) {
            if (languageDialect.getID().equals(id)) {
                return languageDialect;
            }
        }
        return null;
    }

    public abstract CodeStyleCustomSettings getCodeStyleSettings(Project project);

    public DBLanguageParserDefinition getParserDefinition(ConnectionHandler connectionHandler) {
        return connectionHandler.getLanguageDialect(this).getParserDefinition();
    }
}
