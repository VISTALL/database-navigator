package com.dci.intellij.dbn.connection.transaction.ui;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.transaction.UncommittedChange;
import com.intellij.openapi.project.Project;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class UncommittedChangesTableModel implements TableModel {
    private ConnectionHandler connectionHandler;

    public UncommittedChangesTableModel(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public Project getProject() {
        return connectionHandler.getProject();
    }

    public int getRowCount() {
        return connectionHandler.getUncommittedChanges().size();
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int columnIndex) {
        return
            columnIndex == 0 ? "File" :
            columnIndex == 1 ? "Details" : null ;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return UncommittedChange.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return connectionHandler.getUncommittedChanges().getChanges().get(rowIndex);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}
    public void addTableModelListener(TableModelListener l) {}
    public void removeTableModelListener(TableModelListener l) {}
}
