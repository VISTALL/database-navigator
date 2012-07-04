package com.dci.intellij.dbn.editor.data.ui;

import com.dci.intellij.dbn.common.event.EventManager;
import com.dci.intellij.dbn.common.thread.ConditionalLaterInvocator;
import com.dci.intellij.dbn.common.ui.MessageDialog;
import com.dci.intellij.dbn.common.ui.UIForm;
import com.dci.intellij.dbn.common.ui.UIFormImpl;
import com.dci.intellij.dbn.common.ui.table.BasicTable;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionSettingsChangeListener;
import com.dci.intellij.dbn.data.find.DataSearchComponent;
import com.dci.intellij.dbn.data.find.SearchableDataComponent;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.dci.intellij.dbn.editor.data.ui.table.cell.DatasetTableCellEditor;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.AsyncProcessIcon;
import com.intellij.util.ui.UIUtil;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableCellEditor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.SQLException;

public class DatasetEditorForm extends UIFormImpl implements UIForm, SearchableDataComponent, ConnectionSettingsChangeListener {
    private JPanel actionsPanel;
    private JScrollPane datasetTableScrollPane;
    private JPanel mainPanel;
    private JLabel loadingLabel;
    private JPanel loadingIconPanel;
    private JPanel searchPanel;
    private JLabel autoCommitLabel;
    private DatasetEditorTable datasetEditorTable;
    private DataSearchComponent dataSearchComponent;

    public DatasetEditorForm(DatasetEditor datasetEditor) {
        Project project = datasetEditor.getProject();
        try {
            datasetEditorTable = new DatasetEditorTable(datasetEditor);
            datasetTableScrollPane.setViewportView(datasetEditorTable);
            datasetTableScrollPane.setRowHeaderView(datasetEditorTable.getTableGutter());


            JPanel panel = new JPanel();
            panel.setBorder(UIUtil.getTableHeaderCellBorder());
            datasetTableScrollPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, panel);
            ActionToolbar actionToolbar = ActionUtil.createActionToolbar("", true, "DBNavigator.ActionGroup.DataEditor");

            actionsPanel.add(actionToolbar.getComponent(), BorderLayout.WEST);
            loadingIconPanel.add(new AsyncProcessIcon("Loading"), BorderLayout.CENTER);
            hideLoadingHint();
            datasetTableScrollPane.getViewport().setBackground(datasetEditorTable.getBackground());

            ActionUtil.registerDataProvider(actionsPanel, datasetEditor.getDataProvider(), true);
        } catch (SQLException e) {
            MessageDialog.showErrorDialog(project,
                    "Error opening data editor for " + datasetEditor.getDataset().getQualifiedNameWithType(), e.getMessage(), false);
        }

        ConnectionHandler connectionHandler = getConnectionHandler();
        updateAutoCommitInfo(connectionHandler);
        EventManager.subscribe(project, ConnectionSettingsChangeListener.TOPIC, this);
    }

    public JPanel getComponent() {
        return mainPanel;
    }

    public void showLoadingHint() {
        new ConditionalLaterInvocator() {
            public void run() {
                loadingLabel.setVisible(true);
                loadingIconPanel.setVisible(true);
            }
        }.start();
    }

    public void hideLoadingHint() {
        new ConditionalLaterInvocator() {
            public void run() {
                loadingLabel.setVisible(false);
                loadingIconPanel.setVisible(false);
            }
        }.start();
    }


    public DatasetEditorTable getEditorTable() {
        return datasetEditorTable;
    }

    public void dispose() {
        EventManager.unsubscribe(getConnectionHandler().getProject(), this);
        super.dispose();
        if (dataSearchComponent != null) {
            dataSearchComponent.dispose();
            dataSearchComponent = null;
        }
        datasetEditorTable.dispose();
        datasetEditorTable = null;
    }

    @Override
    public void connectionSettingsChanged(String connectionId) {
        ConnectionHandler connectionHandler = getConnectionHandler();
        if (connectionHandler.getId().equals(connectionId)) {
            updateAutoCommitInfo(connectionHandler);
        }
    }

    private void updateAutoCommitInfo(ConnectionHandler connectionHandler) {
        autoCommitLabel.setText(connectionHandler.isAutoCommit() ? "Auto Commit ON" : "Auto Commit OFF");
        autoCommitLabel.setForeground(connectionHandler.isAutoCommit() ? new Color(190, 0, 0): new Color(0, 190, 0));
    }

    private ConnectionHandler getConnectionHandler() {
        return datasetEditorTable.getDataset().getConnectionHandler();
    }

    public float getHorizontalScrollProportion() {
        datasetTableScrollPane.getHorizontalScrollBar().getModel();
        return 0;
    }

    /*********************************************************
     *              SearchableDataComponent                  *
     *********************************************************/
    public void showSearchHeader() {
        datasetEditorTable.cancelEditing();
        datasetEditorTable.clearSelection();

        if (dataSearchComponent == null) {
            dataSearchComponent = new DataSearchComponent(this);
            ActionUtil.registerDataProvider(dataSearchComponent.getSearchField(), datasetEditorTable.getDatasetEditor().getDataProvider(), false);
            searchPanel.add(dataSearchComponent, BorderLayout.CENTER);
        } else {
            dataSearchComponent.initializeFindModel();
        }
        if (searchPanel.isVisible()) {
            dataSearchComponent.getSearchField().selectAll();
        } else {
            searchPanel.setVisible(true);    
        }
        dataSearchComponent.getSearchField().requestFocus();

    }

    public void hideSearchHeader() {
        dataSearchComponent.resetFindModel();
        searchPanel.setVisible(false);
        datasetEditorTable.repaint();
        datasetEditorTable.requestFocus();
    }

    @Override
    public void cancelEditActions() {
        datasetEditorTable.cancelEditing();
    }

    @Override
    public String getSelectedText() {
        TableCellEditor cellEditor = datasetEditorTable.getCellEditor();
        if (cellEditor instanceof DatasetTableCellEditor) {
            DatasetTableCellEditor tableCellEditor = (DatasetTableCellEditor) cellEditor;
            return tableCellEditor.getTextField().getSelectedText();
        }
        return null;
    }

    @Override
    public BasicTable getTable() {
        return datasetEditorTable;
    }
}
