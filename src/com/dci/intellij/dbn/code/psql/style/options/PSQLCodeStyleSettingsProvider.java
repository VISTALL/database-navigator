package com.dci.intellij.dbn.code.psql.style.options;

import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NotNull;

public class PSQLCodeStyleSettingsProvider extends CodeStyleSettingsProvider {

    public CustomCodeStyleSettings createCustomSettings(CodeStyleSettings codeStyleSettings) {
        return new PSQLCustomCodeStyleSettings(codeStyleSettings);
    }

    @NotNull
    public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings settings1) {
        PSQLCustomCodeStyleSettings settingsProvider = settings.getCustomSettings(PSQLCustomCodeStyleSettings.class);
        return settingsProvider.getCodeStyleSettings();
    }

    @NotNull
    public Configurable createSettingsPage(CodeStyleSettings settings) {
        PSQLCustomCodeStyleSettings settingsProvider = settings.getCustomSettings(PSQLCustomCodeStyleSettings.class);
        return settingsProvider.getCodeStyleSettings();
    }

    public String getConfigurableDisplayName() {
        return "PL/SQL (DBN)";
    }


}