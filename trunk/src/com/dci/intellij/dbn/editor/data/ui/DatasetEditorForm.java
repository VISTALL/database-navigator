package com.dci.intellij.dbn.editor.data.ui;

import com.dci.intellij.dbn.common.thread.ConditionalLaterInvocator;
import com.dci.intellij.dbn.common.ui.MessageDialog;
import com.dci.intellij.dbn.common.ui.UIForm;
import com.dci.intellij.dbn.common.ui.UIFormImpl;
import com.dci.intellij.dbn.common.ui.table.BasicTable;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.data.find.DataSearchComponent;
import com.dci.intellij.dbn.data.find.SearchableDataComponent;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.dci.intellij.dbn.editor.data.ui.table.cell.DatasetTableCellEditor;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.util.ui.AsyncProcessIcon;
import com.intellij.util.ui.UIUtil;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableCellEditor;
import java.awt.BorderLayout;
import java.sql.SQLException;

public class DatasetEditorForm extends UIFormImpl implements UIForm, SearchableDataComponent {
    private JPanel actionsPanel;
    private JScrollPane datasetTableScrollPane;
    private JPanel mainPanel;
    private JLabel loadingLabel;
    private JPanel loadingIconPanel;
    private JPanel searchPanel;
    private DatasetEditorTable datasetEditorTable;
    private DataSearchComponent dataSearchComponent;

    public DatasetEditorForm(DatasetEditor datasetEditor) {
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
            MessageDialog.showErrorDialog(datasetEditor.getProject(),
                    "Error opening data editor for " + datasetEditor.getDataset().getQualifiedNameWithType(), e.getMessage(), false);
        }
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
        super.dispose();
        if (dataSearchComponent != null) {
            dataSearchComponent.dispose();
            dataSearchComponent = null;
        }
        datasetEditorTable.dispose();
        datasetEditorTable = null;
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
