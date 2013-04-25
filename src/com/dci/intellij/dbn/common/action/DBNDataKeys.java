package com.dci.intellij.dbn.common.action;

import com.dci.intellij.dbn.execution.statement.result.StatementExecutionResult;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.util.Key;

public interface DBNDataKeys {
    DataKey<StatementExecutionResult> STATEMENT_EXECUTION_RESULT = DataKey.create("DBNavigator.StatementExecutionResult");
    Key<String> ACTION_PLACE_KEY = Key.create("DBNavigator.ActionPlace");
}
