package com.dci.intellij.dbn.debugger.execution.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.debugger.execution.DBProgramRunConfiguration;
import com.dci.intellij.dbn.execution.method.MethodExecutionInput;
import com.dci.intellij.dbn.execution.method.MethodExecutionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public class OpenMethodHistoryAction extends AbstractSelectMethodAction {
    public OpenMethodHistoryAction(DBProgramRunConfiguration configuration) {
        super("Execution History", Icons.METHOD_EXECUTION_HISTORY, configuration);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        MethodExecutionManager methodExecutionManager = MethodExecutionManager.getInstance(project);
        MethodExecutionInput currentInput = getConfiguration().getExecutionInput();
        MethodExecutionInput methodExecutionInput = methodExecutionManager.selectHistoryMethodExecutionInput(currentInput);
        if (methodExecutionInput != null) {
            getConfiguration().setExecutionInput(methodExecutionInput);
        }
    }
}
