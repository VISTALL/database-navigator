package com.dci.intellij.dbn.execution.method.browser.action;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.common.ui.DBNComboBoxAction;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.NamingUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.ProjectConnectionManager;
import com.dci.intellij.dbn.execution.method.browser.ui.MethodExecutionBrowserForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import javax.swing.JComponent;

public class SelectConnectionComboBoxAction extends DBNComboBoxAction {
    MethodExecutionBrowserForm browserComponent;

    public SelectConnectionComboBoxAction(MethodExecutionBrowserForm browserComponent) {
        this.browserComponent = browserComponent;
    }

    @NotNull
    protected DefaultActionGroup createPopupActionGroup(JComponent component) {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        Project project = ActionUtil.getProject(component);
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
        ProjectConnectionManager projectConnectionManager = ProjectConnectionManager.getInstance(browserManager.getProject());
        for (ConnectionHandler virtualConnectionHandler : projectConnectionManager.getVirtualConnections()) {
            SelectConnectionAction connectionAction = new SelectConnectionAction(browserComponent, virtualConnectionHandler);
            actionGroup.add(connectionAction);
        }

        for (ConnectionManager connectionManager : browserManager.getConnectionManagers()) {
            if (connectionManager.getConnectionHandlers().size() > 0) {
                actionGroup.addSeparator();
                for (ConnectionHandler connectionHandler : connectionManager.getConnectionHandlers()) {
                    SelectConnectionAction connectionAction = new SelectConnectionAction(browserComponent, connectionHandler);
                    actionGroup.add(connectionAction);
                }
            }
        }

        return actionGroup;
    }

    public synchronized void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        String text = "Select connection";
        Icon icon = null;

        ConnectionHandler connectionHandler = browserComponent.getSettings().getConnectionHandler();
        if (connectionHandler != null) {
            text = NamingUtil.enhanceUnderscoresForDisplay(connectionHandler.getQualifiedName());
            icon = connectionHandler.getIcon();
        }

        presentation.setText(text);
        presentation.setIcon(icon);
    }
 }