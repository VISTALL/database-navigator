package com.dci.intellij.dbn.database.common.execution;

import com.dci.intellij.dbn.common.locale.Formatter;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.data.type.DBDataType;
import com.dci.intellij.dbn.execution.method.MethodExecutionInput;
import com.dci.intellij.dbn.execution.method.result.MethodExecutionResult;
import com.dci.intellij.dbn.object.DBArgument;
import com.dci.intellij.dbn.object.DBFunction;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.DBSchema;
import com.intellij.openapi.project.Project;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class MethodExecutionProcessorImpl<T extends DBMethod> implements MethodExecutionProcessor<T> {
    private T method;
    private List<DBArgument> arguments;

    protected MethodExecutionProcessorImpl(T method) {
        this.method = method;
        this.arguments = new ArrayList<DBArgument>(method.getArguments());
    }

    public T getMethod() {
        method = (T) method.getUndisposedElement();
        return method;
    }

    public List<DBArgument> getArguments() {
        return arguments;
    }

    public DBArgument getReturnArgument() {
        DBMethod method = getMethod();
        if (method instanceof DBFunction) {
            DBFunction function = (DBFunction) method;
            return function.getReturnArgument();
        }
        return null;
    }

    int getParametersCount() {
        return arguments.size();
    }

    public void execute(MethodExecutionInput executionInput) throws SQLException {
        boolean usePoolConnection = executionInput.isUsePoolConnection();
        ConnectionHandler connectionHandler = getMethod().getConnectionHandler();
        DBSchema executionSchema = executionInput.getExecutionSchema();
        Connection connection = usePoolConnection ?
                connectionHandler.getPoolConnection(executionSchema) :
                connectionHandler.getStandaloneConnection(executionSchema);
        execute(executionInput, connection);
    }

    public void execute(MethodExecutionInput executionInput, Connection connection) throws SQLException {
        long startTime = System.currentTimeMillis();

        String command = buildExecutionCommand(executionInput);
        ConnectionHandler connectionHandler = getMethod().getConnectionHandler();
        boolean usePoolConnection = executionInput.isUsePoolConnection();
        CallableStatement callableStatement = connection.prepareCall (command);

        prepareCall(executionInput, callableStatement);
        callableStatement.execute();
        callableStatement.setQueryTimeout(10);
        if (!usePoolConnection) connectionHandler.notifyOpenTransactions(true);

        MethodExecutionResult executionResult = executionInput.getExecutionResult();
        loadValues(executionResult, callableStatement);
        executionResult.setExecutionDuration((int) (System.currentTimeMillis() - startTime));

        if (executionInput.isCommitAfterExecution()) {
            if (usePoolConnection) connection.commit(); else connectionHandler.commit();
        }
        
        if (usePoolConnection) connectionHandler.freePoolConnection(connection);
    }

    protected void prepareCall(MethodExecutionInput executionInput, CallableStatement callableStatement) throws SQLException {
        for (DBArgument argument : arguments) {
            DBDataType dataType = argument.getDataType();
            if (argument.isInput()) {
                String stringValue = executionInput.getInputValue(argument);
                setParameterValue(callableStatement, argument.getPosition(), dataType, stringValue);
            }
            if (argument.isOutput()) {
               callableStatement.registerOutParameter(argument.getPosition(), dataType.getSqlType());
            }
        }
    }

    public void loadValues(MethodExecutionResult executionResult, CallableStatement callableStatement) throws SQLException {
        for (DBArgument argument : arguments) {
            if (argument.isOutput()) {
                Object result = callableStatement.getObject(argument.getPosition());
                executionResult.addArgumentValue(argument, result);
            }
        }
    }

    private Project getProject() {
        return getMethod().getProject();
    }

    protected void setParameterValue(CallableStatement callableStatement, int parameterIndex, DBDataType dataType, String stringValue) throws SQLException {
        try {
            Object value = null;
            if (StringUtil.isNotEmptyOrSpaces(stringValue))  {
                Formatter formatter = Formatter.getInstance(getProject());
                value = formatter.parseObject(dataType.getTypeClass(), stringValue);
                value = dataType.getNativeDataType().getDataTypeDefinition().convert(value);
            }
            dataType.setValueToPreparedStatement(callableStatement, parameterIndex, value);

        } catch (SQLException e) {
            throw e;
        }  catch (Exception e) {
            throw new SQLException("Invalid value for data type " + dataType.getName() + " provided: \"" + stringValue + "\"");
        }
    }

    public abstract String buildExecutionCommand(MethodExecutionInput executionInput) throws SQLException;
}
