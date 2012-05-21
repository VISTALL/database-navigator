package com.dci.intellij.dbn.data.type;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;

public class DateTimeDataTypeDefinition extends BasicDataTypeDefinition {
    private Constructor constructor;
    public DateTimeDataTypeDefinition(String name, Class typeClass, int sqlType, BasicDataType basicDataType, List<DataTypeDefinition> bundle) {
        super(name, typeClass, sqlType, basicDataType, bundle);
        try {
            constructor = typeClass.getConstructor(long.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public Object convert(Object object) {
        assert object instanceof Date;

        Date date = (Date) object;
        if (object.getClass().equals(getTypeClass())) {
            return object;
        }
        try {
            return constructor.newInstance(date.getTime());
        } catch (Throwable e) {
            e.printStackTrace();
            return object;
        }

    }
}