package com.dci.intellij.dbn.execution.compiler.ui;

import com.dci.intellij.dbn.common.ui.DBNForm;
import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import org.jetbrains.annotations.Nullable;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CompilerTypeSelectionForm extends DBNFormImpl implements DBNForm {
    private JPanel mainPanel;
    private JLabel programLabel;
    private JPanel headerPanel;
    private JCheckBox rememberSelectionCheckBox;
    private JTextArea hintTextArea;

    public CompilerTypeSelectionForm(@Nullable DBSchemaObject object) {
        if (object == null) {
            headerPanel.setVisible(false);
        } else {
            programLabel.setIcon(object.getOriginalIcon());
            programLabel.setText(object.getQualifiedName());
            if (getEnvironmentSettings(object.getProject()).getVisibilitySettings().getDialogHeaders().value()) {
                headerPanel.setBackground(object.getEnvironmentType().getColor());
            }
        }
        hintTextArea.setFont(mainPanel.getFont());
        hintTextArea.setBackground(mainPanel.getBackground());
        hintTextArea.setText(StringUtil.wrap(
                "The compile option type \"Debug\" enables you to use the selected object(s) in debugging activities (i.e. pause/trace execution). " +
                "For runtime performance reasons, it is recommended to use normal compile option, unless you plan to debug the selected element(s)." +
                "\"Keep current\" will carry over the existing compile type.\n\n" +
                "Please select your compile option.", 90, ": ,."));
    }

    protected boolean rememberSelection() {
        return rememberSelectionCheckBox.isSelected();
    }

    public JComponent getComponent() {
        return mainPanel;
    }

    public void dispose() {
        super.dispose();
    }
}
