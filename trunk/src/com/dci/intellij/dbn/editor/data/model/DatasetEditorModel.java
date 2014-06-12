package com.dci.intellij.dbn.editor.data.model;

import com.dci.intellij.dbn.common.thread.ConditionalLaterInvocator;
import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.connection.ConnectionUtil;
import com.dci.intellij.dbn.data.model.resultSet.ResultSetDataModel;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.DatasetEditorError;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilter;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterInput;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterManager;
import com.dci.intellij.dbn.editor.data.options.DataEditorSettings;
import com.dci.intellij.dbn.editor.data.state.DatasetEditorState;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBConstraint;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatasetEditorModel extends ResultSetDataModel<DatasetEditorModelRow> implements ListSelectionListener {
    private boolean isInserting;
    private DatasetEditor datasetEditor;
    private DataEditorSettings settings;
    private DBObjectRef<DBDataset> dataset;

    private List<DatasetEditorModelRow> changedRows = new ArrayList<DatasetEditorModelRow>();

    public DatasetEditorModel(DatasetEditor datasetEditor) throws SQLException {
        super(datasetEditor.getConnectionHandler());
        this.datasetEditor = datasetEditor;
        this.dataset = DBObjectRef.from(datasetEditor.getDataset());
        this.header = new DatasetEditorModelHeader(datasetEditor, null);
        this.settings =  DataEditorSettings.getInstance(datasetEditor.getProject());
    }

    public synchronized void load(ProgressIndicator progressIndicator, boolean useCurrentFilter, boolean keepChanges) throws SQLException {
        if (!isDisposed()) {
            progressIndicator.setText("Loading data for " + dataset.getObjectType().getName() + " " + dataset.getPath());
            load(useCurrentFilter, keepChanges);
        }
    }

    private void load(boolean useCurrentFilter, boolean keepChanges) throws SQLException {
        int originalRowCount = getRowCount();
        int stateRowCount = getState().getRowCount();

        int minRowCount = settings.getGeneralSettings().getFetchBlockSize().value();
        int rowCount = Math.max(stateRowCount, Math.max(originalRowCount, minRowCount));


        Connection connection = connectionHandler.getStandaloneConnection();
        ResultSet newResultSet = loadResultSet(connection, useCurrentFilter);
        ConnectionUtil.closeResultSet(resultSet);
        resultSet = newResultSet;
        resultSetExhausted = false;

        if (keepChanges) snapshotChanges(); else clearChanges();
        fetchNextRecords(rowCount, true);
        restoreChanges();

        new ConditionalLaterInvocator() {
            public void run() {
                DatasetEditorTable editorTable = getEditorTable();
                if (editorTable != null) {
                    editorTable.cancelEditing();
                    editorTable.clearSelection();
                }
            }
        }.start();
    }

    public DataEditorSettings getSettings() {
        return settings;
    }

    @Override
    protected void disposeRow(DatasetEditorModelRow row) {
        if (!changedRows.contains(row)) {
            super.disposeRow(row);
        }
    }

    private ResultSet loadResultSet(Connection connection, boolean useCurrentFilter) throws SQLException {
        DBDataset dataset = getDataset();
        Project project = dataset.getProject();
        DatasetFilter filter = DatasetFilterManager.EMPTY_FILTER;
        if (useCurrentFilter) {
            DatasetFilterManager filterManager = DatasetFilterManager.getInstance(project);
            filter = filterManager.getActiveFilter(dataset);
            if (filter == null) filter = DatasetFilterManager.EMPTY_FILTER;
        }

        String selectStatement = filter.createSelectStatement(dataset, getState().getSortingState());
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        int timeout = settings.getGeneralSettings().getFetchTimeout().value();
        if (timeout != -1) {
            statement.setQueryTimeout(timeout);
        }
        return statement.executeQuery(selectStatement);
    }

    private void snapshotChanges() {
        for (DatasetEditorModelRow row : getRows()) {
            if (row.isDeleted() || row.isModified() || row.isNew()) {
                changedRows.add(row);
            }
        }
    }

    private void restoreChanges() {
        if (hasChanges()) {
            for (DatasetEditorModelRow row : getRows()) {
                DatasetEditorModelRow changedRow = lookupChangedRow(row, true);
                if (changedRow != null) {
                    row.updateStatusFromRow(changedRow);
                }
            }
        }
    }

    private DatasetEditorModelRow lookupChangedRow(DatasetEditorModelRow row, boolean remove) {
        for (DatasetEditorModelRow changedRow : changedRows) {
            if (!changedRow.isDeleted() && changedRow.matches(row, false)) {
                if (remove) changedRows.remove(changedRow);
                return changedRow;
            }
        }
        return null;
    }

    @Override
    public DatasetEditorState getState() {
        return datasetEditor == null ? null : (DatasetEditorState) datasetEditor.getState(FileEditorStateLevel.FULL);
    }

    private boolean hasChanges() {
        return changedRows.size() > 0;
    }

    private void clearChanges() {
        changedRows.clear();
    }

    public boolean isReadonly() {
        return !isEditable();
    }

    public boolean isEditable() {
        DBDataset dataset = this.dataset.get();
        return dataset != null && dataset.isEditable(DBContentType.DATA);
    }

    @Override
    public DatasetEditorModelHeader getHeader() {
        return (DatasetEditorModelHeader) super.getHeader();
    }

    @Override
    protected DatasetEditorModelRow createRow(int resultSetRowIndex) throws SQLException {
        return new DatasetEditorModelRow(this, resultSet, resultSetRowIndex);
    }

    public DBDataset getDataset() {
        return dataset.get();
    }


    @Nullable
    public DatasetEditorTable getEditorTable() {
        return datasetEditor == null ? null : datasetEditor.getEditorTable();
    }

    public boolean isInserting() {
        return isInserting;
    }

    public boolean isModified() {
        if (isInserting) return true;
        for (DatasetEditorModelRow row : getRows()) {
            if (row.isModified() || row.isNew() || row.isDeleted()) return true;
        }
        return changedRows.size() > 0;
    }

    public DatasetFilterInput resolveForeignKeyRecord(DatasetEditorModelCell cell) {
        DBColumn column = cell.getColumnInfo().getColumn();
        if (column != null && column.isForeignKey()) {
            for (DBConstraint constraint : column.getConstraints()) {
                constraint = (DBConstraint) constraint.getUndisposedElement();
                if (constraint != null && constraint.isForeignKey()) {
                    DBDataset foreignKeyDataset = constraint.getForeignKeyConstraint().getDataset();
                    DatasetFilterInput filterInput = new DatasetFilterInput(foreignKeyDataset);

                    for (DBColumn constraintColumn : constraint.getColumns()) {
                        DBColumn foreignKeyColumn = ((DBColumn) constraintColumn.getUndisposedElement()).getForeignKeyColumn();
                        Object value = cell.getRow().getCellForColumn(constraintColumn).getUserValue();
                        filterInput.setColumnValue(foreignKeyColumn, value);
                    }
                    return filterInput;
                }
            }
        }
        return null;
    }

    /****************************************************************
     *                        Editor actions                        *
     ****************************************************************/
    public void deleteRecords(int[] rowIndexes) {
        DatasetEditorTable editorTable = getEditorTable();
        if (editorTable != null) {
            editorTable.fireEditingCancel();
            for (int index : rowIndexes) {
                DatasetEditorModelRow row = getRowAtIndex(index);
                if (!row.isDeleted()) {
                    int rsRowIndex = row.getResultSetRowIndex();
                    row.delete();
                    if (row.isDeleted()) {
                        shiftResultSetRowIndex(rsRowIndex, -1);
                        notifyRowUpdated(index);
                    }
                }
            }
            getConnectionHandler().notifyChanges(getDataset().getVirtualFile());
        }
    }

    public void insertRecord(int rowIndex) {
        DatasetEditorTable editorTable = getEditorTable();
        if (editorTable != null) {
            try {
                editorTable.stopCellEditing();
                resultSet.moveToInsertRow();
                DatasetEditorModelRow newRow = createRow(getRowCount()+1);
                newRow.setInsert(true);
                addRowAtIndex(rowIndex, newRow);
                notifyRowsInserted(rowIndex, rowIndex);

                editorTable.selectCell(rowIndex, editorTable.getSelectedColumn() == -1 ? 0 : editorTable.getSelectedColumn());
                isInserting = true;
                getConnectionHandler().notifyChanges(getDataset().getVirtualFile());
            } catch (SQLException e) {
                MessageUtil.showErrorDialog("Could not insert record for " + getDataset().getQualifiedNameWithType() + ".", e);
            }
        }
    }

    public void duplicateRecord(int rowIndex) {
        DatasetEditorTable editorTable = getEditorTable();
        if (editorTable != null) {
            try {
                editorTable.stopCellEditing();
                int insertIndex = rowIndex + 1;
                resultSet.moveToInsertRow();
                DatasetEditorModelRow oldRow = getRowAtIndex(rowIndex);
                DatasetEditorModelRow newRow = createRow(getRowCount() + 1);
                newRow.setInsert(true);
                newRow.updateDataFromRow(oldRow);
                addRowAtIndex(insertIndex, newRow);
                notifyRowsInserted(insertIndex, insertIndex);

                editorTable.selectCell(insertIndex, editorTable.getSelectedColumn());
                isInserting = true;
                getConnectionHandler().notifyChanges(getDataset().getVirtualFile());
            } catch (SQLException e) {
                MessageUtil.showErrorDialog("Could not duplicate record in " + getDataset().getQualifiedNameWithType() + ".", e);
            }
        }
    }

    public void postInsertRecord(boolean propagateError, boolean rebuild) throws SQLException {
        DatasetEditorTable editorTable = getEditorTable();
        if (editorTable != null) {
            DatasetEditorModelRow row = getInsertRow();
            try {
                editorTable.stopCellEditing();
                resultSet.insertRow();
                resultSet.moveToCurrentRow();
                row.setInsert(false);
                row.setNew(true);
                isInserting = false;
                if (rebuild) load(true, true);
            } catch (SQLException e) {
                DatasetEditorError error = new DatasetEditorError(getConnectionHandler(), e);
                row.notifyError(error, true, true);
                if (error.isNotified()) {
                    try {
                        resultSet.moveToInsertRow();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }

                if (!error.isNotified() || propagateError) throw e;
            }
        }
    }

    public void cancelInsert(boolean notifyListeners) {
        DatasetEditorTable editorTable = getEditorTable();
        if (editorTable != null) {
            try {
                editorTable.fireEditingCancel();
                DatasetEditorModelRow insertRow = getInsertRow();
                int rowIndex = insertRow.getIndex();
                removeRowAtIndex(rowIndex);
                resultSet.moveToCurrentRow();
                isInserting = false;
                if (notifyListeners) notifyRowsDeleted(rowIndex, rowIndex);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * after delete or insert performed on a result set, the row indexes have to be shifted accordingly
     */
    public void shiftResultSetRowIndex(int fromIndex, int shifting) {
        for (DatasetEditorModelRow row : getRows()) {
            if (row.getResultSetRowIndex() > fromIndex) {
                row.shiftResultSetRowIndex(shifting);
            }
        }
    }

    @Nullable
    public DatasetEditorModelRow getInsertRow() {
        for (DatasetEditorModelRow row : getRows()) {
            if (row.isInsert()) {
                return row;
            }
        }
        return null;
    }

    public int getInsertRowIndex() {
        DatasetEditorModelRow insertRow = getInsertRow();
        return insertRow == null ? -1 : insertRow.getIndex();
    }

    public void revertChanges() {
        for (DatasetEditorModelRow row : getRows()) {
            row.revertChanges();
        }
    }

    /*********************************************************
     *                      DataModel                       *
     *********************************************************/
    public DatasetEditorModelCell getCellAt(int rowIndex, int columnIndex) {
        return (DatasetEditorModelCell) super.getCellAt(rowIndex, columnIndex);
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        DatasetEditorModelCell cell = getCellAt(rowIndex, columnIndex);
        cell.updateUserValue(value, false);
    }

    public void setValueAt(Object value, String errorMessage,  int rowIndex, int columnIndex) {
        DatasetEditorModelCell cell = getCellAt(rowIndex, columnIndex);
        cell.updateUserValue(value, errorMessage);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        DatasetEditorTable editorTable = getEditorTable();
        if (editorTable != null) {
            if (!isReadonly() && !getState().isReadonly() && getConnectionHandler().isConnected()) {
                if (!editorTable.isLoading() && editorTable.getSelectedColumnCount() <= 1 && editorTable.getSelectedRowCount() <= 1) {
                    DatasetEditorModelRow row = getRowAtIndex(rowIndex);
                    return row != null && !(isInserting && !row.isInsert()) && !row.isDeleted();
                }
            }
        }
        return false;
    }

    /*********************************************************
     *                ListSelectionListener                  *
     *********************************************************/
    public void valueChanged(ListSelectionEvent event) {
        if (isInserting && !event.getValueIsAdjusting()) {
            DatasetEditorModelRow insertRow = getInsertRow();
            if (insertRow != null) {
                int index = insertRow.getIndex();

                ListSelectionModel listSelectionModel = (ListSelectionModel) event.getSource();
                int selectionIndex = listSelectionModel.getLeadSelectionIndex();

                if (index != selectionIndex) {
                    //postInsertRecord();
                }
            }
        }
    }

    /*********************************************************
     *                       Disposable                      *
     *********************************************************/
    @Override
    public void dispose() {
        super.dispose();
        dataset = null;
        datasetEditor = null;
        changedRows.clear();
        settings = null;
    }
}
