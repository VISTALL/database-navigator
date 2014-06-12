package com.dci.intellij.dbn.editor.data.ui;

import com.dci.intellij.dbn.common.thread.ConditionalLaterInvocator;
import com.dci.intellij.dbn.common.ui.AutoCommitLabel;
import com.dci.intellij.dbn.common.ui.DBNForm;
import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.ui.dialog.MessageDialog;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.data.find.DataSearchComponent;
import com.dci.intellij.dbn.data.find.SearchableDataComponent;
import com.dci.intellij.dbn.data.ui.table.basic.BasicTable;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.state.column.DatasetColumnState;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.dci.intellij.dbn.editor.data.ui.table.cell.DatasetTableCellEditor;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.util.ui.AsyncProcessIcon;
import com.intellij.util.ui.UIUtil;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatasetEditorForm extends DBNFormImpl implements DBNForm, SearchableDataComponent {
    private JPanel actionsPanel;
    private JScrollPane datasetTableScrollPane;
    private JPanel mainPanel;
    private JLabel loadingLabel;
    private JPanel loadingIconPanel;
    private JPanel searchPanel;
    private AutoCommitLabel autoCommitLabel;
    private DatasetEditorTable datasetEditorTable;
    private DataSearchComponent dataSearchComponent;

    private DatasetEditor datasetEditor;

    public DatasetEditorForm(DatasetEditor datasetEditor) {
        this.datasetEditor = datasetEditor;
        DBDataset dataset = getDataset();
        try {
            datasetEditorTable = new DatasetEditorTable(datasetEditor);
            datasetTableScrollPane.setViewportView(datasetEditorTable);
            datasetTableScrollPane.setRowHeaderView(datasetEditorTable.getTableGutter());


            JPanel panel = new JPanel();
            panel.setBorder(UIUtil.getTableHeaderCellBorder());
            datasetTableScrollPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, panel);

            ActionToolbar actionToolbar = ActionUtil.createActionToolbar("", true, "DBNavigator.ActionGroup.DataEditor");
            actionToolbar.setTargetComponent(actionsPanel);

            actionsPanel.add(actionToolbar.getComponent(), BorderLayout.WEST);
            loadingIconPanel.add(new AsyncProcessIcon("Loading"), BorderLayout.CENTER);
            hideLoadingHint();
            datasetTableScrollPane.getViewport().setBackground(datasetEditorTable.getBackground());

            ActionUtil.registerDataProvider(actionsPanel, datasetEditor.getDataProvider(), true);
        } catch (SQLException e) {

            MessageDialog.showErrorDialog(
                    datasetEditor.getProject(),
                    "Error opening data editor for " + dataset.getQualifiedNameWithType(), e.getMessage(), false);
        }

        if (dataset.isEditable(DBContentType.DATA)) {
            ConnectionHandler connectionHandler = getConnectionHandler();
            autoCommitLabel.setConnectionHandler(connectionHandler);
        }
    }

    public DatasetEditorTable beforeRebuild() throws SQLException {
        DatasetEditorTable oldEditorTable = datasetEditorTable;
        datasetEditorTable = new DatasetEditorTable(datasetEditor);

        List<TableColumn> hiddenColumns = new ArrayList<TableColumn>();
        for (DatasetColumnState columnState : datasetEditor.getState().getHeaderState().getColumnStates()) {
            if (!columnState.isVisible()) {
                int columnIndex = columnState.getPosition();
                TableColumn tableColumn = datasetEditorTable.getColumnModel().getColumn(columnIndex);
                hiddenColumns.add(tableColumn);
            }
        }
        for (TableColumn hiddenColumn : hiddenColumns) {
            datasetEditorTable.removeColumn(hiddenColumn);
        }
        return oldEditorTable;
    }

    public void afterRebuild(final DatasetEditorTable oldEditorTable) {
        if (oldEditorTable != null) {
            new ConditionalLaterInvocator(){
                @Override
                public void run() {
                    datasetTableScrollPane.setViewportView(datasetEditorTable);
                    datasetTableScrollPane.setRowHeaderView(datasetEditorTable.getTableGutter());
                    datasetTableScrollPane.getViewport().setBackground(datasetEditorTable.getBackground());
                    oldEditorTable.dispose();
                }
            }.start();
        }
    }


    public void rebuild() {
        try {
            DatasetEditorTable oldDatasetEditorTable = datasetEditorTable;
            datasetEditorTable = new DatasetEditorTable(datasetEditor);

            List<TableColumn> hiddenColumns = new ArrayList<TableColumn>();
            for (DatasetColumnState columnState : datasetEditor.getState().getHeaderState().getColumnStates()) {
                if (!columnState.isVisible()) {
                    int columnIndex = columnState.getPosition();
                    TableColumn tableColumn = datasetEditorTable.getColumnModel().getColumn(columnIndex);
                    hiddenColumns.add(tableColumn);
                }
            }
            for (TableColumn hiddenColumn : hiddenColumns) {
                datasetEditorTable.removeColumn(hiddenColumn);
            }

            datasetTableScrollPane.setViewportView(datasetEditorTable);
            datasetTableScrollPane.setRowHeaderView(datasetEditorTable.getTableGutter());
            oldDatasetEditorTable.dispose();

        } catch (SQLException e) {
            MessageDialog.showErrorDialog(datasetEditor.getProject(),
                    "Error opening creating editor for " + getDataset().getQualifiedNameWithType(), e.getMessage(), false);
        }

    }

    private DBDataset getDataset() {
        return datasetEditor.getDataset();
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
        if (!isDisposed()) {
            super.dispose();
            autoCommitLabel.dispose();
            if (dataSearchComponent != null) {
                dataSearchComponent.dispose();
                dataSearchComponent = null;
            }
            datasetEditorTable.dispose();
            datasetEditorTable = null;
            datasetEditor = null;
        }
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
