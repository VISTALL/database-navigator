package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.browser.ui.HtmlToolTipBuilder;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.DBSequence;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.dci.intellij.dbn.object.common.DBSchemaObjectImpl;
import com.dci.intellij.dbn.object.common.property.DBObjectProperty;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBSequenceImpl extends DBSchemaObjectImpl implements DBSequence {
    public DBSequenceImpl(DBSchema schema, ResultSet resultSet) throws SQLException {
        super(schema, DBContentType.NONE, null);
        name = resultSet.getString("SEQUENCE_NAME");
    }

    @Override
    public void updateProperties() {
        getProperties().set(DBObjectProperty.REFERENCEABLE);
        getProperties().set(DBObjectProperty.SCHEMA_OBJECT);
    }

    public DBObjectType getObjectType() {
        return DBObjectType.SEQUENCE;
    }

    public void buildToolTip(HtmlToolTipBuilder ttb) {
        ttb.append(true, getObjectType().getName(), true);
        ttb.createEmptyRow();
        super.buildToolTip(ttb);
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
