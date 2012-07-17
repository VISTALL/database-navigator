package com.dci.intellij.dbn.execution.method.browser;

import com.dci.intellij.dbn.common.options.PersistentConfiguration;
import com.dci.intellij.dbn.connection.ConnectionBundle;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.execution.method.MethodIdentifier;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import org.jdom.Element;

import java.util.Map;
import java.util.Set;

public class MethodBrowserSettings implements PersistentConfiguration {
    private String connectionId;
    private String schemaName;
    private MethodIdentifier methodIdentifier = new MethodIdentifier();
    private Map<DBObjectType, Boolean> objectVisibility = new THashMap<DBObjectType, Boolean>();

    private transient ConnectionHandler connectionHandler;
    private transient DBSchema schema;
    private transient DBMethod method;

    public MethodBrowserSettings() {
        objectVisibility.put(DBObjectType.FUNCTION, true);
        objectVisibility.put(DBObjectType.PROCEDURE, true);
    }

    public ConnectionHandler getConnectionHandler() {
        if (connectionHandler == null) {
            connectionHandler = ConnectionBundle.getConnectionHandler(connectionId);
        }
        return connectionHandler;
    }

    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.connectionId = connectionHandler == null ? null : connectionHandler.getId();
    }

    public synchronized DBSchema getSchema() {
        if (schema == null) {
            schema = getConnectionHandler() == null || schemaName == null ? null : getConnectionHandler().getObjectBundle().getSchema(schemaName);
        }
        return schema;
    }

    public Set<DBObjectType> getVisibleObjectTypes() {
        Set<DBObjectType> objectTypes = new THashSet<DBObjectType>();
        for (DBObjectType objectType : objectVisibility.keySet()) {
            if (objectVisibility.get(objectType)) {
                objectTypes.add(objectType);
            }
        }
        return objectTypes;
    }

    public boolean getObjectVisibility(DBObjectType objectType) {
        return objectVisibility.get(objectType);
    }

    public boolean setObjectVisibility(DBObjectType objectType, boolean visibility) {
        if (getObjectVisibility(objectType) != visibility) {
            objectVisibility.put(objectType, visibility);
            return true;
        }
        return false;        
    }

    public void setSchema(DBSchema schema) {
        this.schema = schema;
        this.schemaName = schema == null ? null : schema.getName();
    }

    public DBMethod getMethod() {
        if (method == null) {
            method = methodIdentifier.lookupMethod();
        }
        return method;
    }

    public void setMethod(DBMethod method) {
        this.method = method;
        methodIdentifier = new MethodIdentifier(method);
    }

    public void readConfiguration(Element element) throws InvalidDataException {
        connectionId = element.getAttributeValue("connection-id");
        schemaName = element.getAttributeValue("schema");

        Element methodElement = element.getChild("selected-method");
        if (methodElement != null) {
            methodIdentifier.readConfiguration(methodElement);
        }
    }

    public void writeConfiguration(Element element) throws WriteExternalException {
        ConnectionHandler connectionHandler = getConnectionHandler();
        DBSchema schema = getSchema();
        DBMethod method = getMethod();
        if (connectionHandler != null) element.setAttribute("connection-id", connectionHandler.getId());
        if (schema != null) element.setAttribute("schema", schema.getName());
        if (method != null) {
            MethodIdentifier methodIdentifier = new MethodIdentifier(method);
            Element methodElement = new Element("selected-method");
            methodIdentifier.writeConfiguration(methodElement);
            element.addContent(methodElement);
        }

    }
}
