package com.dci.intellij.dbn.execution.method;

import com.dci.intellij.dbn.common.options.PersistentConfiguration;
import com.dci.intellij.dbn.common.options.setting.SettingsUtil;
import com.dci.intellij.dbn.common.util.CommonUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.execution.method.result.MethodExecutionResult;
import com.dci.intellij.dbn.execution.method.result.ui.MethodExecutionResultForm;
import com.dci.intellij.dbn.object.DBArgument;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.DBTypeAttribute;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodExecutionInput implements Disposable, PersistentConfiguration, Comparable<MethodExecutionInput> {
    private MethodIdentifier methodIdentifier;
    private Map<String, String> valuesMap = new HashMap<String, String>();
    private String executionSchemaName;
    private boolean usePoolConnection = true;
    private boolean commitAfterExecution = true;
    private boolean isExecuting = false;


    private transient MethodExecutionResult executionResult;
    private transient List<ArgumentValue> argumentValues = new ArrayList<ArgumentValue>();
    private transient ConnectionHandler connectionHandler;
    private transient DBSchema executionSchema;
    private transient DBMethod method;

    private transient boolean executionCancelled;

    public MethodExecutionInput() {
        methodIdentifier = new MethodIdentifier();
    }

    public MethodExecutionInput(DBMethod method) {
        this.method = method;
        connectionHandler = method.getConnectionHandler();
        methodIdentifier = new MethodIdentifier(method);
        executionSchema = method.getSchema();
        executionSchemaName = executionSchema.getName();
    }

    public void initExecutionResult(boolean debug) {
        MethodExecutionResultForm resultPanel = executionResult == null ? null : executionResult.getResultPanel();
        executionResult = new MethodExecutionResult(this, resultPanel, debug);
    }

    public synchronized DBMethod getMethod() {
        if (method == null) {
            method = methodIdentifier.lookupMethod();
            ConnectionHandler connectionHandler = getConnectionHandler();
            if (method == null && connectionHandler != null && connectionHandler.isValid(true)) {
                return null;
            }
        }
        if (method != null) {
            method = (DBMethod) method.getUndisposedElement();
        }
        return method;
    }

    public MethodIdentifier getMethodIdentifier() {
        return methodIdentifier;
    }

    public synchronized ConnectionHandler getConnectionHandler() {
        if (connectionHandler == null) {

            connectionHandler = ConnectionManager.findConnectionHandler(methodIdentifier.getConnectionId());
        }
        return connectionHandler;
    }

    public synchronized DBSchema getExecutionSchema() {
        if (executionSchema == null) {
            executionSchema = getConnectionHandler().getObjectBundle().getSchema(executionSchemaName);
            if (executionSchema == null) {
                DBMethod method = getMethod();
                executionSchema = method == null ? null : method.getSchema();
            }
        }
        return executionSchema;
    }

    public boolean isObsolete() {
        return getConnectionHandler() == null || getMethod() == null;
    }

    public boolean isExecutionCancelled() {
        return executionCancelled;
    }

    public void setExecutionCancelled(boolean executionCancelled) {
        this.executionCancelled = executionCancelled;
    }

    public void setExecutionSchema(DBSchema schema) {
        executionSchema = schema;
        executionSchemaName = executionSchema.getName();
    }

    public void setInputValue(DBArgument argument, DBTypeAttribute typeAttribute, String value) {
        ArgumentValue argumentValue = getArgumentValue(argument, typeAttribute);
        argumentValue.setValue(value);
    }

    public void setInputValue(DBArgument argument, String value) {
        ArgumentValue argumentValue = getArgumentValue(argument);
        argumentValue.setValue(value);
    }

    public String getInputValue(DBArgument argument) {
        ArgumentValue argumentValue = getArgumentValue(argument);
        return (String) argumentValue.getValue();
    }

    public String getInputValue(DBArgument argument, DBTypeAttribute typeAttribute) {
        ArgumentValue argumentValue = getArgumentValue(argument, typeAttribute);
        return (String) argumentValue.getValue();
    }

    public List<ArgumentValue> getArgumentValues() {
        return argumentValues;
    }

    private ArgumentValue getArgumentValue(DBArgument argument) {
        for (ArgumentValue argumentValue : argumentValues) {
            if (argumentValue.getArgument().equals(argument)) {
                return argumentValue;
            }
        }
        ArgumentValue argumentValue = new ArgumentValue(argument, null);
        argumentValue.setValue(valuesMap.get(argumentValue.getName()));
        argumentValues.add(argumentValue);
        return argumentValue;
    }

    private ArgumentValue getArgumentValue(DBArgument argument, DBTypeAttribute attribute) {
        for (ArgumentValue argumentValue : argumentValues) {
            if (argumentValue.getArgument().equals(argument) &&
                    argumentValue.getAttribute().equals(attribute)) {
                return argumentValue;
            }
        }

        ArgumentValue argumentValue = new ArgumentValue(argument, attribute, null);
        argumentValue.setValue(valuesMap.get(argumentValue.getName()));
        argumentValues.add(argumentValue);
        return argumentValue;
    }

    public MethodExecutionResult getExecutionResult() {
        return executionResult;
    }

    public void setExecutionResult(MethodExecutionResult executionResult) {
        this.executionResult = executionResult;
    }

    public boolean isUsePoolConnection() {
        return usePoolConnection;
    }

    public void setUsePoolConnection(boolean usePoolConnection) {
        this.usePoolConnection = usePoolConnection;
    }

    public boolean isCommitAfterExecution() {
        return commitAfterExecution;
    }

    public void setCommitAfterExecution(boolean commitAfterExecution) {
        this.commitAfterExecution = commitAfterExecution;
    }

    public Project getProject() {
        return getMethod().getProject();
    }

    public void setExecuting(boolean executing) {
        isExecuting = executing;
    }

    public boolean isExecuting() {
        return isExecuting;
    }

    public void dispose() {
        executionResult = null;
        valuesMap.clear();
        argumentValues.clear();
        connectionHandler = null;
        executionSchema = null;
        method = null;
        methodIdentifier = null;
    }

    /*********************************************************
     *                   JDOMExternalizable                  *
     *********************************************************/
    public void readConfiguration(Element element) throws InvalidDataException {
        methodIdentifier.readConfiguration(element);
        executionSchemaName = element.getAttributeValue("execution-schema");
        usePoolConnection = SettingsUtil.getBooleanAttribute(element, "use-pool-connection", true);
        commitAfterExecution = SettingsUtil.getBooleanAttribute(element, "commit-after-execution", true);
        Element argumentsElement = element.getChild("argument-list");
        for (Object object : argumentsElement.getChildren()) {
            Element argumentElement = (Element) object;
            String name = argumentElement.getAttributeValue("name");
            String value = CommonUtil.nullIfEmpty(argumentElement.getAttributeValue("value"));
            valuesMap.put(name, value);
        }
    }

    public void writeConfiguration(Element element) throws WriteExternalException {
        methodIdentifier.writeConfiguration(element);
        element.setAttribute("execution-schema", (String) CommonUtil.nvl(executionSchemaName, ""));
        SettingsUtil.setBooleanAttribute(element, "use-pool-connection", usePoolConnection);
        SettingsUtil.setBooleanAttribute(element, "commit-after-execution", commitAfterExecution);

        Element argumentsElement = new Element("argument-list");
        element.addContent(argumentsElement);

        if (argumentValues.size() > 0) {
            for (ArgumentValue argumentValue : argumentValues) {
                Element argumentElement = new Element("argument");
                argumentElement.setAttribute("name", argumentValue.getName());
                argumentElement.setAttribute("value", (String) CommonUtil.nvl(argumentValue.getValue(), ""));
                argumentsElement.addContent(argumentElement);
            }
        } else {
            for (String name : valuesMap.keySet()) {
                Element argumentElement = new Element("argument");
                argumentElement.setAttribute("name", name);
                argumentElement.setAttribute("value", (String) CommonUtil.nvl(valuesMap.get(name), ""));
                argumentsElement.addContent(argumentElement);
            }
        }
    }

    public int compareTo(MethodExecutionInput executionInput) {
        MethodIdentifier localMethodIdentifier = getMethodIdentifier();
        MethodIdentifier remoteMethodIdentifier = executionInput.getMethodIdentifier();

        int result = localMethodIdentifier.getConnectionId().compareTo(remoteMethodIdentifier.getConnectionId());
        if (result != 0) return result;


        result = localMethodIdentifier.getSchemaName().compareTo(remoteMethodIdentifier.getSchemaName());
        if (result != 0) return result;

        String localProgramName = localMethodIdentifier.getProgramName();
        String remoteProgramName = remoteMethodIdentifier.getProgramName();
        result =
            localProgramName == null && remoteProgramName == null ? 0 :
            localProgramName == null ? -1 :
            remoteProgramName == null ? 1 :
            localProgramName.compareTo(remoteProgramName);
        if (result != 0) return result;

        result = localMethodIdentifier.getMethodName().compareTo(remoteMethodIdentifier.getMethodName());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MethodExecutionInput) {
            MethodExecutionInput executionInput = (MethodExecutionInput) obj;
            return methodIdentifier.equals(executionInput.getMethodIdentifier());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getMethodIdentifier().hashCode();
    }

    public MethodExecutionInput clone() {
        MethodExecutionInput executionInput = new MethodExecutionInput();
        executionInput.methodIdentifier = methodIdentifier;
        executionInput.executionSchemaName = executionSchemaName;
        executionInput.usePoolConnection = usePoolConnection;
        executionInput.commitAfterExecution = commitAfterExecution;
        executionInput.valuesMap = new HashMap<String, String>(valuesMap);
        return executionInput;
    }
}
