package com.dci.intellij.dbn.execution.method;

import com.dci.intellij.dbn.common.options.PersistentConfiguration;
import com.dci.intellij.dbn.connection.ConnectionBundle;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.DBProgram;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

public class MethodIdentifier implements PersistentConfiguration {
    private String connectionId;
    private String schemaName;
    private String programName;
    private String methodName;
    private DBObjectType programObjectType;
    private DBObjectType methodObjectType;
    private int overload;

    public MethodIdentifier(DBMethod method) {
        connectionId = method.getConnectionHandler().getId();
        schemaName = method.getSchema().getName();
        DBProgram program = method.getProgram();
        if (program != null) {
            programName = program.getName();
            programObjectType = program.getObjectType();
        }
        methodName = method.getName();
        methodObjectType = method.getObjectType();
        overload = method.getOverload();
    }

    public MethodIdentifier() {
    }

    public DBMethod lookupMethod() {
        ConnectionHandler connectionHandler = ConnectionBundle.getConnectionHandler(connectionId);
        if (connectionHandler == null) return null;

        DBSchema schema = connectionHandler.getObjectBundle().getSchema(schemaName);
        if (schema == null) return null;

        DBMethod method;
        if (programObjectType != null) {
            DBProgram program = schema.getProgram(programName);
            if (program == null || program.getObjectType() != programObjectType) return null;

            method = program.getMethod(methodName, overload);
        } else {
            method = schema.getMethod(methodName, methodObjectType.getName());
        }

        return method != null && method.getObjectType() == methodObjectType ? method : null;
    }

    public boolean matches(DBMethod method) {
        ConnectionHandler connectionHandler = method.getConnectionHandler();
        if (!connectionId.equals(connectionHandler.getId())) {
            return false;
        }
        DBSchema schema = method.getSchema();
        if (!schema.getName().equals(schemaName)) {
            return false;
        }

        DBProgram program = method.getProgram();
        if (program == null) {
            if (programObjectType != null || programName != null) {
                return false;
            }
        } else {
            if (!program.getName().equalsIgnoreCase(programName) ||
                    program.getObjectType() != programObjectType) {
                return false;
            }
        }

        return
            method.getName().equalsIgnoreCase(methodName) &&
            method.getObjectType() == methodObjectType &&
            method.getOverload() == overload;

    }

    public String getQualifiedName() {
        return programName == null ?
            schemaName + "." + methodName :
            schemaName + "." + programName + "." + methodName;
    }

    public String getQualifiedMethodName() {
        return programName == null ? methodName : programName + "." + methodName;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getProgramName() {
        return programName;
    }

    public String getMethodName() {
        return methodName;
    }

    public DBObjectType getProgramObjectType() {
        return programObjectType;
    }

    public DBObjectType getMethodObjectType() {
        return methodObjectType;
    }

    public int getOverload() {
        return overload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodIdentifier)) return false;

        MethodIdentifier that = (MethodIdentifier) o;

        if (overload != that.overload) return false;
        if (!connectionId.equals(that.connectionId)) return false;
        if (!methodName.equals(that.methodName)) return false;
        if (methodObjectType != that.methodObjectType) return false;
        if (!schemaName.equals(that.schemaName)) return false;
        if (programName != null ? !programName.equals(that.programName) : that.programName != null) return false;
        if (programObjectType != null ? programObjectType != that.programObjectType : that.programObjectType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = connectionId.hashCode();
        result = 31 * result + schemaName.hashCode();
        result = 31 * result + methodName.hashCode();
        result = 31 * result + methodObjectType.hashCode();
        result = 31 * result + (programName != null ? programName.hashCode() : 0);
        result = 31 * result + (programObjectType != null ? programObjectType.hashCode() : 0);
        result = 31 * result + overload;
        return result;
    }

    @Override
    public String toString() {
        return methodObjectType.getName() + " " + getQualifiedName();
    }

    /*********************************************************
     *                   JDOMExternalizable                  *
     *********************************************************/
    public void readConfiguration(Element element) throws InvalidDataException {
        connectionId = element.getAttributeValue("connection-id");
        schemaName = element.getAttributeValue("schema-name");

        programName = element.getAttributeValue("program-name");
        String programTypeName = element.getAttributeValue("program-type");
        if (programTypeName != null) {
            programObjectType = DBObjectType.getObjectType(programTypeName);
        }

        methodName = element.getAttributeValue("method-name");
        methodObjectType = DBObjectType.getObjectType(element.getAttributeValue("method-type"));

        String overload = element.getAttributeValue("method-overload");
        this.overload = Integer.parseInt(overload == null ? "0" : overload);
    }

    public void writeConfiguration(Element element) throws WriteExternalException {
        element.setAttribute("connection-id", connectionId);
        element.setAttribute("schema-name", schemaName);

        if (programName != null && programObjectType != null) {
            element.setAttribute("program-type", programObjectType.getName());
            element.setAttribute("program-name", programName);

        }
        element.setAttribute("method-type", methodObjectType.getName());
        element.setAttribute("method-name", methodName);

        element.setAttribute("method-overload", Integer.toString(overload));
    }
}
