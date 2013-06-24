package com.dci.intellij.dbn.code.common.style.options;

import com.dci.intellij.dbn.code.common.style.options.ui.CodeStyleFormattingSettingsForm;
import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.common.options.setting.SettingsUtil;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

public abstract class CodeStyleFormattingSettings extends Configuration<CodeStyleFormattingSettingsForm> {
    private List<CodeStyleFormattingOption> options = new ArrayList<CodeStyleFormattingOption>();
    private boolean enabled = true;


    public abstract String getDisplayName();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    protected void addOption(CodeStyleFormattingOption option) {
        options.add(option);
    }

    private CodeStyleFormattingOption getCodeStyleCaseOption(String name) {
        for (CodeStyleFormattingOption option : options) {
            if (option.getName().equals(name)) return option;
        }
        return null;
    }

    public List<CodeStyleFormattingOption> getOptions() {
        return options;
    }

    /*********************************************************
     *                     Configuration                     *
     *********************************************************/
    public CodeStyleFormattingSettingsForm createConfigurationEditor() {
        return new CodeStyleFormattingSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return getDisplayName();
    }

    public void readConfiguration(Element element) throws InvalidDataException {
        Element child = element.getChild("formatting");
        if (child != null) {
            enabled = SettingsUtil.getBooleanAttribute(child, "enabled", enabled);
            for (Object object : child.getChildren()) {
                Element optionElement = (Element) object;
                String name = optionElement.getAttributeValue("name");
                CodeStyleFormattingOption option = getCodeStyleCaseOption(name);
                if (option != null) {
                    option.readExternal(optionElement);
                }
            }
        }
    }

    public void writeConfiguration(Element element) throws WriteExternalException {
        Element child = new Element("formatting");
        element.addContent(child);
        SettingsUtil.setBooleanAttribute(child, "enabled", enabled);
        for (CodeStyleFormattingOption option : options) {
            Element optionElement = new Element("option");
            option.writeExternal(optionElement);
            child.addContent(optionElement);
        }
    }
}
