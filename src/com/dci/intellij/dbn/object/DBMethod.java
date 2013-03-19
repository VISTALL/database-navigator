package com.dci.intellij.dbn.object;

import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.identifier.DBMethodIdentifier;

import java.util.List;

public interface DBMethod extends DBSchemaObject {
    DBMethodIdentifier getIdentifier();
    List<DBArgument> getArguments();
    DBArgument getArgument(String name);
    DBProgram getProgram();
    String getMethodType();
    int getOverload();
    boolean isEmbedded();
    boolean isDeterministic();
    boolean hasDeclaredArguments();
}
