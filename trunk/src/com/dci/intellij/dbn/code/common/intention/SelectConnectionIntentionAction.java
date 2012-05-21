package com.dci.intellij.dbn.code.common.intention;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.ProjectConnectionManager;
import com.dci.intellij.dbn.language.common.DBLanguageFile;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.options.ui.GlobalProjectSettingsDialog;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

public class SelectConnectionIntentionAction extends GenericIntentionAction {
    @NotNull
    public String getText() {
        return "Select connection...";
    }

    @NotNull
    public String getFamilyName() {
        return "DBNavigator environment intentions";
    }

    public Icon getIcon(int flags) {
        return Icons.FILE_CONNECTION_MAPPING;
    }

    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        VirtualFile virtualFile = psiFile.getVirtualFile();
        if (psiFile instanceof DBLanguageFile && virtualFile != null && virtualFile.isInLocalFileSystem()) {
            //DBLanguageFile file = (DBLanguageFile) psiFile;
            return true;
        }
        return false;
    }

    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
        List<ConnectionManager> connectionManagers = browserManager.getConnectionManagers();

        DefaultActionGroup actionGroup = new DefaultActionGroup();
        DBLanguageFile dbLanguageFile = (DBLanguageFile) psiFile;

        boolean connectionsFound = false;
        for (ConnectionManager connectionManager : connectionManagers) {
            if (connectionManager.getConnectionHandlers().size() > 0) {
                actionGroup.addSeparator();
                for (ConnectionHandler connectionHandler : connectionManager.getConnectionHandlers()) {

                    SelectConnectionAction connectionAction = new SelectConnectionAction(connectionHandler, dbLanguageFile);
                    actionGroup.add(connectionAction);
                    connectionsFound = true;
                }
            }
        }

        if (connectionsFound) actionGroup.addSeparator();
        ProjectConnectionManager projectConnectionManager = ProjectConnectionManager.getInstance(browserManager.getProject());
        for (ConnectionHandler virtualConnectionHandler : projectConnectionManager.getVirtualConnections()) {
            SelectConnectionAction connectionAction = new SelectConnectionAction(virtualConnectionHandler, dbLanguageFile);
            actionGroup.add(connectionAction);
        }

        actionGroup.addSeparator();
        actionGroup.add(new SetupConnectionAction());

        ListPopup popupBuilder = JBPopupFactory.getInstance().createActionGroupPopup(
                "Select connection",
                actionGroup,
                DataManager.getInstance().getDataContext(editor.getComponent()),
                JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                true);

        popupBuilder.showCenteredInCurrentWindow(project);


        /*
        // old dialog approach
        DBLanguageFile file = (DBLanguageFile) psiFile;
        SelectConnectionDialog selectCurrentSchemaDialog = new SelectConnectionDialog(file);
        selectCurrentSchemaDialog.show();
        */
    }




    private class SelectConnectionAction extends AnAction {
        private ConnectionHandler connectionHandler;
        private DBLanguageFile file;

        private SelectConnectionAction(ConnectionHandler connectionHandler, DBLanguageFile file) {
            super(connectionHandler.getName(), null, connectionHandler.getIcon());
            this.file = file;
            this.connectionHandler = connectionHandler;
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            DBSchema currentSchema = connectionHandler.getUserSchema();
            file.setActiveConnection(connectionHandler);
            file.setCurrentSchema(currentSchema);

        }
    }

    private class SetupConnectionAction extends AnAction {
        private SetupConnectionAction() {
            super("Setup new connection", null, Icons.CONNECTION_NEW);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            Project project = ActionUtil.getProject(e);
            GlobalProjectSettingsDialog globalSettingsDialog = new GlobalProjectSettingsDialog(project);
            globalSettingsDialog.show();
        }
    }

    public boolean startInWriteAction() {
        return false;
    }
}
