package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.object.DBProgram;
import com.dci.intellij.dbn.object.DBType;
import com.dci.intellij.dbn.object.DBTypeProcedure;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.dci.intellij.dbn.object.common.property.DBObjectProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTypeProcedureImpl extends DBProcedureImpl implements DBTypeProcedure {
    private int overload;

    public DBTypeProcedureImpl(DBType type, ResultSet resultSet) throws SQLException {
        super(type, resultSet);
        overload = resultSet.getInt("OVERLOAD");
    }

    @Override
    public void updateStatuses(ResultSet resultSet) throws SQLException {}

    @Override
    public void updateProperties() {
        getProperties().set(DBObjectProperty.NAVIGABLE);
    }

    public DBType getType() {
        return (DBType) getParentObject();
    }

    @Override
    public DBProgram getProgram() {
        return getType();
    }    

    public int getOverload() {
        return overload;
    }

    public boolean isEmbedded() {
        return true;
    }

    @Override
    public String getPresentableTextDetails() {
        return getOverload() > 0 ? " - " + getOverload() : "";
    }

    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.TYPE_PROCEDURE;
    }

    public void executeUpdateDDL(DBContentType contentType, String oldCode, String newCode) throws SQLException {}
}