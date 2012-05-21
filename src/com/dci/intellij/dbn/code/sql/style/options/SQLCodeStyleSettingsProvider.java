package com.dci.intellij.dbn.code.sql.style.options;

import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NotNull;

public class SQLCodeStyleSettingsProvider extends CodeStyleSettingsProvider {

    public CustomCodeStyleSettings createCustomSettings(CodeStyleSettings codeStyleSettings) {
        return new SQLCustomCodeStyleSettings(codeStyleSettings);
    }

    @NotNull
    public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings settings1) {
        SQLCustomCodeStyleSettings settingsProvider = settings.getCustomSettings(SQLCustomCodeStyleSettings.class);
        return settingsProvider.getCodeStyleSettings();
    }

    @NotNull
    public Configurable createSettingsPage(CodeStyleSettings settings) {
        SQLCustomCodeStyleSettings settingsProvider = settings.getCustomSettings(SQLCustomCodeStyleSettings.class);
        return settingsProvider.getCodeStyleSettings();
    }

    public String getConfigurableDisplayName() {
        return "SQL (DBN)";
    }
}
