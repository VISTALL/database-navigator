package com.dci.intellij.dbn.data.ui.table.model;

import com.dci.intellij.dbn.common.ui.table.model.BasicDataModelHeader;
import com.dci.intellij.dbn.common.ui.table.model.ColumnInfo;
import com.dci.intellij.dbn.common.ui.table.model.DataModelHeader;
import com.dci.intellij.dbn.object.common.DBObjectBundle;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetDataModelHeader extends BasicDataModelHeader implements DataModelHeader {

    public ResultSetDataModelHeader(DBObjectBundle objectBundle, ResultSet resultSet) throws SQLException {
        super();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            ColumnInfo columnInfo = new ResultSetColumnInfo(objectBundle, resultSet, i);
            getColumnInfos().add(columnInfo);
        }
    }


}
