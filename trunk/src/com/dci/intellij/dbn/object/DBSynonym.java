package com.dci.intellij.dbn.object;

import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBSchemaObject;

public interface DBSynonym extends DBSchemaObject {
    DBObject getUnderlyingObject();
}