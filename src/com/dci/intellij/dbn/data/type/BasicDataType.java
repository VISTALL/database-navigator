package com.dci.intellij.dbn.data.type;

public enum BasicDataType {
    LITERAL("Literal"),
    NUMERIC("Numeric"),
    DATE_TIME("Date/Time"),
    CLOB("Character Large Object"),
    BLOB("Byte Large Object"),
    ROWID("Row ID"),
    FILE("File"),
    BOOLEAN("Boolean"),
    CURSOR("Cursor");

    private String name;

    private BasicDataType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean is(BasicDataType ... basicDataTypes) {
        for (BasicDataType basicDataType : basicDataTypes) {
            if (this == basicDataType) return true;
        }
        return false;
    }

    public boolean isLOB() {
        return is(BasicDataType.BLOB, BasicDataType.CLOB);
    }
}
