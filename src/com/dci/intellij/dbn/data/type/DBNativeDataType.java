package com.dci.intellij.dbn.data.type;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentElement;
import com.dci.intellij.dbn.data.value.BlobValue;
import com.dci.intellij.dbn.data.value.ClobValue;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public class DBNativeDataType implements DynamicContentElement {
    private DataTypeDefinition dataTypeDefinition;

    public DBNativeDataType(DataTypeDefinition dataTypeDefinition) {
        this.dataTypeDefinition = dataTypeDefinition;
    }

    public boolean isDisposed() {
        return false;
    }

    public String getName() {
        return dataTypeDefinition.getName();
    }

    public DataTypeDefinition getDataTypeDefinition() {
        return dataTypeDefinition;
    }
    
    public BasicDataType getBasicDataType() {
        return dataTypeDefinition.getBasicDataType();
    }

    public boolean isLOB() {
        return getBasicDataType().isLOB();
    }

    public Object getValueFromResultSet(ResultSet resultSet, int columnIndex) throws SQLException {
        // FIXME: add support for stream updatable types
        BasicDataType basicDataType = dataTypeDefinition.getBasicDataType();
        if (basicDataType == BasicDataType.BLOB) return new BlobValue(resultSet.getBlob(columnIndex));
        if (basicDataType == BasicDataType.CLOB) return new ClobValue(resultSet.getClob(columnIndex));
        if (basicDataType == BasicDataType.ROWID) return "[ROWID]";
        if (basicDataType == BasicDataType.FILE) return "[FILE]";

        Class clazz = dataTypeDefinition.getTypeClass();
        if (Number.class.isAssignableFrom(clazz) && resultSet.getString(columnIndex) == null) {
            // mysql converts null numbers to 0!!!
            // FIXME make this database dependent (e.g. in CompatibilityInterface).
            return null;
        }
        return
                clazz == String.class ? resultSet.getString(columnIndex) :
                clazz == Byte.class ? resultSet.getByte(columnIndex) :
                clazz == Short.class ? resultSet.getShort(columnIndex) :
                clazz == Integer.class ? resultSet.getInt(columnIndex) :
                clazz == Long.class ? resultSet.getLong(columnIndex) :
                clazz == Float.class ? resultSet.getFloat(columnIndex) :
                clazz == Double.class ? resultSet.getDouble(columnIndex) :
                clazz == BigDecimal.class ? resultSet.getBigDecimal(columnIndex) :
                clazz == Date.class ? resultSet.getDate(columnIndex) :
                clazz == Time.class ? resultSet.getTime(columnIndex) :
                clazz == Timestamp.class ? resultSet.getTimestamp(columnIndex) :
                        resultSet.getObject(columnIndex);
    }

    public void setValueToResultSet(ResultSet resultSet, int columnIndex, Object value) throws SQLException {
        // FIXME: add support for stream updatable types
        BasicDataType basicDataType = dataTypeDefinition.getBasicDataType();
        if (basicDataType == BasicDataType.BLOB) return;
        if (basicDataType == BasicDataType.CLOB) return;
        if (basicDataType == BasicDataType.ROWID) return;
        if (basicDataType == BasicDataType.FILE) return;

        if (value == null) {
            resultSet.updateObject(columnIndex, null);
        } else {
            Class clazz = dataTypeDefinition.getTypeClass();
            if (value.getClass().isAssignableFrom(clazz)) {
                if(clazz == String.class) resultSet.updateString(columnIndex, (String) value); else
                if(clazz == Byte.class) resultSet.updateByte(columnIndex, (Byte) value); else
                if(clazz == Short.class) resultSet.updateShort(columnIndex, (Short) value); else
                if(clazz == Integer.class) resultSet.updateInt(columnIndex, (Integer) value); else
                if(clazz == Long.class) resultSet.updateLong(columnIndex, (Long) value); else
                if(clazz == Float.class) resultSet.updateFloat(columnIndex, (Float) value); else
                if(clazz == Double.class) resultSet.updateDouble(columnIndex, (Double) value); else
                if(clazz == BigDecimal.class) resultSet.updateBigDecimal(columnIndex, (BigDecimal) value); else
                if(clazz == Date.class) resultSet.updateDate(columnIndex, (Date) value); else
                if(clazz == Time.class) resultSet.updateTime(columnIndex, (Time) value); else
                if(clazz == Timestamp.class) resultSet.updateTimestamp(columnIndex, (Timestamp) value); else
                        resultSet.updateObject(columnIndex, value);
            } else {
                throw new SQLException("Can not convert \"" + value.toString() + "\" into " + dataTypeDefinition.getName());
            }
        }
    }

    public void setValueToPreparedStatement(PreparedStatement callableStatement, int parameterIndex, Object value) throws SQLException {
        BasicDataType basicDataType = dataTypeDefinition.getBasicDataType();
        if (basicDataType == BasicDataType.CURSOR) return;

        if (value == null) {
            callableStatement.setObject(parameterIndex, null);
        } else {
            Class clazz = dataTypeDefinition.getTypeClass();
            if (value.getClass().isAssignableFrom(clazz)) {
                if(clazz == String.class) callableStatement.setString(parameterIndex, (String) value); else
                if(clazz == Byte.class) callableStatement.setByte(parameterIndex, (Byte) value); else
                if(clazz == Short.class) callableStatement.setShort(parameterIndex, (Short) value); else
                if(clazz == Integer.class) callableStatement.setInt(parameterIndex, (Integer) value); else
                if(clazz == Long.class) callableStatement.setLong(parameterIndex, (Long) value); else
                if(clazz == Float.class) callableStatement.setFloat(parameterIndex, (Float) value); else
                if(clazz == Double.class) callableStatement.setDouble(parameterIndex, (Double) value); else
                if(clazz == BigDecimal.class) callableStatement.setBigDecimal(parameterIndex, (BigDecimal) value); else
                if(clazz == Date.class) callableStatement.setDate(parameterIndex, (Date) value); else
                if(clazz == Time.class) callableStatement.setTime(parameterIndex, (Time) value); else
                if(clazz == Timestamp.class) callableStatement.setTimestamp(parameterIndex, (Timestamp) value); else
                if(clazz == Boolean.class) callableStatement.setBoolean(parameterIndex, (Boolean) value); else
                        callableStatement.setObject(parameterIndex, value);
            } else {
                throw new SQLException("Can not convert \"" + value.toString() + "\" into " + dataTypeDefinition.getName());
            }
        }
    }

    public int getSqlType(){
        return dataTypeDefinition.getSqlType();
    }


    public String toString() {
        return dataTypeDefinition.getName();
    }

    /*********************************************************
     *                 DynamicContentElement                 *
     *********************************************************/
    public boolean isValid() {
        return true;
    }

    public void setValid(boolean valid) {

    }

    public String getDescription() {
        return null;
    }

    public DynamicContent getOwnerContent() {
        return null;
    }

    public void setOwnerContent(DynamicContent ownerContent) {
    }

    public void reload() {
    }

    public void dispose() {

    }

    public int compareTo(Object o) {
        DBNativeDataType remote = (DBNativeDataType) o;
        return getName().compareTo(remote.getName());
    }
}
