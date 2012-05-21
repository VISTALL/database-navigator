package com.dci.intellij.dbn.execution.common.result.ui;

import com.dci.intellij.dbn.common.ui.UIForm;
import com.dci.intellij.dbn.execution.ExecutionResult;

public interface ExecutionResultForm<E extends ExecutionResult> extends UIForm {
    void setExecutionResult(E executionResult);
    E getExecutionResult();
}
