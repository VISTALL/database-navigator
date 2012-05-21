package com.dci.intellij.dbn.debugger.settings.ui;

import com.dci.intellij.dbn.common.ui.UIForm;
import com.dci.intellij.dbn.common.ui.UIFormImpl;

import javax.swing.JPanel;

public class DBProgramDebuggerSettingsForm extends UIFormImpl implements UIForm {
    private JPanel mainPanel;

    public JPanel getComponent() {
        return mainPanel;
    }

    public void dispose() {
        super.dispose();
    }
}
