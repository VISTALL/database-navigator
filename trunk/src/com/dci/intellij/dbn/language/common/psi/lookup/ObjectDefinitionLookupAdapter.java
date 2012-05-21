package com.dci.intellij.dbn.language.common.psi.lookup;

import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.element.util.IdentifierRole;
import com.dci.intellij.dbn.language.common.psi.LeafPsiElement;
import com.dci.intellij.dbn.object.common.DBObjectType;
import org.jetbrains.annotations.NotNull;

public class ObjectDefinitionLookupAdapter extends ObjectLookupAdapter {

    public ObjectDefinitionLookupAdapter(LeafPsiElement lookupIssuer, DBObjectType objectType, String identifierName) {
        super(lookupIssuer, IdentifierRole.DEFINITION, objectType, identifierName);
    }

    public ObjectDefinitionLookupAdapter(LeafPsiElement lookupIssuer, @NotNull DBObjectType objectType, String identifierName, ElementTypeAttribute attribute) {
        super(lookupIssuer, IdentifierRole.DEFINITION, objectType, identifierName, attribute);
    }
}
