package com.dci.intellij.dbn.data.ui.table.model;

import com.dci.intellij.dbn.common.ui.table.model.ColumnInfo;
import com.dci.intellij.dbn.common.ui.table.model.SortableDataModelCell;
import com.dci.intellij.dbn.data.type.DBDataType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetDataModelCell extends SortableDataModelCell {
    public ResultSetDataModelCell(ResultSetDataModelRow row, ResultSet resultSet, ColumnInfo columnInfo) throws SQLException {
        super(row, null, columnInfo);
        DBDataType dataType = columnInfo.getDataType();
        Object userValue = dataType.getValueFromResultSet(resultSet, columnInfo.getColumnIndex() + 1);
        setUserValue(userValue);
    }

    @Override
    public ResultSetDataModelRow getRow() {
        return (ResultSetDataModelRow) super.getRow();
    }
}
