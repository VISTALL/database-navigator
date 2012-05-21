package com.dci.intellij.dbn.language.editor.action;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.ProjectConnectionManager;
import com.dci.intellij.dbn.options.action.OpenSettingsDialogAction;
import com.intellij.openapi.actionSystem.Anchor;
import com.intellij.openapi.actionSystem.Constraints;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

public class SelectConnectionActionGroup extends DefaultActionGroup {
    private static SelectConnectionAction NO_CONNECTION = new SelectConnectionAction(null);

    public SelectConnectionActionGroup(DatabaseBrowserManager browserManager) {
        add(new OpenSettingsDialogAction(), new Constraints(Anchor.FIRST, null));
        addSeparator();
        add(NO_CONNECTION);
        ProjectConnectionManager projectConnectionManager = ProjectConnectionManager.getInstance(browserManager.getProject());
        for (ConnectionHandler virtualConnectionHandler : projectConnectionManager.getVirtualConnections()) {
            SelectConnectionAction connectionAction = new SelectConnectionAction(virtualConnectionHandler);
            add(connectionAction);
        }

        for (ConnectionManager connectionManager : browserManager.getConnectionManagers()) {
            if (connectionManager.getConnectionHandlers().size() > 0) {
                addSeparator();
                for (ConnectionHandler connectionHandler : connectionManager.getConnectionHandlers()) {
                    SelectConnectionAction connectionAction = new SelectConnectionAction(connectionHandler);
                    add(connectionAction);
                }
            }
        }
    }
}
