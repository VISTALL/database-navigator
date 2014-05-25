package com.dci.intellij.dbn.language.psql;

import com.dci.intellij.dbn.code.psql.color.PSQLTextAttributesKeys;
import com.dci.intellij.dbn.code.sql.color.SQLTextAttributesKeys;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.editor.code.SourceCodeEditorManager;
import com.dci.intellij.dbn.execution.statement.StatementGutterRenderer;
import com.dci.intellij.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.dci.intellij.dbn.language.common.element.ElementType;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.navigation.NavigateToDefinitionAction;
import com.dci.intellij.dbn.language.common.navigation.NavigateToObjectAction;
import com.dci.intellij.dbn.language.common.navigation.NavigateToSpecificationAction;
import com.dci.intellij.dbn.language.common.navigation.NavigationAction;
import com.dci.intellij.dbn.language.common.navigation.NavigationGutterRenderer;
import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.language.common.psi.ChameleonPsiElement;
import com.dci.intellij.dbn.language.common.psi.ExecutablePsiElement;
import com.dci.intellij.dbn.language.common.psi.IdentifierPsiElement;
import com.dci.intellij.dbn.language.common.psi.NamedPsiElement;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class PSQLLanguageAnnotator implements Annotator {

    public void annotate(@NotNull final PsiElement psiElement, @NotNull final AnnotationHolder holder) {
        if (psiElement instanceof BasePsiElement) {
            BasePsiElement basePsiElement = (BasePsiElement) psiElement;
            ElementType elementType = basePsiElement.getElementType();
            if (elementType.is(ElementTypeAttribute.OBJECT_SPECIFICATION) || elementType.is(ElementTypeAttribute.OBJECT_DECLARATION)) {
                annotateSpecDeclarationNavigable(basePsiElement, holder);
            }

        }

        if (psiElement instanceof IdentifierPsiElement) {
            IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) psiElement;
            ConnectionHandler connectionHandler = identifierPsiElement.getActiveConnection();
            if (connectionHandler != null) {
                annotateIdentifier(psiElement, holder);
            }
        }

        if (psiElement instanceof NamedPsiElement) {
            NamedPsiElement namedPsiElement = (NamedPsiElement) psiElement;
            if (namedPsiElement.hasErrors()) {
                holder.createErrorAnnotation(namedPsiElement, "Invalid " + namedPsiElement.getElementType().getDescription());
            }
        }

        if (psiElement instanceof ChameleonPsiElement) {
            Annotation annotation = holder.createInfoAnnotation(psiElement, null);
            annotation.setTextAttributes(SQLTextAttributesKeys.CHAMELEON);
        }

        if (psiElement instanceof ExecutablePsiElement)  annotateExecutable(psiElement, holder);
    }

     private void annotateIdentifier(final PsiElement psiElement, final AnnotationHolder holder) {
/*        IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) psiElement;
        if (identifierPsiElement.isReference())
            identifierPsiElement.resolve();*/
        /*if (identifierPsiElement.isObject() && identifierPsiElement.isReference()) {
            annotateObject(identifierPsiElement, holder);
        } else if (identifierPsiElement.isAlias()) {
            if (identifierPsiElement.isReference())
                annotateAliasRef(identifierPsiElement, holder); else
                annotateAliasDef(identifierPsiElement, holder);
        }*/
    }

    private void annotateAliasRef(IdentifierPsiElement aliasReference, AnnotationHolder holder) {
        if (aliasReference.resolve() == null) {
            Annotation annotation = holder.createWarningAnnotation(aliasReference, "Unknown identifier");
            annotation.setTextAttributes(PSQLTextAttributesKeys.UNKNOWN_IDENTIFIER);
        } else {
            Annotation annotation = holder.createInfoAnnotation(aliasReference, null);
            annotation.setTextAttributes(PSQLTextAttributesKeys.DATA_TYPE);
        }
    }

    private void annotateAliasDef(IdentifierPsiElement aliasDefinition, AnnotationHolder holder) {
        /*Set<PsiElement> aliasDefinitions = new HashSet<PsiElement>();
        SequencePsiElement sourceScope = aliasDefinition.getEnclosingScopePsiElement();
        sourceScope.collectAliasDefinitionPsiElements(aliasDefinitions, aliasDefinition.getUnquotedText(), DBObjectType.ANY, null);
        if (aliasDefinitions.size() > 1) {
            holder.createWarningAnnotation(aliasDefinition, "Duplicate alias definition: " + aliasDefinition.getUnquotedText());
        }
        Annotation annotation = holder.createInfoAnnotation(aliasDefinition, null);
        annotation.setTextAttributes(SQLTextAttributesKeys.DATA_TYPE);*/
    }

    private void annotateObject(IdentifierPsiElement objectReference, AnnotationHolder holder) {
        PsiElement reference = objectReference.resolve();
        /*ConnectionHandler connectionHandler = objectReference.getActiveConnection();
        if (reference == null && connectionHandler != null && connectionHandler.getConnectionStatus().isValid()) {
            Annotation annotation = holder.createErrorAnnotation(objectReference.getAstNode(),
                    "Unknown " + objectReference.getObjectTypeName());
            annotation.setTextAttributes(PSQLTextAttributesKeys.UNKNOWN_IDENTIFIER);
        }*/
    }

    private void annotateSpecDeclarationNavigable(BasePsiElement basePsiElement, AnnotationHolder holder) {
        BasePsiElement subjectPsiElement = basePsiElement.lookupFirstPsiElement(ElementTypeAttribute.SUBJECT);
        if (subjectPsiElement instanceof IdentifierPsiElement) {
            IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) subjectPsiElement;
            DBObjectType objectType = identifierPsiElement.getObjectType();
            ElementType elementType = basePsiElement.getElementType();

            if (identifierPsiElement.isObject() && objectType.getGenericType() == DBObjectType.METHOD) {

                DBContentType targetContentType =
                        elementType.is(ElementTypeAttribute.OBJECT_DECLARATION) ? DBContentType.CODE_SPEC :
                        elementType.is(ElementTypeAttribute.OBJECT_SPECIFICATION) ? DBContentType.CODE_BODY : null;

                if (targetContentType != null && identifierPsiElement.getFile() instanceof PSQLFile) {
                    PSQLFile file = (PSQLFile) identifierPsiElement.getFile();
                    DBSchemaObject object = (DBSchemaObject) file.getUnderlyingObject();

                    if (object == null) {
                        ElementTypeAttribute targetAttribute =
                                elementType.is(ElementTypeAttribute.OBJECT_DECLARATION) ? ElementTypeAttribute.OBJECT_SPECIFICATION :
                                elementType.is(ElementTypeAttribute.OBJECT_SPECIFICATION) ? ElementTypeAttribute.OBJECT_DECLARATION : null;

                        if (targetAttribute != null) {
                            BasePsiElement rootPsiElement = identifierPsiElement.lookupEnclosingPsiElement(ElementTypeAttribute.ROOT);

                            BasePsiElement targetElement = rootPsiElement == null ? null :
                                    rootPsiElement.lookupPsiElementBySubject(targetAttribute,
                                                identifierPsiElement.getChars(),
                                                identifierPsiElement.getObjectType());

                            if (targetElement != null) {
                                NavigationAction navigationAction = targetContentType == DBContentType.CODE_BODY ?
                                        new NavigateToDefinitionAction(null, targetElement, objectType) :
                                        new NavigateToSpecificationAction(null, targetElement, objectType);
                                Annotation annotation = holder.createInfoAnnotation(basePsiElement, null);
                                annotation.setGutterIconRenderer(new NavigationGutterRenderer(navigationAction));
                            }
                            NavigateToObjectAction navigateToObjectAction = new NavigateToObjectAction(null, objectType);
                            Annotation annotation = holder.createInfoAnnotation(basePsiElement, null);
                            annotation.setGutterIconRenderer(new NavigationGutterRenderer(navigateToObjectAction));


                        }
                    } else if (object.getContentType() == DBContentType.CODE_SPEC_AND_BODY) {
                        SourceCodeEditorManager codeEditorManager = SourceCodeEditorManager.getInstance(object.getProject());


                        BasePsiElement targetElement = codeEditorManager.getObjectNavigationElement(object, targetContentType, identifierPsiElement.getObjectType(), identifierPsiElement.getChars());
                        if (targetElement != null) {
                            NavigationAction navigationAction = targetContentType == DBContentType.CODE_BODY ?
                                    new NavigateToDefinitionAction(object, targetElement, objectType) :
                                    new NavigateToSpecificationAction(object, targetElement, objectType);
                            Annotation annotation = holder.createInfoAnnotation(basePsiElement, null);
                            annotation.setGutterIconRenderer(new NavigationGutterRenderer(navigationAction));
                        }
                        NavigateToObjectAction navigateToObjectAction = new NavigateToObjectAction(identifierPsiElement.resolveUnderlyingObject(), objectType);
                        Annotation annotation = holder.createInfoAnnotation(basePsiElement, null);
                        annotation.setGutterIconRenderer(new NavigationGutterRenderer(navigateToObjectAction));
                    }
                }
            }
        }
    }

    private void annotateExecutable(PsiElement psiElement, AnnotationHolder holder) {
        ExecutablePsiElement executable = (ExecutablePsiElement) psiElement;
        if (!executable.isNestedExecutable()) {
            StatementExecutionProcessor executionProcessor = executable.getExecutionProcessor();
            if (executionProcessor != null) {
                Annotation annotation = holder.createInfoAnnotation(psiElement, null);
                annotation.setGutterIconRenderer(new StatementGutterRenderer(executionProcessor));
            }
        }
    }
}
