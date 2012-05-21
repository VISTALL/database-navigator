package com.dci.intellij.dbn.navigation.psi;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import gnu.trove.THashMap;

import java.util.Map;

public class NavigationPsiCache {
    private Map<DBObject, DBObjectPsiFile> objectPsiFiles = new THashMap<DBObject, DBObjectPsiFile>();
    private Map<DBObject, DBObjectPsiDirectory> objectPsiDirectories = new THashMap<DBObject, DBObjectPsiDirectory>();
    private Map<DBObjectList, DBObjectListPsiDirectory> objectListPsiDirectories = new THashMap<DBObjectList, DBObjectListPsiDirectory>();
    private DBConnectionPsiDirectory connectionPsiDirectory;

    public NavigationPsiCache(ConnectionHandler connectionHandler) {
        connectionPsiDirectory = new DBConnectionPsiDirectory(connectionHandler);
    }

    public DBConnectionPsiDirectory getConnectionPsiDirectory() {
        return connectionPsiDirectory;
    }

    private synchronized DBObjectPsiFile lookupPsiFile(DBObject object) {
        DBObjectPsiFile psiFile = objectPsiFiles.get(object);
        if (psiFile == null) {
            psiFile = new DBObjectPsiFile(object);
            objectPsiFiles.put(object, psiFile);
        }

        return psiFile;
    }

    private synchronized DBObjectPsiDirectory lookupPsiDirectory(DBObject object) {
        DBObjectPsiDirectory psiDirectory = objectPsiDirectories.get(object);
        if (psiDirectory == null) {
            psiDirectory = new DBObjectPsiDirectory(object);
            objectPsiDirectories.put(object, psiDirectory);
        }

        return psiDirectory;
    }
    
    private synchronized DBObjectListPsiDirectory lookupPsiDirectory(DBObjectList objectList) {
        DBObjectListPsiDirectory psiDirectory = objectListPsiDirectories.get(objectList);
        if (psiDirectory == null) {
            psiDirectory = new DBObjectListPsiDirectory(objectList);
            objectListPsiDirectories.put(objectList, psiDirectory);
        }

        return psiDirectory;
    }
    
    
    public static DBObjectPsiFile getPsiFile(DBObject object) {
        return object == null ? null :
                object.getConnectionHandler().getPsiCache().lookupPsiFile(object);
    }

    public static DBObjectPsiDirectory getPsiDirectory(DBObject object) {
        return object == null ? null :
                object.getConnectionHandler().getPsiCache().lookupPsiDirectory(object);
    }
    
    public static DBObjectListPsiDirectory getPsiDirectory(DBObjectList objectList) {
        return objectList == null ? null :
                objectList.getConnectionHandler().getPsiCache().lookupPsiDirectory(objectList);
    }

    public static DBConnectionPsiDirectory getPsiDirectory(ConnectionHandler connectionHandler) {
        return connectionHandler.getPsiCache().getConnectionPsiDirectory();
    }
}
