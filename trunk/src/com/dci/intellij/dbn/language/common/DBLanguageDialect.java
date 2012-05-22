package com.dci.intellij.dbn.language.common;

import com.dci.intellij.dbn.language.common.element.ChameleonTokenType;
import com.intellij.lang.LanguageDialect;
import com.intellij.psi.tree.IFileElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class DBLanguageDialect extends LanguageDialect implements DBFileElementTypeProvider {
    private DBLanguageDialectIdentifier identifier;
    private DBLanguageSyntaxHighlighter syntaxHighlighter;
    private DBLanguageParserDefinition parserDefinition;
    private IFileElementType fileElementType;
    private Set<InjectedLanguageTokenType> injectedLanguageTokens;
    private ChameleonTokenType chameleonTokenType;
    private static Map<DBLanguageDialectIdentifier, DBLanguageDialect> register = new HashMap<DBLanguageDialectIdentifier, DBLanguageDialect>();

    public DBLanguageDialect(@NonNls @NotNull DBLanguageDialectIdentifier identifier, @NotNull DBLanguage baseLanguage) {
        super(identifier.getValue(), baseLanguage);
        this.identifier = identifier;
        syntaxHighlighter = createSyntaxHighlighter();
        parserDefinition = createParserDefinition();
        fileElementType = createFileElementType();
        register.put(identifier, this);
        chameleonTokenType = new ChameleonTokenType(this);
    }

    protected abstract Set<InjectedLanguageTokenType> createInjectedLanguageTokens();
    protected abstract DBLanguageSyntaxHighlighter createSyntaxHighlighter() ;
    protected abstract DBLanguageParserDefinition createParserDefinition();
    protected abstract IFileElementType createFileElementType();
    public ChameleonTokenType getChameleonTokenType(DBLanguageDialectIdentifier dialectIdentifier) {
        throw new IllegalArgumentException("Language " + getID() + " does not support chameleons of type " + dialectIdentifier.getValue() );
    }

    public static DBLanguageDialect getLanguageDialect(DBLanguageDialectIdentifier identifier) {
        return register.get(identifier);
    }

    public DBLanguageDialectIdentifier getIdentifier() {
        return identifier;
    }

    public boolean isReservedWord(String identifier) {
        return getParserTokenTypes().isReservedWord(identifier);
    }

    @NotNull
    public DBLanguage getBaseLanguage() {
        return (DBLanguage) super.getBaseLanguage();
    }

    public SharedTokenTypeBundle getSharedTokenTypes() {
        return getBaseLanguage().getSharedTokenTypes();
    }

    public DBLanguageSyntaxHighlighter getSyntaxHighlighter() {
        return syntaxHighlighter;
    }

    @NotNull
    public DBLanguageParserDefinition getParserDefinition() {
        return parserDefinition;
    }

    public IFileElementType getFileElementType() {
        return fileElementType;
    }

    public TokenTypeBundle getParserTokenTypes() {
        return parserDefinition.getParser().getTokenTypes();
    }

    public TokenTypeBundle getHighlighterTokenTypes() {
        return syntaxHighlighter.getTokenTypes();
    }

    public TokenType getInjectedLanguageToken(DBLanguageDialectIdentifier dialectIdentifier) {
        if (injectedLanguageTokens == null) {
            injectedLanguageTokens = createInjectedLanguageTokens();
            if (injectedLanguageTokens == null) injectedLanguageTokens = new HashSet<InjectedLanguageTokenType>();
        }
        for (InjectedLanguageTokenType injectedLanguageToken : injectedLanguageTokens) {
            if (injectedLanguageToken.getInjectedLanguage().getIdentifier() == dialectIdentifier) {
                return injectedLanguageToken;
            }
        }
        return null;
    }

    public ChameleonTokenType getChameleonTokenType() {
        return chameleonTokenType;
    }
}