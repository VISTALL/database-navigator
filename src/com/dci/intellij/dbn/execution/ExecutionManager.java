package com.dci.intellij.dbn.execution;

import com.dci.intellij.dbn.common.AbstractProjectComponent;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.thread.SimpleLaterInvocator;
import com.dci.intellij.dbn.execution.common.ui.ExecutionConsoleForm;
import com.dci.intellij.dbn.execution.compiler.CompilerResult;
import com.dci.intellij.dbn.execution.method.result.MethodExecutionResult;
import com.dci.intellij.dbn.execution.statement.result.StatementExecutionResult;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentFactoryImpl;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExecutionManager extends AbstractProjectComponent implements JDOMExternalizable, Disposable {
    private static final String TOOL_WINDOW_ID = "DB Execution Console";
    private ExecutionConsoleForm executionConsoleForm;

    private ExecutionManager(Project project) {
        super(project);
    }

    public static ExecutionManager getInstance(Project project) {
        return project.getComponent(ExecutionManager.class);
    }

    public void hideExecutionConsole() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(getProject());
        ToolWindow toolWindow = toolWindowManager.getToolWindow(TOOL_WINDOW_ID);
        if (toolWindow != null) {
            toolWindow.getContentManager().removeAllContents(false);
            toolWindow.setAvailable(false, null);
        }
    }

    private void showExecutionConsole() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(getProject());
        ToolWindow toolWindow = toolWindowManager.getToolWindow(TOOL_WINDOW_ID);
        if (toolWindow == null) {
            toolWindow = toolWindowManager.registerToolWindow(TOOL_WINDOW_ID, false, ToolWindowAnchor.BOTTOM, this, true);
            toolWindow.setIcon(Icons.EXECUTION_CONSOLE);
            toolWindow.setToHideOnEmptyContent(true);
        }

        if (toolWindow.getContentManager().getContents().length == 0) {
            ExecutionConsoleForm executionConsoleForm = getExecutionConsoleForm();
            ContentFactory contentFactory = new ContentFactoryImpl();
            Content content = contentFactory.createContent(executionConsoleForm.getComponent(), null, true);
            toolWindow.getContentManager().addContent(content);
            toolWindow.setAvailable(true, null);
        }

        toolWindow.show(null);
    }

    public void showExecutionConsole(final CompilerResult compilerResult) {
        new SimpleLaterInvocator() {
            public void run() {
                getExecutionConsoleForm().show(compilerResult);
                showExecutionConsole();
            }
        }.start();
    }

    public void showExecutionConsole(final List<CompilerResult> compilerResults) {
        new SimpleLaterInvocator() {
            public void run() {
                getExecutionConsoleForm().show(compilerResults);
                showExecutionConsole();
            }
        }.start();
    }

    public void showExecutionConsole(final StatementExecutionResult executionResult) {
        new SimpleLaterInvocator() {
            public void run() {
                getExecutionConsoleForm().show(executionResult);
                showExecutionConsole();
            }
        }.start();
    }

    public void showExecutionConsole(final MethodExecutionResult executionResult) {
        new SimpleLaterInvocator() {
            public void run() {
                getExecutionConsoleForm().show(executionResult);
                showExecutionConsole();
            }
        }.start();
    }

    public void removeMessagesTab() {
        ExecutionConsoleForm executionConsoleForm = getExecutionConsoleForm();
        executionConsoleForm.removeMessagesTab();
        if (executionConsoleForm.getTabCount() == 0) {
            hideExecutionConsole();
        }
    }

    public void removeResultTab(ExecutionResult executionResult) {
        ExecutionConsoleForm executionConsoleForm = getExecutionConsoleForm();
        executionConsoleForm.removeResultTab(executionResult);
        if (executionConsoleForm.getTabCount() == 0) {
            hideExecutionConsole();
        }
    }

    public void focusResultTab(ExecutionResult executionResult) {
        showExecutionConsole();
        getExecutionConsoleForm().focusResultTab(executionResult);
    }

    public ExecutionConsoleForm getExecutionConsoleForm() {
        if (executionConsoleForm == null) {
            executionConsoleForm = new ExecutionConsoleForm(getProject());
        }
        return executionConsoleForm;
    }

    @NonNls
    @NotNull
    public String getComponentName() {
        return "DBNavigator.Project.ExecutionManager";
    }

    /****************************************
    *            JDOMExternalizable         *
    *****************************************/
    public void readExternal(Element element) throws InvalidDataException {

    }

    public void writeExternal(Element element) throws WriteExternalException {

    }

    public void dispose() {
        if (executionConsoleForm != null) {
            executionConsoleForm.dispose();
            executionConsoleForm = null;
        }
    }

    public ExecutionResult getSelectedExecutionResult() {
        return executionConsoleForm == null ? null : executionConsoleForm.getSelectedExecutionResult();
    }
}
