package com.dci.intellij.dbn.editor.data.model;

import com.dci.intellij.dbn.data.model.ColumnInfo;
import com.dci.intellij.dbn.data.model.DataModelHeader;
import com.dci.intellij.dbn.data.model.basic.BasicDataModelHeader;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DatasetEditorModelHeader extends BasicDataModelHeader implements DataModelHeader {
    public DatasetEditorModelHeader(DBDataset dataset, ResultSet resultSet) throws SQLException {
        super();
        List<ColumnInfo> columnInfos = getColumnInfos();
        if (resultSet == null) {
            List<DBColumn> columns = new ArrayList<DBColumn>(dataset.getColumns());
            Collections.sort(columns, COLUMN_POSITION_COMPARATOR);
            int index = 0;
            for (DBColumn column : columns) {
                if (!column.isHidden()) {
                    ColumnInfo columnInfo = new DatasetEditorColumnInfo(column, index);
                    columnInfos.add(columnInfo);
                    index++;
                }
            }
        } else {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                String name = metaData.getColumnName(i+1);
                DBColumn column = dataset.getColumn(name);
                ColumnInfo columnInfo = new DatasetEditorColumnInfo(column, i);
                columnInfos.add(columnInfo);
            }
        }
    }

    public int indexOfColumn(DBColumn column) {
        for (int i=0; i<getColumnCount(); i++) {
            ColumnInfo info = getColumnInfo(i);
            DatasetEditorColumnInfo columnInfo = (DatasetEditorColumnInfo) info;
            if (columnInfo.getColumn().equals(column)) return i;
        }
        return -1;
    }

    private static final Comparator<DBColumn> COLUMN_POSITION_COMPARATOR = new Comparator<DBColumn>() {
        public int compare(DBColumn column1, DBColumn column2) {
            return column1.getPosition()-column2.getPosition();
        }
    };
}
