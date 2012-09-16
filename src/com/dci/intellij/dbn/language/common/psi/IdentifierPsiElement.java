package com.dci.intellij.dbn.language.common.psi;

import com.dci.intellij.dbn.code.common.style.formatting.FormattingAttributes;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.language.common.element.IdentifierElementType;
import com.dci.intellij.dbn.language.common.element.LeafElementType;
import com.dci.intellij.dbn.language.common.element.impl.QualifiedIdentifierVariant;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.psi.lookup.AliasDefinitionLookupAdapter;
import com.dci.intellij.dbn.language.common.psi.lookup.IdentifierLookupAdapter;
import com.dci.intellij.dbn.language.common.psi.lookup.ObjectDefinitionLookupAdapter;
import com.dci.intellij.dbn.language.common.psi.lookup.PsiLookupAdapter;
import com.dci.intellij.dbn.language.common.psi.lookup.VariableDefinitionLookupAdapter;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.DBSynonym;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectBundle;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.dci.intellij.dbn.object.common.DBVirtualObject;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.Set;

public class IdentifierPsiElement extends LeafPsiElement implements PsiNamedElement {
    public IdentifierPsiElement(ASTNode astNode, IdentifierElementType elementType) {
        super(astNode, elementType);

    }

    public IdentifierElementType getElementType() {
        return (IdentifierElementType) super.getElementType();
    }

    public ItemPresentation getPresentation() {
        return this;
    }

    public boolean isQuoted() {
        CharSequence charSequence = getChars();
        char quotesChar = getIdentifierQuotesChar();
        return (charSequence.charAt(0) == quotesChar && charSequence.charAt(charSequence.length()-1) == quotesChar);
    }

    @Override
    public String getName() {
        return getText();
    }

    @Override
    public FormattingAttributes getFormattingAttributes() {
        return super.getFormattingAttributes();
    }

    /**
     * ******************************************************
     * ItemPresentation                *
     * *******************************************************
     */
    public String getPresentableText() {
        return getUnquotedText().toUpperCase() + " (" + getObjectType() + ")";
    }

    @Nullable
    public String getLocationString() {
        return null;
    }

    @Nullable
    public Icon getIcon(boolean open) {
        DBObjectType type = getObjectType();
        return type.getIcon();
    }

    @Nullable
    public TextAttributesKey getTextAttributesKey() {
        return null;
    }


    /**
     * ******************************************************
     * Lookup routines                 *
     * *******************************************************
     */
    public BasePsiElement lookupPsiElement(PsiLookupAdapter lookupAdapter, int scopeCrossCount) {
        if (lookupAdapter instanceof IdentifierLookupAdapter) {
            IdentifierLookupAdapter identifierLookupAdapter = (IdentifierLookupAdapter) lookupAdapter;
            if (identifierLookupAdapter.matchesName(this)) {
                initLookup();
                return lookupAdapter.matches(this) ? this : null;
            }
        }
        return null;

    }

    public Set<BasePsiElement> collectPsiElements(PsiLookupAdapter lookupAdapter, Set<BasePsiElement> bucket, int scopeCrossCount) {
        if (lookupAdapter instanceof IdentifierLookupAdapter) {
            IdentifierLookupAdapter identifierLookupAdapter = (IdentifierLookupAdapter) lookupAdapter;
            if (identifierLookupAdapter.matchesName(this)) {
                initLookup();
                if (lookupAdapter.matches(this)) {
                    if (bucket == null) bucket = new THashSet<BasePsiElement>();
                    bucket.add(this);
                }
            }
        }

        return bucket;
    }

    public void collectSubjectPsiElements(Set<BasePsiElement> bucket) {
        if (getElementType().is(ElementTypeAttribute.SUBJECT)) {
            bucket.add(this);
        }
    }

    public void collectExecVariablePsiElements(Set<ExecVariablePsiElement> bucket) {
    }

    /**
     * Identifiers may change their type after being resolved. (e.g.  OBJECT -> ALIAS or even underlying object type)
     * Nevertheless, calling resolve upon lookup may cause stack overflows.
     */
    boolean lookupInitialized = false;

    private void initLookup() {
        if (!lookupInitialized) {
            lookupInitialized = true;
            ConnectionHandler activeConnection = getActiveConnection();
            if (!isResolved() && !isPhysical() && activeConnection != null && !activeConnection.isVirtual()) {
                resolve();
            }
        }

    }

    /**
     * ******************************************************
     * Miscellaneous                     *
     * *******************************************************
     */
    public boolean isObject() {
        return getElementType().isObject();
    }

