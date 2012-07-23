package com.dci.intellij.dbn.common.option;


import com.dci.intellij.dbn.common.Constants;
import com.dci.intellij.dbn.common.Icons;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;

public class PersistableOption implements DialogWrapper.DoNotAskOption{
    private Integer selectedOption;
    private String[] options;
    private int maxPersistableOption;

    public PersistableOption(int maxPersistableOption, String... options) {
        this.options = options;
        this.maxPersistableOption = maxPersistableOption;
    }

    public String[] getOptions() {
        return options;
    }

    public Integer getSelectedOption() {
        return selectedOption;
    }

    @Override
    public boolean isToBeShown() {
        return true;
    }

    @Override
    public void setToBeShown(boolean keepAsking, int selectedOption) {
        if (keepAsking || selectedOption > maxPersistableOption) {
            this.selectedOption = null;
        } else {
            this.selectedOption = selectedOption;
        }
    }

    @Override
    public boolean canBeHidden() {
        return true;
    }

    @Override
    public boolean shouldSaveOptionsOnCancel() {
        return false;
    }

    @Override
    public String getDoNotShowMessage() {
        return "Remember option";
    }

    public int resolve(String message, String title) {
        if (selectedOption != null) {
            return selectedOption;
        } else {
            return Messages.showDialog(message, Constants.DBN_TITLE_PREFIX + title, options, 0, Icons.DIALOG_WARNING, this);
        }
    }
}
