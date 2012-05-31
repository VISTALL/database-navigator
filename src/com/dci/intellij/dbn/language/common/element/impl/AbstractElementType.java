package com.dci.intellij.dbn.language.common.element.impl;

import com.dci.intellij.dbn.code.common.style.formatting.FormattingDefinition;
import com.dci.intellij.dbn.code.common.style.formatting.FormattingDefinitionFactory;
import com.dci.intellij.dbn.code.common.style.formatting.IndentDefinition;
import com.dci.intellij.dbn.code.common.style.formatting.SpacingDefinition;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.element.ElementType;
import com.dci.intellij.dbn.language.common.element.ElementTypeBundle;
import com.dci.intellij.dbn.language.common.element.lookup.ElementTypeLookupCache;
import com.dci.intellij.dbn.language.common.element.parser.ElementTypeParser;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttributesBundle;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeDefinitionException;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeLogger;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.intellij.psi.tree.IElementType;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public abstract class AbstractElementType extends IElementType implements ElementType {
    private static final FormattingDefinition STATEMENT_FORMATTING = new FormattingDefinition(null, IndentDefinition.NORMAL, SpacingDefinition.MIN_LINE_BREAK, null);

    private String id;
    private String description;
    private Icon icon;
    private FormattingDefinition formatting;
    private ElementTypeLookupCache lookupCache;
    private ElementTypeParser parser;
    private ElementTypeBundle bundle;
    private ElementType parent;


    private DBObjectType virtualObjectType;
    private boolean isVirtualObjectInsideLookup;
    private ElementTypeAttributesBundle attributes = ElementTypeAttributesBundle.EMPTY;
    private ElementTypeLogger logger = new ElementTypeLogger(this);

    public AbstractElementType(ElementTypeBundle bundle, ElementType parent, String id, @Nullable String description) {
        super(id, bundle.getLanguageDialect(), false);
        this.id = id;
        this.description = description;
        this.bundle = bundle;
        this.parent = parent;
        this.lookupCache = createLookupCache();
        this.parser = createParser();
    }

    public AbstractElementType(ElementTypeBundle bundle, ElementType parent, String id, Element def) throws ElementTypeDefinitionException {
        super(id, bundle.getLanguageDialect(), false);
        this.id = def.getAttributeValue("id");
        if (!id.equals(this.id)) {
            this.id = id;
            def.setAttribute("id", this.id);
            bundle.markIndexesDirty();
        }
        this.bundle = bundle;
        this.parent = parent;
        this.lookupCache = createLookupCache();
        this.parser = createParser();
        loadDefinition(def);
    }

    protected abstract ElementTypeLookupCache createLookupCache();

    protected abstract ElementTypeParser createParser();

    public void setDefaultFormatting(FormattingDefinition defaultFormatting) {
        formatting = FormattingDefinitionFactory.mergeDefinitions(formatting, defaultFormatting);
    }

    protected void loadDefinition(Element def) throws ElementTypeDefinitionException {
        String attributesString = def.getAttributeValue("attributes");
        if (StringUtil.isNotEmptyOrSpaces(attributesString)) {
            attributes =  new ElementTypeAttributesBundle(attributesString);
        }

        isVirtualObjectInsideLookup = Boolean.parseBoolean(def.getAttributeValue("virtual-object-inside-lookup"));
        String objectTypeName = def.getAttributeValue("virtual-object");
        if (objectTypeName != null) {
            virtualObjectType = ElementTypeBundle.resolveObjectType(objectTypeName);
        }
        formatting = FormattingDefinitionFactory.loadDefinition(def);
        if (is(ElementTypeAttribute.STATEMENT)) {
            setDefaultFormatting(STATEMENT_FORMATTING);
        }

        String iconKey = def.getAttributeValue("icon");
        if (iconKey != null)  icon = Icons.getIcon(iconKey);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Icon getIcon() {
        return icon;
    }

    public ElementType getParent() {
        return parent;
    }

    public ElementTypeLookupCache getLookupCache() {
        return lookupCache;
    }

    public @NotNull ElementTypeParser getParser() {
        return parser;
    }

    public ElementTypeLogger getLogger() {
        return logger;
    }

    public boolean is(ElementTypeAttribute attribute) {
        return attributes.is(attribute);
    }

    public ElementTypeAttributesBundle getAttributes() {
        return attributes;
    }

    public FormattingDefinition getFormatting() {
        return formatting;
    }

    @NotNull
    public DBLanguageDialect getLanguage() {
        return (DBLanguageDialect) super.getLanguage();
    }

    public ElementTypeBundle getElementBundle() {
        return bundle;
    }

    public String toString() {
        return getId();
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public void registerVirtualObject(DBObjectType objectType) {
        getLookupCache().registerVirtualObject(objectType);
    }

    /*********************************************************
     *                  Virtual Object                       *
     *********************************************************/
    public boolean isVirtualObject() {
        return virtualObjectType != null;
    }

    public DBObjectType getVirtualObjectType() {
        return virtualObjectType;
    }

    public boolean isVirtualObjectInsideLookup() {
        return isVirtualObjectInsideLookup;
    }
}
