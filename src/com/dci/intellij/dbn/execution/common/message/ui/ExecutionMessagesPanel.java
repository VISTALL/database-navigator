package com.dci.intellij.dbn.execution.common.message.ui;

import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.execution.common.message.action.*;
import com.dci.intellij.dbn.execution.common.message.ui.tree.MessagesTree;
import com.dci.intellij.dbn.execution.compiler.CompilerMessage;
import com.dci.intellij.dbn.execution.statement.StatementExecutionMessage;
import com.intellij.openapi.actionSystem.ActionToolbar;

import javax.swing.*;

public class ExecutionMessagesPanel {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JPanel statusPanel;
    private JScrollPane messagesScrollPane;

    private MessagesTree tMessages;

    public ExecutionMessagesPanel() {
        tMessages = new MessagesTree();
        messagesScrollPane.setViewportView(tMessages);

        ActionToolbar actionToolbar = ActionUtil.createActionToolbar(
                "DBNavigator.ExecutionMessages.Controls", false,
                new CloseMessagesWindowAction(tMessages),
                new ViewExecutedStatementAction(tMessages),
                new ExpandMessagesTreeAction(tMessages),
                new CollapseMessagesTreeAction(tMessages),
                ActionUtil.SEPARATOR,
                new OpenSettingsAction(tMessages));
        actionsPanel.add(actionToolbar.getComponent());
    }

    public void addExecutionMessage(StatementExecutionMessage executionMessage, boolean focus) {
        tMessages.addExecutionMessage(executionMessage, focus);
    }

    public void addCompilerMessage(CompilerMessage compilerMessage, boolean focus) {
        tMessages.addCompilerMessage(compilerMessage, focus);
    }

    public void reset() {
        tMessages.reset();
    }

    public JComponent getComponent() {
        return mainPanel;
    }

    public void select(CompilerMessage compilerMessage) {
        tMessages.selectCompilerMessage(compilerMessage);
    }
}
