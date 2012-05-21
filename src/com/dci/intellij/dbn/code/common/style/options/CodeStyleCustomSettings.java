package com.dci.intellij.dbn.code.common.style.options;

import com.dci.intellij.dbn.common.options.CompositeConfiguration;
import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.common.options.ui.CompositeConfigurationEditorForm;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

public abstract class CodeStyleCustomSettings<T extends CompositeConfigurationEditorForm> extends CompositeConfiguration<T>{
    protected CodeStyleCaseSettings caseSettings;
    protected CodeStyleFormattingSettings formattingSettings;

    protected CodeStyleCustomSettings() {
        caseSettings = createCaseSettings();
        formattingSettings = createAttributeSettings();
    }

    protected abstract CodeStyleCaseSettings createCaseSettings();
    protected abstract CodeStyleFormattingSettings createAttributeSettings();

    public CodeStyleCaseSettings getCaseSettings() {
        return caseSettings;
    }

    public CodeStyleFormattingSettings getFormattingSettings() {
        return formattingSettings;
    }

    /*********************************************************
    *                     Configuration                     *
    *********************************************************/
    protected Configuration[] createConfigurations() {
        return new Configuration[] {
                caseSettings,
                formattingSettings};
    }

    public void readConfiguration(Element element) throws InvalidDataException {
        readConfiguration(element, caseSettings);
        readConfiguration(element, formattingSettings);
    }

    public void writeConfiguration(Element element) throws WriteExternalException {
         caseSettings.writeConfiguration(element);
         formattingSettings.writeConfiguration(element);
     }


}
