package com.dci.intellij.dbn.debugger.execution.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.util.NamingUtil;
import com.dci.intellij.dbn.debugger.execution.DBProgramRunConfiguration;
import com.dci.intellij.dbn.execution.method.MethodExecutionInput;
import com.dci.intellij.dbn.object.DBMethod;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;

public class SelectMethodAction extends AbstractSelectMethodAction{
    private MethodExecutionInput executionInput;

    public SelectMethodAction(MethodExecutionInput executionInput, DBProgramRunConfiguration configuration) {
        super("", null, configuration);
        this.executionInput = executionInput;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        getConfiguration().setExecutionInput(executionInput);
    }

    @Override
    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        DBMethod method = executionInput.getMethod();
        if (method == null) {
            presentation.setIcon(Icons.DBO_METHOD);
        } else {
            presentation.setIcon(method.getOriginalIcon());
        }
        presentation.setText(NamingUtil.enhanceNameForDisplay(executionInput.getMethodRef().getPath()));
    }
}
