package com.dci.intellij.dbn.language.psql;

import com.dci.intellij.dbn.code.psql.style.options.PSQLCodeStyleSettings;
import com.dci.intellij.dbn.code.psql.style.options.PSQLCustomCodeStyleSettings;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.psql.dialect.PSQLLanguageDialect;
import com.dci.intellij.dbn.language.psql.dialect.oracle.OraclePLSQLLanguageDialect;
import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.psi.tree.IFileElementType;

public class PSQLLanguage extends DBLanguage<PSQLLanguageDialect> {
    public static final PSQLLanguage INSTANCE = new PSQLLanguage();

    private IFileElementType fileElementType;

    protected PSQLLanguageDialect[] createLanguageDialects() {
        PSQLLanguageDialect oraclePLSQLLanguageDialect = new OraclePLSQLLanguageDialect();
        return new PSQLLanguageDialect[]{ oraclePLSQLLanguageDialect };
    }

    public PSQLLanguageDialect getMainLanguageDialect() {
        return getAvailableLanguageDialects()[0];
    }

    public IFileElementType getFileElementType() {
        if (fileElementType == null) {
            fileElementType = new PSQLFileElementType(this);

        }
        return fileElementType;
    }

    private PSQLLanguage() {
        super("DBN-PSQL", "text/plsql");
    }

    public PSQLCodeStyleSettings getCodeStyleSettings(Project project) {
        CodeStyleSettings codeStyleSettings = CodeStyleSettingsManager.getSettings(project);
        PSQLCustomCodeStyleSettings customCodeStyleSettings = codeStyleSettings.getCustomSettings(PSQLCustomCodeStyleSettings.class);
        return customCodeStyleSettings.getCodeStyleSettings();
    }
}