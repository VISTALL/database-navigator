package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.object.DBNestedTable;
import com.dci.intellij.dbn.object.DBNestedTableColumn;
import com.dci.intellij.dbn.object.common.DBObjectImpl;
import com.dci.intellij.dbn.object.common.DBObjectType;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBNestedTableColumnImpl extends DBObjectImpl implements DBNestedTableColumn {

    private DBNestedTable nestedTable;

    public DBNestedTableColumnImpl(DBNestedTable parent, ResultSet resultSet) throws SQLException {
        super(parent);
        this.nestedTable = parent;
        
        // todo !!!
    }

    public DBObjectType getObjectType() {
        return DBObjectType.NESTED_TABLE_COLUMN;
    }

    public DBNestedTable getNestedTable() {
        nestedTable = (DBNestedTable) nestedTable.getUndisposedElement();
        return nestedTable;
    }

    @Override
    public void dispose() {
        super.dispose();
        nestedTable = null;
    }

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/

    public boolean isLeafTreeElement() {
        return true;
    }

    @NotNull
    public List<BrowserTreeElement> buildAllPossibleTreeChildren() {
        return BrowserTreeElement.EMPTY_LIST;
    }
}
