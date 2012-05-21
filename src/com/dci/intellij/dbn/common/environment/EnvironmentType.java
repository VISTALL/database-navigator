package com.dci.intellij.dbn.common.environment;

import com.dci.intellij.dbn.common.options.PersistentConfiguration;
import com.dci.intellij.dbn.common.options.setting.SettingsUtil;
import com.dci.intellij.dbn.common.util.Cloneable;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

import java.awt.Color;

public class EnvironmentType implements Cloneable, PersistentConfiguration {
    public static final EnvironmentType DEFAULT     = new EnvironmentType("", "", null);
    public static final EnvironmentType DEVELOPMENT = new EnvironmentType("Development", "Development environment", new Color(-2430209));
    public static final EnvironmentType TEST        = new EnvironmentType("Test", "Testing environment", new Color(-2621494));
    public static final EnvironmentType PRODUCTION  = new EnvironmentType("Production", "Productive environment", new Color(-11574));
    public static final EnvironmentType OTHER       = new EnvironmentType("Other", "", new Color(-1576));

    private String name;
    private String description;
    private Color color;

    public EnvironmentType() {}

    public EnvironmentType(String name, String description, Color color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public EnvironmentType clone() {
        return new EnvironmentType(name, description, color);
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnvironmentType)) return false;

        EnvironmentType that = (EnvironmentType) o;

        if (color != null ? !color.equals(that.color) : that.color != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }

    @Override
    public void readConfiguration(Element element) throws InvalidDataException {
        name = element.getAttributeValue("name");
        description = element.getAttributeValue("description");
        color = SettingsUtil.getColorAttribute(element, "color", color);
    }

    @Override
    public void writeConfiguration(Element element) throws WriteExternalException {
        element.setAttribute("name", name);
        element.setAttribute("description", description);
        SettingsUtil.setColorAttribute(element, "color", color);
    }
}
