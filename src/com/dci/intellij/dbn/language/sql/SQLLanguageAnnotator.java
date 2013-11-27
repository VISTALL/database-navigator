package com.dci.intellij.dbn.language.sql;

import com.dci.intellij.dbn.code.sql.color.SQLTextAttributesKeys;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.execution.statement.StatementGutterRenderer;
import com.dci.intellij.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.dci.intellij.dbn.language.common.psi.ChameleonPsiElement;
import com.dci.intellij.dbn.language.common.psi.ExecutablePsiElement;
import com.dci.intellij.dbn.language.common.psi.IdentifierPsiElement;
import com.dci.intellij.dbn.language.common.psi.NamedPsiElement;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class SQLLanguageAnnotator implements Annotator {
    public static final SQLLanguageAnnotator INSTANCE = new SQLLanguageAnnotator();

    public void annotate(@NotNull final PsiElement psiElement, @NotNull final AnnotationHolder holder) {
        if (psiElement instanceof ExecutablePsiElement)  annotateExecutable(psiElement, holder); else
        if (psiElement instanceof ChameleonPsiElement)  annotateChameleon(psiElement, holder); else
        if (psiElement instanceof IdentifierPsiElement) {
            IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) psiElement;
            ConnectionHandler connectionHandler = identifierPsiElement.getActiveConnection();
            if (connectionHandler != null && !connectionHandler.isVirtual()) {
                annotateIdentifier(psiElement, holder);
            }
        }

        if (psiElement instanceof NamedPsiElement) {
            NamedPsiElement namedPsiElement = (NamedPsiElement) psiElement;
            if (namedPsiElement.hasErrors()) {
                holder.createErrorAnnotation(namedPsiElement, "Invalid " + namedPsiElement.getElementType().getDescription());
            }
        }
    }

    private void annotateIdentifier(final PsiElement psiElement, final AnnotationHolder holder) {
        IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) psiElement;
        if (identifierPsiElement.getLanguageDialect().isReservedWord(identifierPsiElement.getText())) {
            Annotation annotation = holder.createInfoAnnotation(identifierPsiElement, null);
            annotation.setTextAttributes(SQLTextAttributesKeys.IDENTIFIER);
        }
        if (identifierPsiElement.isObject()) {
            annotateObject(identifierPsiElement, holder);
        } else if (identifierPsiElement.isAlias()) {
            if (identifierPsiElement.isReference())
                annotateAliasRef(identifierPsiElement, holder); else
                annotateAliasDef(identifierPsiElement, holder);
        }
    }

    private void annotateAliasRef(IdentifierPsiElement aliasReference, AnnotationHolder holder) {
        if (aliasReference.resolve() == null) {
            Annotation annotation = holder.createWarningAnnotation(aliasReference, "Unknown identifier");
            annotation.setTextAttributes(SQLTextAttributesKeys.UNKNOWN_IDENTIFIER);
        } else {
            Annotation annotation = holder.createInfoAnnotation(aliasReference, null);
            annotation.setTextAttributes(SQLTextAttributesKeys.ALIAS);
        }
    }

    private void annotateAliasDef(IdentifierPsiElement aliasDefinition, AnnotationHolder holder) {
        /*Set<BasePsiElement> aliasDefinitions = new HashSet<BasePsiElement>();
        BasePsiElement scope = aliasDefinition.getEnclosingScopePsiElement();
        scope.collectAliasDefinitionPsiElements(aliasDefinitions, aliasDefinition.getUnquotedText(), DBObjectType.ANY);
        if (aliasDefinitions.size() > 1) {
            holder.createWarningAnnotation(aliasDefinition, "Duplicate alias definition: " + aliasDefinition.getUnquotedText());
        }
        Annotation annotation = holder.createInfoAnnotation(aliasDefinition, null);
        annotation.setTextAttributes(SQLTextAttributesKeys.ALIAS);*/
    }

    private void annotateObject(IdentifierPsiElement objectReference, AnnotationHolder holder) {
        PsiElement reference = objectReference.resolve();
        ConnectionHandler connectionHandler = objectReference.getActiveConnection();
        if (reference == null && connectionHandler != null && connectionHandler.getConnectionStatus().isValid()) {
            if (!objectReference.isDefinition()) {
                Annotation annotation = holder.createWarningAnnotation(objectReference.getNode(),
                        "Unknown identifier");
                annotation.setTextAttributes(SQLTextAttributesKeys.UNKNOWN_IDENTIFIER);
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

    private void annotateChameleon(PsiElement psiElement, AnnotationHolder holder) {
        ChameleonPsiElement executable = (ChameleonPsiElement) psiElement;
/*
        if (!executable.isNestedExecutable()) {
            StatementExecutionProcessor executionProcessor = executable.getExecutionProcessor();
            if (executionProcessor != null) {
                Annotation annotation = holder.createInfoAnnotation(psiElement, null);
                annotation.setGutterIconRenderer(new StatementGutterRenderer(executionProcessor));
            }
        }
*/
    }
}
