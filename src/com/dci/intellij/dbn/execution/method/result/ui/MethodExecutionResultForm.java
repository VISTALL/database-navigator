package com.dci.intellij.dbn.execution.method.result.ui;

import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.ui.tab.TabbedPane;
import com.dci.intellij.dbn.common.ui.table.DBNTable;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.execution.common.result.ui.ExecutionResultForm;
import com.dci.intellij.dbn.execution.method.ArgumentValue;
import com.dci.intellij.dbn.execution.method.result.MethodExecutionResult;
import com.dci.intellij.dbn.execution.method.result.action.CloseExecutionResultAction;
import com.dci.intellij.dbn.execution.method.result.action.EditMethodAction;
import com.dci.intellij.dbn.execution.method.result.action.OpenSettingsAction;
import com.dci.intellij.dbn.execution.method.result.action.PromptMethodExecutionAction;
import com.dci.intellij.dbn.execution.method.result.action.StartMethodExecutionAction;
import com.dci.intellij.dbn.object.DBArgument;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.ui.GuiUtils;
import com.intellij.ui.tabs.TabInfo;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.util.List;

public class MethodExecutionResultForm extends DBNFormImpl implements ExecutionResultForm<MethodExecutionResult> {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JTable inputArgumentsTable;
    private JTable outputArgumentsTable;
    private JSplitPane outputSplitPanel;
    private JPanel simpleOutputsPanel;
    private JPanel outputPanel;
    private JPanel statusPanel;
    private JLabel connectionLabel;
    private JLabel durationLabel;
    private JScrollPane outputScrollPane;
    private JScrollPane inputScrollPane;
    private JPanel cursorOutputsPanel;
    private TabbedPane cursorOutputTabs;


    private MethodExecutionResult executionResult;

    public MethodExecutionResultForm(MethodExecutionResult executionResult) {
        this.executionResult = executionResult;
        cursorOutputTabs = new TabbedPane(executionResult.getProject());
        createActionsPanel(executionResult);
        updateCursorArgumentsPanel();

        if (executionResult.hasCursorResults() && executionResult.hasSimpleResults()) {
            outputSplitPanel.setVisible(true);
            outputPanel.setVisible(false);
        } else {
            outputSplitPanel.removeAll();
            outputSplitPanel.setVisible(false);
            outputPanel.setVisible(true);
            if (executionResult.hasCursorResults()) {
                outputPanel.add(cursorOutputsPanel, BorderLayout.CENTER);
                cursorOutputsPanel.add(cursorOutputTabs, BorderLayout.CENTER);
            } else {
                outputPanel.add(simpleOutputsPanel, BorderLayout.CENTER);
            }
        }
        updateStatusBarLabels();
        GuiUtils.replaceJSplitPaneWithIDEASplitter(mainPanel);
        outputScrollPane.getViewport().setBackground(outputArgumentsTable.getBackground());
        inputScrollPane.getViewport().setBackground(inputArgumentsTable.getBackground());
    }

    public void setExecutionResult(MethodExecutionResult executionResult) {
        if (this.executionResult != executionResult) {
            this.executionResult = executionResult;
            rebuild();
        }
    }

    public MethodExecutionResult getExecutionResult() {
        return executionResult;
    }

    public void rebuild() {
        updateArgumentValueTables();
        updateCursorArgumentsPanel();
        updateStatusBarLabels();
    }

    private void updateArgumentValueTables() {
        List<ArgumentValue> inputArgumentValues = executionResult.getExecutionInput().getArgumentValues();
        List<ArgumentValue> outputArgumentValues = executionResult.getArgumentValues();

        Project project = executionResult.getProject();
        inputArgumentsTable.setModel(new ArgumentValueTableModel(project, inputArgumentValues));
        outputArgumentsTable.setModel(new ArgumentValueTableModel(project, outputArgumentValues));
        ((DBNTable)inputArgumentsTable).accommodateColumnsSize();
        ((DBNTable) outputArgumentsTable).accommodateColumnsSize();
    }

    private void updateCursorArgumentsPanel() {
        cursorOutputTabs.removeAllTabs();
        for (ArgumentValue argumentValue : executionResult.getArgumentValues()) {
            if (argumentValue.isCursor()) {
                DBArgument argument = argumentValue.getArgument();

                MethodExecutionCursorResultForm cursorResultComponent =
                        new MethodExecutionCursorResultForm(executionResult, argument);

                TabInfo tabInfo = new TabInfo(cursorResultComponent.getComponent());
                tabInfo.setText(argument.getName());
                tabInfo.setIcon(argument.getIcon());
                tabInfo.setObject(argument);
                cursorOutputTabs.addTab(tabInfo);
            }
        }
    }

    private void updateStatusBarLabels() {
        connectionLabel.setIcon(executionResult.getConnectionHandler().getIcon());
        connectionLabel.setText(executionResult.getConnectionHandler().getName());

        durationLabel.setText(": " + executionResult.getExecutionDuration() + " ms");
    }



    private void createActionsPanel(MethodExecutionResult executionResult) {
        ActionToolbar actionToolbar = ActionUtil.createActionToolbar(
                "DBNavigator.MethodExecutionResult.Controls", false,
                new CloseExecutionResultAction(executionResult),
                new EditMethodAction(executionResult),
                new StartMethodExecutionAction(executionResult),
                new PromptMethodExecutionAction(executionResult),
                ActionUtil.SEPARATOR,
                new OpenSettingsAction());
        actionsPanel.add(actionToolbar.getComponent());
    }

    public JPanel getComponent() {
        return mainPanel;
    }

    public void dispose() {
        super.dispose();
        executionResult.dispose();
        executionResult = null;
    }

    private void createUIComponents() {
        List<ArgumentValue> inputArgumentValues = executionResult.getExecutionInput().getArgumentValues();
        List<ArgumentValue> outputArgumentValues = executionResult.getArgumentValues();
        Project project = executionResult.getProject();
        inputArgumentsTable = new ArgumentValueTable(new ArgumentValueTableModel(project, inputArgumentValues));
        outputArgumentsTable = new ArgumentValueTable(new ArgumentValueTableModel(project, outputArgumentValues));
    }
}
