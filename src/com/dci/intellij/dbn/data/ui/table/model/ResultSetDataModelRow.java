package com.dci.intellij.dbn.data.ui.table.model;

import com.dci.intellij.dbn.common.ui.table.model.ColumnInfo;
import com.dci.intellij.dbn.common.ui.table.model.SortableDataModelRow;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ResultSetDataModelRow<T extends ResultSetDataModelCell> extends SortableDataModelRow<T> {
    public ResultSetDataModelRow(ResultSetDataModel model, ResultSet resultSet) throws SQLException {
        super(model);
        for (int i = 0; i < model.getColumnCount(); i++) {
            ColumnInfo columnInfo = getModel().getColumnInfo(i);
            T cell = createCell(resultSet, columnInfo);
            getCells().add(cell);
        }
    }

    @Override
    public ResultSetDataModel getModel() {
        return (ResultSetDataModel) super.getModel();
    }

    protected T createCell(ResultSet resultSet, ColumnInfo columnInfo) throws SQLException {
        return (T) new ResultSetDataModelCell(this, resultSet, columnInfo);
    }
}
