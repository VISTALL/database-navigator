package com.dci.intellij.dbn.database.oracle;

import com.dci.intellij.dbn.data.type.BasicDataType;
import com.dci.intellij.dbn.data.type.BasicDataTypeDefinition;
import com.dci.intellij.dbn.data.type.DataTypeDefinition;
import com.dci.intellij.dbn.data.type.DateTimeDataTypeDefinition;
import com.dci.intellij.dbn.data.type.NumericDataTypeDefinition;
import com.dci.intellij.dbn.database.common.DatabaseNativeDataTypes;
import oracle.jdbc.OracleTypes;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OracleNativeDataTypes implements DatabaseNativeDataTypes {
    public static List<DataTypeDefinition> ALL = new ArrayList<DataTypeDefinition>();

    public static final DataTypeDefinition CHAR = new BasicDataTypeDefinition("CHAR", String.class, OracleTypes.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition CHAR_VARYING = new BasicDataTypeDefinition("CHAR VARYING", String.class, OracleTypes.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition VARCHAR2 = new BasicDataTypeDefinition("VARCHAR2", String.class, OracleTypes.VARCHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition VACHAR = new BasicDataTypeDefinition("VARCHAR", String.class, OracleTypes.VARCHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition CHARACTER = new BasicDataTypeDefinition("CHARACTER", String.class, OracleTypes.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition CHARACTER_VARYING = new BasicDataTypeDefinition("CHARACTER VARYING", String.class, OracleTypes.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition NATIONAL_CHAR = new BasicDataTypeDefinition("NATIONAL CHAR", String.class, OracleTypes.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition NATIONAL_CHAR_VARYING = new BasicDataTypeDefinition("NATIONAL CHAR VARYING", String.class, OracleTypes.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition NATIONAL_CHARACTER = new BasicDataTypeDefinition("NATIONAL CHARACTER", String.class, OracleTypes.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition NATIONAL_CHARACTER_VARYING = new BasicDataTypeDefinition("NATIONAL CHARACTER VARYING", String.class, OracleTypes.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition NCHAR_VARYING = new BasicDataTypeDefinition("NCHAR VARYING", String.class, OracleTypes.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition NCHAR = new BasicDataTypeDefinition("NCHAR", String.class, OracleTypes.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition NVARCHAR2 = new BasicDataTypeDefinition("NVARCHAR2", String.class, OracleTypes.CHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition STRING = new BasicDataTypeDefinition("STRING", String.class, OracleTypes.VARCHAR, BasicDataType.LITERAL, ALL);
    public static final DataTypeDefinition LONG = new BasicDataTypeDefinition("LONG", String.class, OracleTypes.LONGVARCHAR, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition LONG_RAW = new BasicDataTypeDefinition("LONG RAW", String.class, OracleTypes.LONGVARCHAR, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition RAW = new BasicDataTypeDefinition("RAW", String.class, OracleTypes.RAW, BasicDataType.LITERAL, ALL);

    public static final DataTypeDefinition NUMBER = new NumericDataTypeDefinition("NUMBER", BigDecimal.class, OracleTypes.NUMBER, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition BINARY_INTEGER = new NumericDataTypeDefinition("BINARY_INTEGER", Integer.class, OracleTypes.INTEGER, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition BINARY_FLOAT = new NumericDataTypeDefinition("BINARY_FLOAT", Float.class, OracleTypes.BINARY_FLOAT, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition BINARY_DOUBLE = new NumericDataTypeDefinition("BINARY_DOUBLE", Double.class, OracleTypes.BINARY_DOUBLE, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition NUMERIC = new NumericDataTypeDefinition("NUMERIC", Short.class, OracleTypes.NUMERIC, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition DECIMAL = new NumericDataTypeDefinition("DECIMAL", Short.class, OracleTypes.DECIMAL, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition DEC = new NumericDataTypeDefinition("DEC", Short.class, OracleTypes.DECIMAL, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition INTEGER = new NumericDataTypeDefinition("INTEGER", Integer.class, OracleTypes.INTEGER, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition INT = new NumericDataTypeDefinition("INT", Integer.class, OracleTypes.INTEGER, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition SMALLINT = new NumericDataTypeDefinition("SMALLINT", Short.class, OracleTypes.SMALLINT, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition FLOAT = new NumericDataTypeDefinition("FLOAT", Float.class, OracleTypes.FLOAT, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition DOUBLE_PRECISION = new NumericDataTypeDefinition("DOUBLE PRECISION", Double.class, OracleTypes.DOUBLE, BasicDataType.NUMERIC, ALL);
    public static final DataTypeDefinition REAL = new NumericDataTypeDefinition("REAL", Float.class, OracleTypes.FLOAT, BasicDataType.NUMERIC, ALL);

    public static final DataTypeDefinition DATE = new DateTimeDataTypeDefinition("DATE", Timestamp.class, OracleTypes.DATE, BasicDataType.DATE_TIME, ALL);
    public static final DataTypeDefinition TIME = new DateTimeDataTypeDefinition("TIME", Timestamp.class, OracleTypes.TIME, BasicDataType.DATE_TIME, ALL);
    public static final DataTypeDefinition TIME_WITH_TIME_ZONE = new DateTimeDataTypeDefinition("TIME WITH TIME ZONE", Timestamp.class, OracleTypes.TIMESTAMPTZ, BasicDataType.DATE_TIME, ALL);
    public static final DataTypeDefinition TIMESTAMP_WITH_LOCAL_TIME_ZONE = new DateTimeDataTypeDefinition("TIMESTAMP WITH LOCAL TIME ZONE", Timestamp.class, OracleTypes.TIMESTAMPLTZ, BasicDataType.DATE_TIME, ALL);
    public static final DataTypeDefinition TIMESTAMP = new DateTimeDataTypeDefinition("TIMESTAMP", Timestamp.class, OracleTypes.TIMESTAMP, BasicDataType.DATE_TIME, ALL);
    public static final DataTypeDefinition INTERVAL_DAY_TO_SECOND = new BasicDataTypeDefinition("INTERVAL DAY TO SECOND", Object.class, OracleTypes.INTERVALDS, BasicDataType.DATE_TIME, ALL);
    public static final DataTypeDefinition INTERVAL_YEAR_TO_MONTH = new BasicDataTypeDefinition("INTERVAL YEAR TO MONTH", Object.class, OracleTypes.INTERVALYM, BasicDataType.DATE_TIME, ALL);

    public static final DataTypeDefinition BLOB = new BasicDataTypeDefinition("BLOB", Blob.class, OracleTypes.BLOB, BasicDataType.BLOB, ALL);
    public static final DataTypeDefinition CLOB = new BasicDataTypeDefinition("CLOB", Clob.class, OracleTypes.CLOB, BasicDataType.CLOB, ALL);
    public static final DataTypeDefinition NCLOB = new BasicDataTypeDefinition("NCLOB", Clob.class, OracleTypes.CLOB, BasicDataType.CLOB, ALL);

    public static final DataTypeDefinition BFILE = new BasicDataTypeDefinition("BFILE", Object.class, OracleTypes.BFILE, BasicDataType.FILE, ALL);

    public static final DataTypeDefinition ROWID = new BasicDataTypeDefinition("ROWID", Object.class, OracleTypes.ROWID, BasicDataType.ROWID, ALL);
    public static final DataTypeDefinition UROWID = new BasicDataTypeDefinition("UROWID", Object.class, OracleTypes.ROWID, BasicDataType.ROWID, ALL);

    public static final DataTypeDefinition REF_CURSOR = new BasicDataTypeDefinition("REF CURSOR", Object.class, OracleTypes.CURSOR, BasicDataType.CURSOR, ALL);
    public static final DataTypeDefinition PL_SQL_BOOLEAN = new BasicDataTypeDefinition("PL/SQL BOOLEAN", String.class, OracleTypes.VARCHAR, BasicDataType.BOOLEAN, ALL);

    public List<DataTypeDefinition> list() {
        return ALL;
    }
}
