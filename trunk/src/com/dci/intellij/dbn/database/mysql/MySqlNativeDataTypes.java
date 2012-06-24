package com.dci.intellij.dbn.database.mysql;

import com.dci.intellij.dbn.data.type.BasicDataType;
import com.dci.intellij.dbn.data.type.BasicDataTypeDefinition;
import com.dci.intellij.dbn.data.type.DataTypeDefinition;
import com.dci.intellij.dbn.data.type.DateTimeDataTypeDefinition;
import com.dci.intellij.dbn.data.type.NumericDataTypeDefinition;
import com.dci.intellij.dbn.database.common.DatabaseNativeDataTypes;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class MySqlNativeDataTypes implements DatabaseNativeDataTypes {
    public static List<DataTypeDefinition> ALL = new ArrayList<DataTypeDefinition>();
    
    public static final DataTypeDefinition CHAR = new BasicDataTypeDefinition("CHAR", String.class, Types.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition VARCHAR = new BasicDataTypeDefinition("VARCHAR", String.class, Types.VARCHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition BINARY = new BasicDataTypeDefinition("BINARY", String.class, Types.BINARY, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition VARBINARY = new BasicDataTypeDefinition("VARBINARY", String.class, Types.VARBINARY, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition NATIONAL_CHAR = new BasicDataTypeDefinition("NATIONAL CHAR", String.class, Types.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition NATIONAL_VARCHAR = new BasicDataTypeDefinition("NATIONAL VARCHAR", String.class, Types.VARCHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition ENUM = new BasicDataTypeDefinition("ENUM", String.class, Types.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition SET = new BasicDataTypeDefinition("SET", String.class, Types.CHAR, BasicDataType.LITERAL, ALL);
     
    public static final DataTypeDefinition BIT = new NumericDataTypeDefinition("BIT", Short.class, Types.BIT, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition TINYINT = new NumericDataTypeDefinition("TINYINT", Short.class, Types.TINYINT, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition BOOL = new NumericDataTypeDefinition("BOOL", Boolean.class, Types.BOOLEAN, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition BOOLEAN = new NumericDataTypeDefinition("BOOLEAN", Boolean.class, Types.BOOLEAN, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition SMALLINT = new NumericDataTypeDefinition("SMALLINT", Integer.class, Types.SMALLINT, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition MEDIUMINT = new NumericDataTypeDefinition("MEDIUMINT", Integer.class, Types.INTEGER, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition INT = new NumericDataTypeDefinition("INT", Long.class, Types.INTEGER, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition INT_UNSIGNED = new NumericDataTypeDefinition("INT UNSIGNED", Long.class, Types.INTEGER, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition INTEGER = new NumericDataTypeDefinition("INTEGER", Long.class, Types.INTEGER, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition BIGINT = new NumericDataTypeDefinition("BIGINT", Long.class, Types.BIGINT, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition FLOAT = new NumericDataTypeDefinition("FLOAT", Float.class, Types.FLOAT, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition DOUBLE = new NumericDataTypeDefinition("DOUBLE", Double.class, Types.DOUBLE, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition DOUBLE_PRECISION = new NumericDataTypeDefinition("DOUBLE PRECISION", Double.class, Types.DOUBLE, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition DECIMAL = new NumericDataTypeDefinition("DECIMAL", BigDecimal.class, Types.DECIMAL, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition DEC = new NumericDataTypeDefinition("DEC", BigDecimal.class, Types.DECIMAL, BasicDataType.NUMERIC, ALL);
 
    public static final DataTypeDefinition DATE = new DateTimeDataTypeDefinition("DATE", Timestamp.class, Types.DATE, BasicDataType.DATE_TIME, ALL);
    public static final DataTypeDefinition DATETIME = new DateTimeDataTypeDefinition("DATETIME", Timestamp.class, Types.TIMESTAMP, BasicDataType.DATE_TIME, ALL);
    public static final DataTypeDefinition TIMESTAMP = new DateTimeDataTypeDefinition("TIMESTAMP", Timestamp.class, Types.TIMESTAMP, BasicDataType.DATE_TIME, ALL);
    public static final DataTypeDefinition TIME = new DateTimeDataTypeDefinition("TIME", Timestamp.class, Types.TIME, BasicDataType.DATE_TIME, ALL);
    public static final DataTypeDefinition YEAR = new DateTimeDataTypeDefinition("YEAR", Timestamp.class, Types.DATE, BasicDataType.DATE_TIME, ALL);

    public static final DataTypeDefinition TINYBLOB = new BasicDataTypeDefinition("TINYBLOB", Blob.class, Types.BLOB, BasicDataType.BLOB, ALL);
    public static final DataTypeDefinition TINYTEXT = new BasicDataTypeDefinition("TINYTEXT", Blob.class, Types.CLOB, BasicDataType.CLOB, ALL);
    public static final DataTypeDefinition BLOB = new BasicDataTypeDefinition("BLOB", Blob.class, Types.BLOB, BasicDataType.BLOB, ALL);
    public static final DataTypeDefinition TEXT = new BasicDataTypeDefinition("TEXT", Blob.class, Types.CLOB, BasicDataType.CLOB, ALL);
    public static final DataTypeDefinition MEDIUMBLOB = new BasicDataTypeDefinition("MEDIUMBLOB", Blob.class, Types.BLOB, BasicDataType.BLOB, ALL);
    public static final DataTypeDefinition MEDIUMTEXT = new BasicDataTypeDefinition("MEDIUMTEXT", Blob.class, Types.CLOB, BasicDataType.CLOB, ALL);
    public static final DataTypeDefinition LONGBLOB = new BasicDataTypeDefinition("LONGBLOB", Blob.class, Types.BLOB, BasicDataType.BLOB, ALL);
    public static final DataTypeDefinition LONGTEXT = new BasicDataTypeDefinition("LONGTEXT", Blob.class, Types.CLOB, BasicDataType.CLOB, ALL);
    
    public List<DataTypeDefinition> list() {
        return ALL;
    }
}