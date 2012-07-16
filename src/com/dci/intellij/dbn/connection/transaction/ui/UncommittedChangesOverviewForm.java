package com.dci.intellij.dbn.connection.transaction.ui;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.common.ui.UIForm;
import com.dci.intellij.dbn.common.ui.UIFormImpl;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.GuiUtils;
import com.intellij.ui.SimpleTextAttributes;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UncommittedChangesOverviewForm extends UIFormImpl implements UIForm {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JPanel detailsPanel;
    private JList connectionsList;
    private List<ConnectionHandler> connectionHandlers = new ArrayList<ConnectionHandler>();

    private Map<ConnectionHandler, UncommittedChangesForm> uncommittedChangeForms = new HashMap<ConnectionHandler, UncommittedChangesForm>();

    public UncommittedChangesOverviewForm(Project project, String hintText) {
        DefaultListModel model = new DefaultListModel();

        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
        for (ConnectionManager connectionManager : browserManager.getConnectionManagers()) {
            for (ConnectionHandler connectionHandler : connectionManager.getConnectionHandlers()) {
                if (connectionHandler.hasUncommittedChanges()) {
                    connectionHandlers.add(connectionHandler);
                    model.addElement(connectionHandler);
                }
            }
        }


        connectionsList.setModel(model);
        connectionsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ConnectionHandler connectionHandler = (ConnectionHandler) connectionsList.getSelectedValue();
                showChangesForm(connectionHandler);
            }
        });
        connectionsList.setCellRenderer(new ListCellRenderer());

/*
        ActionToolbar actionToolbar = ActionUtil.createActionToolbar("", true,
                new ShowGroupedTreeAction(tree),
                new DeleteHistoryEntryAction(tree),
                ActionUtil.SEPARATOR,
                new OpenSettingsAction());
        actionsPanel.add(actionToolbar.getComponent());
*/
        GuiUtils.replaceJSplitPaneWithIDEASplitter(mainPanel);
    }

    public JPanel getComponent() {
        return mainPanel;
    }

    public void dispose() {
        super.dispose();
    }

    public List<ConnectionHandler> getConnectionHandlers (){
        return connectionHandlers;
    }

    public void showChangesForm(ConnectionHandler connectionHandler) {
        detailsPanel.removeAll();
        if (connectionHandler != null) {
            UncommittedChangesForm uncommittedChangesForm = uncommittedChangeForms.get(connectionHandler);
            if (uncommittedChangesForm == null) {
                uncommittedChangesForm = new UncommittedChangesForm(connectionHandler, null);
                uncommittedChangeForms.put(connectionHandler, uncommittedChangesForm);
            }
            detailsPanel.add(uncommittedChangesForm.getComponent(), BorderLayout.CENTER);
        }
        detailsPanel.updateUI();
    }

    private class ListCellRenderer extends ColoredListCellRenderer {

        @Override
        protected void customizeCellRenderer(JList list, Object value, int index, boolean selected, boolean hasFocus) {
            ConnectionHandler connectionHandler = (ConnectionHandler) value;
            setIcon(connectionHandler.getIcon());
            append(connectionHandler.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            append(" (" + connectionHandler.getUncommittedChanges().size() + ")", SimpleTextAttributes.GRAY_ATTRIBUTES);

        }
    }
}