    public boolean isAlias() {
        return getElementType().isAlias();
    }

    public boolean isVariable() {
        return getElementType().isVariable();
    }


    public boolean isDefinition() {
        return getElementType().isDefinition();
    }

    public boolean isReference() {
        return getElementType().isReference();
    }
    
    public boolean isReferenceable() {
        return getElementType().isReferenceable();
    }

    public boolean isObjectOfType(DBObjectType objectType) {
        return getElementType().isObjectOfType(objectType);
    }

    public boolean isLocalReference() {
        return getElementType().isLocalReference();
    }

    public DBObjectType getObjectType() {
        return getElementType().getObjectType();
    }

    public String getObjectTypeName() {
        return getElementType().getObjectTypeName();
    }

    /**
     * Looks-up whatever underlying database object may be referenced from this identifier.
     * - if this references to a synonym, the DBObject behind the synonym is returned.
     * - if this is an alias reference or definition, it returns the underlying DBObject of the aliased identifier.
     *
     * @return real underlying database object behind the identifier.
     */
    public synchronized DBObject resolveUnderlyingObject() {
        DBObject object = null;
        PsiElement psiElement = isLookupInitialized() ? ref.getReferencedElement() : resolve();
        if (isObject()) {
            if (psiElement instanceof DBObject) {
                object = (DBObject) psiElement;
            } else if (psiElement instanceof IdentifierPsiElement) {
                IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) psiElement;
                PsiElement underlyingPsiElement = identifierPsiElement.resolve();
                if (underlyingPsiElement instanceof DBObject) {
                    object = (DBObject) underlyingPsiElement;
                }
            }
        } else if (isAlias()) {
            BasePsiElement aliasDefinition;
            if (isDefinition()) {
                aliasDefinition = this;
            } else {
                BasePsiElement resolveScope = getEnclosingScopePsiElement();

                PsiLookupAdapter lookupAdapter = new AliasDefinitionLookupAdapter(this, getObjectType(), getUnquotedText());
                aliasDefinition = lookupAdapter.findInScope(resolveScope);
            }

            if (aliasDefinition != null && aliasDefinition instanceof IdentifierPsiElement) {
                BasePsiElement aliasedObject = PsiUtil.resolveAliasedEntityElement((IdentifierPsiElement) aliasDefinition);
                if (aliasedObject != null) {
                    if (aliasedObject.isVirtualObject()) {
                        return aliasedObject.resolveUnderlyingObject();
                    } else if (aliasedObject instanceof IdentifierPsiElement) {
                        IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) aliasedObject;
                        PsiElement underlyingObject = identifierPsiElement.resolve();
                        if (underlyingObject != null && underlyingObject instanceof DBObject) {
                            object = (DBObject) underlyingObject;
                        }
                    }
                }
            }
        }

        while (object != null && object instanceof DBSynonym) {
            DBSynonym synonym = (DBSynonym) object;
            object = synonym.getUnderlyingObject();
        }

        return object;
    }

    public NamedPsiElement lookupNamedPsiElement(String id) {
        return null;
    }

    public BasePsiElement lookupPsiElementBySubject(ElementTypeAttribute attribute, CharSequence subjectName, DBObjectType subjectType) {
        if (getElementType().is(attribute) && getElementType().is(ElementTypeAttribute.SUBJECT)) {
            if (subjectType == getObjectType() && StringUtil.equalsIgnoreCase(subjectName, this.getChars())) {
                return this;
            }
        }
        return null;
    }

    /**
     * ******************************************************
     * Variant builders                *
     * *******************************************************
     */

    private Object[] buildAliasRefVariants() {
        SequencePsiElement statement = (SequencePsiElement) lookupEnclosingPsiElement(ElementTypeAttribute.STATEMENT);
        BasePsiElement sourceScope = getEnclosingScopePsiElement();
        PsiLookupAdapter lookupAdapter = new AliasDefinitionLookupAdapter(this, getObjectType(), null);
        Set<BasePsiElement> aliasDefinitions = lookupAdapter.collectInScope(statement, null);
        return aliasDefinitions == null ? new Object[0] : aliasDefinitions.toArray();
    }

    /**
     * ******************************************************
     * Rersolvers                      *
     * *******************************************************
     */

    private void resolveWithinQualifiedIdentifierElement(QualifiedIdentifierPsiElement qualifiedIdentifier) {
        int index = qualifiedIdentifier.getIndexOf(this);

        BasePsiElement parentObjectElement = null;
        DBObject parentObject = null;
        if (index > 0) {
            IdentifierPsiElement parentElement = qualifiedIdentifier.getLeafAtIndex(index - 1);
            if (parentElement.resolve() != null) {
                parentObjectElement = parentElement.isObject() ? parentElement : PsiUtil.resolveAliasedEntityElement(parentElement);
                parentObject = parentObjectElement != null ? parentElement.resolveUnderlyingObject() : null;
            } else {
                return;
            }
        }

        for (QualifiedIdentifierVariant parseVariant : qualifiedIdentifier.getParseVariants()) {
            LeafElementType parseVariantElementType = parseVariant.getLeaf(index);

            if (parseVariantElementType instanceof IdentifierElementType) {
                IdentifierElementType substitutionCandidate = (IdentifierElementType) parseVariantElementType;
                DBObjectType objectType = substitutionCandidate.getObjectType();

                if (parentObject == null) {  // index == 0
                    if (substitutionCandidate.isObject()) {
                        resolveWithScopeParentLookup(objectType, substitutionCandidate);
                    } else if (substitutionCandidate.isAlias()) {
                        PsiLookupAdapter lookupAdapter = new AliasDefinitionLookupAdapter(this, objectType, ref.getText());
                        BasePsiElement referencedElement = lookupAdapter.findInParentScopeOf(this);
                        if (referencedElement != this && isValidReference(referencedElement)) {
                            setElementType(parseVariantElementType);
                            ref.setReferencedElement(referencedElement);
                            ref.setParent(null);
                        }

                    } else if (substitutionCandidate.isVariable()) {
                        PsiLookupAdapter lookupAdapter = new VariableDefinitionLookupAdapter(this, DBObjectType.ANY, ref.getText());
                        BasePsiElement referencedElement = lookupAdapter.findInParentScopeOf(this);
                        if (referencedElement != this && isValidReference(referencedElement)) {
                            setElementType(parseVariantElementType);
                            ref.setReferencedElement(referencedElement);
                            ref.setParent(null);
                        }

                    }
                } else { // index > 0
                    IdentifierElementType parentElementType = (IdentifierElementType) parseVariant.getLeaf(index - 1);
                    if (parentObject.isOfType(parentElementType.getObjectType())) {
                        DBObject referencedElement = parentObject.getChildObject(objectType, ref.getText(), false);
                        if (isValidReference(referencedElement)) {
                            setElementType(parseVariantElementType);
                            ref.setReferencedElement(referencedElement);
                            ref.setParent(parentObjectElement);
                        }
                    }

                }
                if (ref.getReferencedElement() != null) {
                    return;
                }
            }
        }
    }

    private void resolveWithScopeParentLookup(DBObjectType objectType, IdentifierElementType substitutionCandidate) {
        if (substitutionCandidate.isObject()) {
            ConnectionHandler activeConnection = ref.getActiveConnection();
            if (!substitutionCandidate.isLocalReference() && activeConnection != null && !activeConnection.isVirtual()) {
                Set<DBObject> parentObjects = identifyPotentialParentObjects(objectType, null, this, this);
                if (parentObjects != null && parentObjects.size() > 0) {
                    for (DBObject parentObject : parentObjects) {
                        /*BasePsiElement probableParentObjectElement = null;
                        if (parentObject instanceof IdentifierPsiElement) {
                           IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) parentObject;
                           probableParentObjectElement =
                                   identifierPsiElement.isObject() ?
                                           identifierPsiElement :
                                           PsiUtil.resolveAliasedEntityElement(identifierPsiElement);

                           parentObject = identifierPsiElement.resolveUnderlyingObject();
                       } */

                        PsiElement referencedElement = parentObject.getChildObject(objectType, ref.getText(), false);
                        if (isValidReference(referencedElement)) {
                            ref.setParent(null);
                            ref.setReferencedElement(referencedElement);
                            setElementType(substitutionCandidate);
                            return;
                        }
                    }
                }

                DBObjectBundle objectBundle = activeConnection.getObjectBundle();
                PsiElement referencedElement = objectBundle.getObject(objectType, ref.getText());
                if (isValidReference(referencedElement)) {
                    ref.setParent(null);
                    ref.setReferencedElement(referencedElement);
                    setElementType(substitutionCandidate);
                    return;
                }

                DBSchema schema = getCurrentSchema();
                if (schema != null && objectType.isSchemaObject()) {
                    referencedElement = schema.getChildObject(objectType, ref.getText(), false);
                    if (isValidReference(referencedElement)) {
                        ref.setParent(null);
                        ref.setReferencedElement(referencedElement);
                        setElementType(substitutionCandidate);
                        return;
                    }
                }
            }
            if (!substitutionCandidate.isDefinition()){
                PsiLookupAdapter lookupAdapter = new ObjectDefinitionLookupAdapter(this, objectType, ref.getText());
                PsiElement referencedElement = lookupAdapter.findInParentScopeOf(this);
                if (referencedElement != this && isValidReference(referencedElement)) {
                    ref.setParent(null);
                    ref.setReferencedElement(referencedElement);
                    setElementType(substitutionCandidate);
                    return;
                }
            }
        } else if (substitutionCandidate.isAlias()) {
            PsiLookupAdapter lookupAdapter = new AliasDefinitionLookupAdapter(this, objectType, ref.getText());
            BasePsiElement referencedElement = lookupAdapter.findInParentScopeOf(this);
            if (referencedElement != null && referencedElement != this) {
                ref.setParent(null);
                ref.setReferencedElement(referencedElement);
            }
        } else if (substitutionCandidate.isVariable()) {
            PsiLookupAdapter lookupAdapter = new VariableDefinitionLookupAdapter(this, DBObjectType.ANY, ref.getText());
            BasePsiElement referencedElement = lookupAdapter.findInParentScopeOf(this);
            if (referencedElement != null && referencedElement != this) {
                ref.setParent(null);
                ref.setReferencedElement(referencedElement);
            }
        }
    }

    private boolean isValidReference(PsiElement referencedElement) {
        if (referencedElement != null && referencedElement != this) {
            if (referencedElement instanceof DBVirtualObject) {
                DBVirtualObject object = (DBVirtualObject) referencedElement;
                if (object.getUnderlyingPsiElement().containsPsiElement(this)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isLookupInitialized() {
        return ref != null && ref.isResolving();
    }

    /**
     * ******************************************************
     * PsiReference                    *
     * *******************************************************
     */
    private PsiResolveResult ref;

    @Nullable
    public synchronized PsiElement resolve() {
        if (isDefinition() && (isAlias() || isVariable())) {
            // alias definitions do not have references.
            // underlying object is determined on runtime
            return null;
        }

        ConnectionHandler connectionHandler = getActiveConnection();
        if ((connectionHandler == null || connectionHandler.isVirtual()) && isObject() && isDefinition()) {
            return null;
        }
        if (ref == null) ref = new PsiResolveResult(this);
        if (ref.isDirty()) {
            //System.out.println("resolving " + getTextRange() + " " + getText());
            ref.preResolve(this);
            if (getParent() instanceof QualifiedIdentifierPsiElement) {
                QualifiedIdentifierPsiElement qualifiedIdentifier = (QualifiedIdentifierPsiElement) getParent();
                resolveWithinQualifiedIdentifierElement(qualifiedIdentifier);
            } else {
                resolveWithScopeParentLookup(getObjectType(), getElementType());
            }
            ref.postResolve();
        }
        return ref.getReferencedElement();
    }

    public String getUnquotedText() {
        String text = getText();
        if (isQuoted() && text.length() > 1) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    public String getReferenceQualifiedName() {
        return getUnquotedText().toUpperCase() + " " + getObjectTypeName();
    }

    public boolean isReferenceTo(PsiElement element) {
        return resolve() == element;
    }

    public boolean isSoft() {
        return isDefinition();
    }

    public boolean hasErrors() {
        return false;
    }

    @Override
    public boolean equals(BasePsiElement basePsiElement) {
        if (this == basePsiElement) {
            return true;
        } else {
            if (basePsiElement instanceof IdentifierPsiElement) {
                IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) basePsiElement;
                if (identifierPsiElement.getElementType().isSameAs(getElementType())) {
                    CharSequence localText = getChars();
                    CharSequence remoteText = identifierPsiElement.getChars();
                    return StringUtil.equalsIgnoreCase(localText, remoteText);
                }
            }

            return false;
        }
    }

    @Override
    public boolean matches(BasePsiElement basePsiElement) {
        if (this == basePsiElement) {
            return true;
        } else {
            if (basePsiElement instanceof IdentifierPsiElement) {
                IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) basePsiElement;
                return identifierPsiElement.getElementType().isSameAs(getElementType()) &&
                        StringUtil.equalsIgnoreCase(identifierPsiElement.getChars(), getChars());
            }

            return false;
        }
    }

    public boolean isResolved() {
        return ref != null && !ref.isDirty();
    }

    public PsiElement setName(@NonNls String name) throws IncorrectOperationException {
        return null;
    }
}
