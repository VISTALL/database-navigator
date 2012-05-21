package com.dci.intellij.dbn.data.type;

import java.util.List;

public class BasicDataTypeDefinition implements DataTypeDefinition {
    private BasicDataType basicDataType;
    private String name;
    private Class typeClass;
    private int sqlType;


    public BasicDataTypeDefinition(String name, Class typeClass, int sqlType, BasicDataType basicDataType, List<DataTypeDefinition> bundle) {
        this.name = name;
        this.typeClass = typeClass;
        this.sqlType = sqlType;
        this.basicDataType = basicDataType;
        bundle.add(this);
    }

    public String getName() {
        return name;
    }

    public Class getTypeClass() {
        return typeClass;
    }

    public int getSqlType() {
        return sqlType;
    }

    public BasicDataType getBasicDataType() {
        return basicDataType;
    }

    @Override
    public String toString() {
        return name;
    }

    public Object convert(Object object) {
        return object;
    }
}