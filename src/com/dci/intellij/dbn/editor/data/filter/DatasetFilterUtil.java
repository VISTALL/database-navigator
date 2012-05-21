package com.dci.intellij.dbn.editor.data.filter;

import com.dci.intellij.dbn.common.ui.table.SortingState;
import com.dci.intellij.dbn.database.DatabaseCompatibilityInterface;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.sql.SQLLanguage;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.DBTable;

import java.util.List;

public class DatasetFilterUtil {

    public static void addOrderByClause(DBDataset dataset, StringBuilder buffer, SortingState sortingState) {
        if (dataset instanceof DBTable) {
            DBTable table = (DBTable) dataset;
            if (sortingState.isValid()) {
                buffer.append(" order by ");
                buffer.append(sortingState.getColumnName());
                buffer.append(" ");
                buffer.append(sortingState.getDirectionAsString());
            } else {
                List<DBColumn> primaryKeyColumns = table.getPrimaryKeyColumns();
                if (primaryKeyColumns.size() > 0) {
                    buffer.append(" order by ");
                    for (DBColumn column : primaryKeyColumns) {
                        buffer.append(column.getName());
                        if (primaryKeyColumns.get(primaryKeyColumns.size()-1) == column ) {
                            buffer.append(" asc");
                        } else {
                            buffer.append(", ");
                        }
                    }
                }
            }
        }
    }

    public static void addForUpdateClause(DBDataset dataset, StringBuilder buffer) {
        if (dataset instanceof DBTable && dataset.hasLobColumns()) {
            buffer.append(" for update");
        }
    }

    public static void createSelectStatement(DBDataset dataset, StringBuilder buffer) {
        buffer.append("select ");
        int index = 0;
        for (DBColumn column : dataset.getColumns()) {
            if (index > 0) {
                buffer.append(", ");
            }
            buffer.append(column.getName());
            index++;
        }
        buffer.append(" from ");
        buffer.append(dataset.getSchema().getName());
        buffer.append(".");

        DBLanguageDialect languageDialect = dataset.getLanguageDialect(SQLLanguage.INSTANCE);
        if (languageDialect.isReservedWord(dataset.getName())) {
            char quotesChar = DatabaseCompatibilityInterface.getInstance(dataset).getIdentifierQuotes();
            buffer.append(quotesChar);
            buffer.append(dataset.getName());
            buffer.append(quotesChar);
        } else {
            buffer.append(dataset.getName());
        }

    }

    public static void createSimpleSelectStatement(DBDataset dataset, StringBuilder buffer) {
        buffer.append("select a.* from ");

        buffer.append(dataset.getSchema().getName());
        buffer.append(".");

        DBLanguageDialect languageDialect = dataset.getLanguageDialect(SQLLanguage.INSTANCE);
        if (languageDialect.isReservedWord(dataset.getName())) {
            char quotesChar = DatabaseCompatibilityInterface.getInstance(dataset).getIdentifierQuotes();
            buffer.append(quotesChar);
            buffer.append(dataset.getName());
            buffer.append(quotesChar);
        } else {
            buffer.append(dataset.getName());
        }
        buffer.append(" a");

    }
}
