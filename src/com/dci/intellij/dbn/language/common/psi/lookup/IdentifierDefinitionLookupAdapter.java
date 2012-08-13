package com.dci.intellij.dbn.language.common.psi.lookup;

import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.element.util.IdentifierRole;
import com.dci.intellij.dbn.language.common.psi.IdentifierPsiElement;
import com.dci.intellij.dbn.object.common.DBObjectType;
import org.jetbrains.annotations.NotNull;

public class IdentifierDefinitionLookupAdapter extends IdentifierLookupAdapter {
    public IdentifierDefinitionLookupAdapter(IdentifierPsiElement lookupIssuer, DBObjectType objectType, String identifierName) {
        super(lookupIssuer, null, IdentifierRole.DEFINITION, objectType, identifierName);
    }

    public IdentifierDefinitionLookupAdapter(IdentifierPsiElement lookupIssuer, @NotNull DBObjectType objectType, String identifierName, ElementTypeAttribute attribute) {
        super(lookupIssuer, null, IdentifierRole.DEFINITION, objectType, identifierName, attribute);
    }
}
