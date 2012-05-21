package com.dci.intellij.dbn.common.action;

import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.execution.statement.result.StatementExecutionResult;
import com.intellij.openapi.actionSystem.DataKey;

public interface DBNDataKeys {
    DataKey<DatasetEditor> DATASET_EDITOR = DataKey.create("DBNavigator.DatasetEditor");
    DataKey<StatementExecutionResult> STATEMENT_EXECUTION_RESULT = DataKey.create("DBNavigator.StatementExecutionResult");
}
