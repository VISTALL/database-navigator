package com.dci.intellij.dbn.common.ui.dialog;

import com.dci.intellij.dbn.common.ui.UIFormImpl;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class DBNDialogWithTimeoutForm extends UIFormImpl{
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JLabel timeLeftLabel;

    public DBNDialogWithTimeoutForm(JComponent contentComponent, int secondsLeft) {
        updateTimeLeft(secondsLeft);
        contentPanel.add(contentComponent, BorderLayout.CENTER);
    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public void updateTimeLeft(int secondsLeft) {
        timeLeftLabel.setText(secondsLeft + " s");
    }
}
