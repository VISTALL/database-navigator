package com.dci.intellij.dbn.language.common.psi.lookup;

import com.dci.intellij.dbn.language.common.element.IdentifierElementType;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.element.util.IdentifierRole;
import com.dci.intellij.dbn.language.common.element.util.IdentifierType;
import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.language.common.psi.IdentifierPsiElement;
import com.dci.intellij.dbn.language.common.psi.LeafPsiElement;
import com.dci.intellij.dbn.object.common.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IdentifierLookupAdapter extends PsiLookupAdapter {
    private IdentifierType identifierType;
    private IdentifierRole identifierRole;
    private DBObjectType objectType;
    private String identifierName;
    private ElementTypeAttribute attribute;
    private LeafPsiElement lookupIssuer;

    protected IdentifierLookupAdapter(LeafPsiElement lookupIssuer, IdentifierType identifierType, IdentifierRole identifierRole, DBObjectType objectType, String identifierName) {
        this(lookupIssuer, identifierType, identifierRole, objectType, identifierName, null);
    }

    protected IdentifierLookupAdapter(LeafPsiElement lookupIssuer, @NotNull IdentifierType identifierType, @Nullable IdentifierRole identifierRole, @NotNull DBObjectType objectType, String identifierName, ElementTypeAttribute attribute) {
        this.lookupIssuer = lookupIssuer;
        this.identifierType = identifierType;
        this.objectType = objectType;
        this.identifierName = identifierName;
        this.identifierRole = identifierRole;
        this.attribute = attribute;
    }

    public boolean matches(BasePsiElement basePsiElement) {
        if (basePsiElement instanceof IdentifierPsiElement && basePsiElement != lookupIssuer) {
            IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) basePsiElement;
            return
                checkType(identifierPsiElement) &&
                checkObjectType(identifierPsiElement) &&
                checkRole(identifierPsiElement) &&
                checkAttribute(identifierPsiElement) &&
                checkName(identifierPsiElement);
        }
        return false;
    }

    private boolean checkType(IdentifierPsiElement identifierPsiElement) {
        return identifierType == identifierPsiElement.getElementType().getIdentifierType();
    }

    private boolean checkObjectType(IdentifierPsiElement identifierPsiElement) {
        return identifierPsiElement.isObjectOfType(objectType);
    }

    private boolean checkName(IdentifierPsiElement identifierPsiElement) {
        return identifierName == null || identifierPsiElement.getUnquotedText().equalsIgnoreCase(identifierName);
    }

    private boolean checkRole(IdentifierPsiElement identifierPsiElement) {
        IdentifierElementType elementType = identifierPsiElement.getElementType();
        IdentifierRole role = elementType.getIdentifierRole();
        switch (identifierRole) {
            case ALL: return true;
            case DEFINITION: return role == IdentifierRole.DEFINITION || identifierPsiElement.isReferenceable();
            case REFERENCE: return role == IdentifierRole.REFERENCE;
        }
        return false;
    }

    private boolean checkAttribute(IdentifierPsiElement identifierPsiElement) {
        return attribute == null || identifierPsiElement.getElementType().is(attribute);
    }

    @Override
    public boolean accepts(BasePsiElement element) {
        return true;
    }

    public IdentifierType getIdentifierType() {
        return identifierType;
    }

    public IdentifierRole getIdentifierRole() {
        return identifierRole;
    }

    public DBObjectType getObjectType() {
        return objectType;
    }

    public String getIdentifierName() {
        return identifierName;
    }

    public ElementTypeAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(ElementTypeAttribute attribute) {
        this.attribute = attribute;
    }

    public String toString() {
        return "IdentifierLookupAdapter{" +
                "identifierType=" + identifierType +
                ", identifierRole=" + identifierRole +
                ", objectType=" + objectType +
                ", identifierName='" + identifierName + '\'' +
                ", attribute=" + attribute +
                '}';
    }
}
