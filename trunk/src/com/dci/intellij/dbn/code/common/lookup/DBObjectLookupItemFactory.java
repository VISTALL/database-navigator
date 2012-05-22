package com.dci.intellij.dbn.code.common.lookup;

import com.dci.intellij.dbn.code.common.completion.CodeCompletionContext;
import com.dci.intellij.dbn.code.common.completion.CodeCompletionContributor;
import com.dci.intellij.dbn.code.common.completion.CodeCompletionLookupConsumer;
import com.dci.intellij.dbn.code.common.style.DBLCodeStyleManager;
import com.dci.intellij.dbn.code.common.style.options.CodeStyleCaseOption;
import com.dci.intellij.dbn.code.common.style.options.CodeStyleCaseSettings;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.sql.SQLLanguage;
import com.dci.intellij.dbn.object.DBSynonym;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBVirtualObject;
import com.intellij.openapi.project.Project;

import javax.swing.Icon;

public class DBObjectLookupItemFactory extends LookupItemFactory {
    private DBLanguage language;
    private DBObject object;
    private String typeHame;

    public DBObjectLookupItemFactory(DBObject object, DBLanguage language) {
        this.object = object;
        this.language = language;
    }

    @Override
    public DBLookupItem createLookupItem(Object source, CodeCompletionLookupConsumer consumer) {
        DBLookupItem lookupItem = super.createLookupItem(source, consumer);

        if (lookupItem != null) {
            if (needsQuotes()) {
                char quoteChar = consumer.getContext().getIdentifierQuoteChar();
                lookupItem.setLookupString(quoteChar + object.getName() + quoteChar);
            }

/*
            lookupItem.setInsertHandler(consumer.isAddParenthesis() ?
                                BracketsInsertHandler.INSTANCE :
                                BasicInsertHandler.INSTANCE);
*/

        }
        return lookupItem;
    }

    public DBObject getObject() {
        return object;
    }

    private boolean needsQuotes() {
        String name = object.getName();
        return  name.indexOf('#') > -1 ||
                name.indexOf('.') > -1 ||
                object.getLanguageDialect(SQLLanguage.INSTANCE).isReservedWord(name);
    }

    public String getTextHint() {
        if (typeHame == null) {
            DBObject parentObject = object.getParentObject();

            String typePrefix = "";
            if (object instanceof DBSynonym) {
                DBSynonym synonym = (DBSynonym) object;
                typePrefix = synonym.getUnderlyingObject().getTypeName() + " ";
            }

            typeHame = parentObject == null ?
                    typePrefix + object.getTypeName() :
                    typePrefix + object.getTypeName() + " (" +
                       parentObject.getTypeName() + " " +
                       parentObject.getName() + ")";
        }
        return typeHame;
    }

    public boolean isBold() {
        return false;
    }

    @Override
    public String getText(CodeCompletionContext context) {
        Project project = context.getFile().getProject();
        CodeStyleCaseSettings styleCaseSettings = DBLCodeStyleManager.getInstance(project).getCodeStyleCaseSettings(language);
        CodeStyleCaseOption caseOption = styleCaseSettings.getObjectCaseOption();
        String text = caseOption.changeCase(object.getName());

        if (object instanceof DBVirtualObject && text.contains(CodeCompletionContributor.DUMMY_TOKEN)) {
            return null;
        }

        String userInput = context.getUserInput();
        if (userInput != null && userInput.length() > 0 && !text.startsWith(userInput)) {
            char firstInputChar = userInput.charAt(0);
            char firstPresentationChar = text.charAt(0);

            if (Character.toUpperCase(firstInputChar) == Character.toUpperCase(firstPresentationChar)) {
                boolean upperCaseInput = Character.isUpperCase(firstInputChar);
                boolean upperCasePresentation = Character.isUpperCase(firstPresentationChar);

                if (StringUtil.isMixedCase(text)) {
                    if (upperCaseInput != upperCasePresentation) {
                        text = upperCaseInput ?
                                text.toUpperCase() :
                                text.toLowerCase();
                    }
                } else {
                    text = upperCaseInput ?
                            text.toUpperCase() :
                            text.toLowerCase();
                }
            } else {
                return null;
            }
        }

        return text;
    }

    public Icon getIcon() {
        return object.getIcon();
    }
}