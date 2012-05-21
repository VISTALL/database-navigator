package com.dci.intellij.dbn.data.type;

public interface DataTypeDefinition {
    String getName();
    Class getTypeClass();
    int getSqlType();
    BasicDataType getBasicDataType();
    Object convert(Object object);
}
