package com.dci.intellij.dbn.object.common;

import com.dci.intellij.dbn.object.DBSchema;

public interface ObjectTypeFilter {
    boolean acceptRootObject(DBObjectType objectType);

    boolean acceptCurrentSchemaObject(DBObjectType objectType);

    boolean acceptPublicSchemaObject(DBObjectType objectType);

    boolean acceptAnySchemaObject(DBObjectType objectType);

    boolean acceptObject(DBSchema schema, DBSchema currentSchema, DBObjectType objectType);
}
