package com.dci.intellij.dbn.database.postgres;

import com.dci.intellij.dbn.database.DatabaseMessageParserInterface;
import com.dci.intellij.dbn.database.DatabaseObjectIdentifier;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

public class PostgresMessageParserInterface implements DatabaseMessageParserInterface {
    @Nullable
    public DatabaseObjectIdentifier identifyObject(String message) {
         return null;
    }

    public boolean isTimeoutException(SQLException e) {
        return false;
    }

    public boolean isModelException(SQLException e) {
        return false;
    }

}