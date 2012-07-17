package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.DatabaseBrowserUtils;
import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.loader.DynamicContentLoader;
import com.dci.intellij.dbn.common.content.loader.DynamicContentResultSetLoader;
import com.dci.intellij.dbn.common.content.loader.DynamicSubcontentCompoundLoader;
import com.dci.intellij.dbn.common.content.loader.DynamicSubcontentLoader;
import com.dci.intellij.dbn.database.DatabaseMetadataInterface;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.object.DBArgument;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.DBProgram;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.common.DBSchemaObjectImpl;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.dci.intellij.dbn.object.common.property.DBObjectProperty;
import com.dci.intellij.dbn.object.common.status.DBObjectStatus;
import com.dci.intellij.dbn.object.common.status.DBObjectStatusHolder;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class DBMethodImpl extends DBSchemaObjectImpl implements DBMethod {
    protected DBObjectList<DBArgument> arguments;
    private DBSchema schema;
    boolean isDeterministic;

    public DBMethodImpl(DBSchemaObject parent, DBContentType contentType, ResultSet resultSet) throws SQLException {
        super(parent, contentType, resultSet);
        schema = parent.getSchema();
        initLists();
    }

    public DBMethodImpl(DBSchema schema, DBContentType contentType, ResultSet resultSet) throws SQLException {
        super(schema, contentType, resultSet);
        isDeterministic = resultSet.getString("IS_DETERMINISTIC").equals("Y");
        this.schema = schema;
        initLists();
    }

    @Override
    public void updateProperties() {
        super.updateProperties();
        getProperties().set(DBObjectProperty.COMPILABLE);
    }

    @Override
    public void updateStatuses(ResultSet resultSet) throws SQLException {
        boolean isValid = "Y".equals(resultSet.getString("IS_VALID"));
        boolean isDebug = "Y".equals(resultSet.getString("IS_DEBUG"));
        DBObjectStatusHolder objectStatus = getStatus();
        objectStatus.set(DBObjectStatus.VALID, isValid);
        objectStatus.set(DBObjectStatus.DEBUG, isDebug);
    }

    @Override
    public boolean isEditable(DBContentType contentType) {
        return getContentType() == DBContentType.CODE && contentType == DBContentType.CODE;
    }    

    protected void initLists() {
        DBObjectListContainer container = getChildObjects();
        arguments = container.createSubcontentObjectList(DBObjectType.ARGUMENT, this, ARGUMENTS_LOADER, getSchema(), true, true);
    }

    public boolean isDeterministic() {
        return isDeterministic;
    }

    public boolean hasDeclaredArguments() {
        for (DBArgument argument : getArguments()) {
            if (argument.getDataType().isDeclared()) {
                return true;
            }
        }
        return false; 
    }

    public List<DBArgument> getArguments() {
        return arguments.getObjects();
    }

    public DBArgument getArgument(String name) {
        return (DBArgument) getObjectByName(getArguments(), name);
    }

    public int getOverload() {
        return 0;
    }

    public boolean isEmbedded() {
        return false;
    }

    public DBSchema getSchema() {
        schema = (DBSchema) schema.getUndisposedElement();
        return schema;
    }

    @Override
    public void reload() {
        arguments.reload(true);
    }

    @Override
    public int compareTo(Object o) {
        int result = super.compareTo(o);
        if (result == 0) {
            DBMethod method = (DBMethod) o;
            return getOverload() - method.getOverload();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            DBMethod method = (DBMethod) obj;
            return method.getOverload() == getOverload();
        }
        return false;
    }

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/
    @NotNull
    public List<BrowserTreeNode> buildAllPossibleTreeChildren() {
        return DatabaseBrowserUtils.createList(arguments);
    }

    /*********************************************************
     *                         Loaders                       *
     *********************************************************/

    private static final DynamicContentLoader<DBArgument> ARGUMENTS_ALTERNATIVE_LOADER = new DynamicContentResultSetLoader<DBArgument>() {
        public ResultSet createResultSet(DynamicContent<DBArgument> dynamicContent, Connection connection) throws SQLException {
            DatabaseMetadataInterface metadataInterface = dynamicContent.getConnectionHandler().getInterfaceProvider().getMetadataInterface();
            DBMethod method = (DBMethod) dynamicContent.getParent();
            String ownerName = method.getSchema().getName();
            String methodName = method.getName();
            String methodType = method.getMethodType();
            String overload = Integer.toString(method.getOverload());
            DBProgram program = method.getProgram();
            if (program == null) {
                return metadataInterface.loadMethodArguments(ownerName, methodName, methodType, overload, connection);
            } else {
                return metadataInterface.loadProgramMethodArguments(ownerName, program.getName(), methodName, overload, connection);
            }
        }

        public DBArgument createElement(DynamicContent<DBArgument> dynamicContent, ResultSet resultSet, LoaderCache loaderCache) throws SQLException {
            DBMethod method = (DBMethod) dynamicContent.getParent();
            return new DBArgumentImpl(method, resultSet);
        }
    };

    private static final DynamicSubcontentLoader ARGUMENTS_LOADER = new DynamicSubcontentCompoundLoader<DBArgument>(true) {
        public DynamicContentLoader<DBArgument> getAlternativeLoader() {
            return ARGUMENTS_ALTERNATIVE_LOADER;
        }

        public boolean match(DBArgument argument, DynamicContent dynamicContent) {
            DBMethod method = (DBMethod) dynamicContent.getParent();
            return argument.getMethod().equals(method) && argument.getOverload() == method.getOverload();
        }
    };
}
