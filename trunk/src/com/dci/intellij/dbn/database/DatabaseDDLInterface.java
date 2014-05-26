package com.dci.intellij.dbn.database;

import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.DBTrigger;
import com.dci.intellij.dbn.object.factory.MethodFactoryInput;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseDDLInterface extends DatabaseInterface{
    int getEditorHeaderEndOffset(DatabaseObjectTypeId objectTypeId, String objectName, String sourceContent);

    boolean includesTypeAndNameInSourceContent(DatabaseObjectTypeId objectTypeId);

    String createTriggerEditorHeader(DBTrigger trigger);

    String createMethodEditorHeader(DBMethod method);

    /*********************************************************
     *                   CREATE statements                   *
     *********************************************************/
    void createView(String viewName, String code, Connection connection) throws SQLException;

    void createMethod(MethodFactoryInput methodFactoryInput, Connection connection) throws SQLException;

    void createObject(String code, Connection connection) throws SQLException;

    /*********************************************************
     *                   CHANGE statements                   *
     *********************************************************/
    void updateView(String viewName, String oldCode, String newCode, Connection connection) throws SQLException;

    void updateObject(String objectName, String objectType, String oldCode, String newCode, Connection connection) throws SQLException;

   /*********************************************************
    *                   DROP statements                     *
    *********************************************************/
    void dropObject(String objectType, String objectName, Connection connection) throws SQLException;

   /*********************************************************
    *                   RENAME statements                     *
    *********************************************************/

}
