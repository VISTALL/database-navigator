package com.dci.intellij.dbn.execution.method;

import com.dci.intellij.dbn.object.DBArgument;
import com.dci.intellij.dbn.object.DBTypeAttribute;

import java.sql.ResultSet;

public class ArgumentValue {
    private DBArgument argument;
    private DBTypeAttribute attribute;
    private Object value;

    public ArgumentValue(DBArgument argument, DBTypeAttribute attribute, Object value) {
        this.argument = argument;
        this.attribute = attribute;
        this.value = value;
    }

    public ArgumentValue(DBArgument argument, Object value) {
        this.argument = argument;
        this.value = value;
    }

    public DBArgument getArgument() {
        return argument;
    }

    public DBTypeAttribute getAttribute() {
        return attribute;
    }

    public String getName() {
        return
            attribute == null ?
            argument.getName() :
            argument.getName() + "." + attribute.getName();        
    }

    public Object getValue() {
        return value;
    }

    public boolean isCursor() {
        return value instanceof ResultSet;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String toString() {
        return argument.getName() + " = " + value;
    }
}
