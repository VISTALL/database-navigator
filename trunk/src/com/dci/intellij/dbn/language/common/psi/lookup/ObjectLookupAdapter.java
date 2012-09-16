package com.dci.intellij.dbn.language.common.psi.lookup;

import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.element.util.IdentifierRole;
import com.dci.intellij.dbn.language.common.element.util.IdentifierType;
import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.language.common.psi.LeafPsiElement;
import com.dci.intellij.dbn.object.common.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectLookupAdapter extends IdentifierLookupAdapter {
    public ObjectLookupAdapter(LeafPsiElement lookupIssuer, DBObjectType objectType) {
        super(lookupIssuer, IdentifierType.OBJECT, IdentifierRole.ALL, objectType, null);
    }

    public ObjectLookupAdapter(LeafPsiElement lookupIssuer, DBObjectType objectType, CharSequence identifierName) {
        super(lookupIssuer, IdentifierType.OBJECT, IdentifierRole.ALL, objectType, identifierName);
    }

    public ObjectLookupAdapter(LeafPsiElement lookupIssuer, IdentifierRole identifierRole, DBObjectType objectType) {
        super(lookupIssuer, IdentifierType.OBJECT, identifierRole, objectType, null);
    }

    public ObjectLookupAdapter(LeafPsiElement lookupIssuer, IdentifierRole identifierRole, DBObjectType objectType, CharSequence identifierName) {
        super(lookupIssuer, IdentifierType.OBJECT, identifierRole, objectType, identifierName);
    }

    public ObjectLookupAdapter(LeafPsiElement lookupIssuer, @Nullable IdentifierRole identifierRole, @NotNull DBObjectType objectType, CharSequence identifierName, ElementTypeAttribute attribute) {
        super(lookupIssuer, IdentifierType.OBJECT, identifierRole, objectType, identifierName, attribute);
    }

    @Override
    public boolean matches(BasePsiElement basePsiElement) {
        if (super.matches(basePsiElement)) return true;

        DBObjectType virtualObjectType = basePsiElement.getElementType().getVirtualObjectType();
        return virtualObjectType != null && virtualObjectType.matches(getObjectType());
    }
}
