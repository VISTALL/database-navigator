package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.browser.ui.HtmlToolTipBuilder;
import com.dci.intellij.dbn.common.content.loader.DynamicContentLoader;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBIndex;
import com.dci.intellij.dbn.object.DBTable;
import com.dci.intellij.dbn.object.common.DBObjectRelationType;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.dci.intellij.dbn.object.common.DBSchemaObjectImpl;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import com.dci.intellij.dbn.object.common.list.DBObjectNavigationList;
import com.dci.intellij.dbn.object.common.list.DBObjectNavigationListImpl;
import com.dci.intellij.dbn.object.common.list.loader.DBObjectListFromRelationListLoader;
import com.dci.intellij.dbn.object.common.property.DBObjectProperty;
import com.dci.intellij.dbn.object.common.status.DBObjectStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBIndexImpl extends DBSchemaObjectImpl implements DBIndex {
    private DBObjectList<DBColumn> columns;
    private DBTable table;
    private boolean isUnique;

    public DBIndexImpl(DBTable table, ResultSet resultSet) throws SQLException {
        super(table, DBContentType.NONE, resultSet);
        this.table = table;
        name = resultSet.getString("INDEX_NAME");
        isUnique = resultSet.getString("IS_UNIQUE").equals("Y");

        createLists();
    }
                                                    
    private void createLists() {
        DBTable table = getTable();
        if (table != null) {
            columns = getChildObjects().createSubcontentObjectList(DBObjectType.COLUMN, this, COLUMNS_LOADER, table, DBObjectRelationType.INDEX_COLUMN, false);
        }
    }

    @Override
    public void updateProperties() {
        getProperties().set(DBObjectProperty.SCHEMA_OBJECT);
    }

    public void updateStatuses(ResultSet resultSet) throws SQLException {
        boolean valid = resultSet.getString("IS_VALID").equals("Y");
        getStatus().set(DBObjectStatus.VALID, valid);
    }

    public DBObjectType getObjectType() {
        return DBObjectType.INDEX;
    }

    public DBTable getTable() {
        table = (DBTable) table.getUndisposedElement();
        return table;
    }

    public List<DBColumn> getColumns() {
        return columns.getObjects();
    }

    public boolean isUnique() {
        return isUnique;
    }

    protected List<DBObjectNavigationList> createNavigationLists() {
        List<DBObjectNavigationList> objectNavigationLists = super.createNavigationLists();

        if (columns.size() > 0) {
            objectNavigationLists.add(new DBObjectNavigationListImpl<DBColumn>("Columns", columns.getObjects()));
        }
        objectNavigationLists.add(new DBObjectNavigationListImpl<DBTable>("Table", getTable()));

        return objectNavigationLists;
    }

    public void buildToolTip(HtmlToolTipBuilder ttb) {
        ttb.append(true, getObjectType().getName(), true);
        ttb.createEmptyRow();
        super.buildToolTip(ttb);
    }

    /********************************************************
     *                   TreeeElement                       *
     * ******************************************************/

    public boolean isLeafTreeElement() {
        return true;
    }

    @NotNull
    public List<BrowserTreeNode> buildAllPossibleTreeChildren() {
        return BrowserTreeNode.EMPTY_LIST;
    }

    /**
     * ******************************************************
     * Loaders                       *
     * *******************************************************
     */
    private static final DynamicContentLoader COLUMNS_LOADER = new DBObjectListFromRelationListLoader();
}
