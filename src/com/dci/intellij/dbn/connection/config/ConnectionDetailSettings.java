package com.dci.intellij.dbn.connection.config;

import com.dci.intellij.dbn.common.environment.EnvironmentType;
import com.dci.intellij.dbn.common.environment.EnvironmentTypeBundle;
import com.dci.intellij.dbn.common.options.ProjectConfiguration;
import com.dci.intellij.dbn.common.options.setting.SettingsUtil;
import com.dci.intellij.dbn.common.util.CommonUtil;
import com.dci.intellij.dbn.connection.config.ui.ConnectionDetailSettingsForm;
import com.dci.intellij.dbn.options.general.GeneralProjectSettings;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class ConnectionDetailSettings extends ProjectConfiguration<ConnectionDetailSettingsForm> {
    private Map<String, String> properties = new HashMap<String, String>();
    private Charset charset = Charset.forName("UTF-8");
    private EnvironmentType environmentType = EnvironmentType.DEFAULT;
    private boolean autoCommit;

    public ConnectionDetailSettings(Project project) {
        super(project);
    }


    public String getDisplayName() {
        return "Connection Detail Settings";
    }

    public String getHelpTopic() {
        return "connectionPropertySettings";
    }

    /*********************************************************
     *                        Custom                         *
     *********************************************************/

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public EnvironmentType getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(EnvironmentType environmentType) {
        this.environmentType = environmentType;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    /*********************************************************
     *                     Configuration                     *
     *********************************************************/
    @Override
    public ConnectionDetailSettingsForm createConfigurationEditor() {
        return new ConnectionDetailSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "details";
    }

    @Override
    public void readConfiguration(Element element) throws InvalidDataException {
        String charsetName = SettingsUtil.getString(element, "charset", "UTF-8");
        charset = Charset.forName(charsetName);
        
        autoCommit = SettingsUtil.getBoolean(element, "auto-commit", autoCommit);
        String environmentTypeName = SettingsUtil.getString(element, "environment-type", EnvironmentType.DEFAULT.getName());
        EnvironmentTypeBundle environmentTypes = GeneralProjectSettings.getInstance(getProject()).getEnvironmentSettings().getEnvironmentTypes();
        environmentType = environmentTypes.get(environmentTypeName);

        Element propertiesElement = element.getChild("properties");
        if (propertiesElement != null) {
            for (Object o : propertiesElement.getChildren()) {
                Element propertyElement = (Element) o;
                properties.put(
                        propertyElement.getAttributeValue("key"),
                        propertyElement.getAttributeValue("value"));
            }
        }
    }

    @Override
    public void writeConfiguration(Element element) throws WriteExternalException {
        SettingsUtil.setString(element, "charset", charset.name());
        
        SettingsUtil.setBoolean(element, "auto-commit", autoCommit);
        SettingsUtil.setString(element, "environment-type", environmentType.getName());

        if (properties.size() > 0) {
            Element propertiesElement = new Element("properties");
            for (String propertyKey : properties.keySet()) {
                Element propertyElement = new Element("property");
                propertyElement.setAttribute("key", propertyKey);
                propertyElement.setAttribute("value", CommonUtil.nvl(properties.get(propertyKey), ""));

                propertiesElement.addContent(propertyElement);
            }
            element.addContent(propertiesElement);
        }

    }
}
