package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.ui.HtmlToolTipBuilder;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.ddl.DDLFileManager;
import com.dci.intellij.dbn.ddl.DDLFileType;
import com.dci.intellij.dbn.ddl.DDLFileTypeId;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.object.DBArgument;
import com.dci.intellij.dbn.object.DBFunction;
import com.dci.intellij.dbn.object.DBProgram;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.common.loader.DBObjectTimestampLoader;
import com.dci.intellij.dbn.object.common.loader.DBSourceCodeLoader;
import com.dci.intellij.dbn.object.common.status.DBObjectStatus;
import com.dci.intellij.dbn.object.common.status.DBObjectStatusHolder;

import javax.swing.Icon;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBFunctionImpl extends DBMethodImpl implements DBFunction {
    DBFunctionImpl(DBSchemaObject parent, ResultSet resultSet) throws SQLException {
        // type functions are not editable independently
        super(parent, DBContentType.NONE, resultSet);
        assert this.getClass() != DBFunctionImpl.class;
        name = resultSet.getString("FUNCTION_NAME");
    }

    public DBFunctionImpl(DBSchema schema, ResultSet resultSet) throws SQLException {
        super(schema, DBContentType.CODE, resultSet);
        name = resultSet.getString("FUNCTION_NAME");
    }



    public DBArgument getReturnArgument() {
        for (DBArgument argument : getArguments()) {
            if (argument.getPosition() == 1) {
                return argument;
            }
        }
        return null;
    }

    public DBObjectType getObjectType() {
        return DBObjectType.FUNCTION;
    }

    public Icon getIcon() {
        if (getContentType() == DBContentType.CODE) {
            DBObjectStatusHolder objectStatus = getStatus();
            if (objectStatus.is(DBObjectStatus.VALID)) {
                if (objectStatus.is(DBObjectStatus.DEBUG)){
                    return Icons.DBOBJECT_FUNCTION_DEBUG;
                }
            } else {
                return Icons.DBOBJECT_FUNCTION_ERR;
            }

        }
        return Icons.DBOBJECT_FUNCTION;
    }

    public Icon getOriginalIcon() {
        return Icons.DBOBJECT_FUNCTION;
    }

    public DBProgram getProgram() {
        return null;
    }

    public String getMethodType() {
        return "FUNCTION";
    }

    public void buildToolTip(HtmlToolTipBuilder ttb) {
        ttb.append(true, getObjectType().getName(), true);
        ttb.createEmptyRow();
        super.buildToolTip(ttb);
    }

    /*********************************************************
     *                         Loaders                       *
     *********************************************************/

    private class SourceCodeLoader extends DBSourceCodeLoader {
        protected SourceCodeLoader(DBObject object) {
            super(object, false);
        }

        public ResultSet loadSourceCode(Connection connection) throws SQLException {
            return getConnectionHandler().getInterfaceProvider().getMetadataInterface().loadObjectSourceCode(
                   getSchema().getName(),
                   getName(),
                   "FUNCTION",
                   connection);
        }
    }

    private static DBObjectTimestampLoader TIMESTAMP_LOADER = new DBObjectTimestampLoader("FUNCTION") {};

    /*********************************************************
     *              DBEditableCodeSchemaObject               *
     *********************************************************/

    public String loadCodeFromDatabase(DBContentType contentType) throws SQLException {
        SourceCodeLoader loader = new SourceCodeLoader(this);
        String body = loader.load();
        String header = getConnectionHandler().getInterfaceProvider().getDDLInterface().createMethodEditorHeader(this);
        return header + body;
    }

    public String getCodeParseRootId(DBContentType contentType) {
        return getParentObject() instanceof DBSchema && contentType == DBContentType.CODE ? "function_declaration" : null;
    }

    public DDLFileType getDDLFileType(DBContentType contentType) {
        return DDLFileManager.getInstance(getProject()).getDDLFileType(DDLFileTypeId.FUNCTION);
    }

    public DDLFileType[] getDDLFileTypes() {
        return new DDLFileType[]{getDDLFileType(null)};
    }

    public DBObjectTimestampLoader getTimestampLoader(DBContentType contentType) {
        return TIMESTAMP_LOADER;
    }

}

