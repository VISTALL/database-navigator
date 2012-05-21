package com.dci.intellij.dbn.vfs;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.common.DevNullStreams;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.navigation.psi.NavigationPsiCache;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.UnknownFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class  DatabaseObjectFile<T extends DBObject> extends VirtualFile implements DBVirtualFile {
    private static final byte[] EMPTY_BYTE_CONTENT = new byte[0];
    protected T object;

    protected String name;
    protected String path;
    protected String url;

    public DatabaseObjectFile(T object) {
        this.object = object;
        this.name = object.getName();
    }

    public T getObject() {
        object = (T) object.getUndisposedElement();
        return object;
    }

    public ConnectionHandler getConnectionHandler() {
        return object.getConnectionHandler();
    }

    public boolean equals(Object obj) {
        if (obj instanceof DatabaseObjectFile) {
            DatabaseObjectFile objectFile = (DatabaseObjectFile) obj;
            return objectFile.getObject().equals(getObject());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return object.getQualifiedNameWithType().hashCode();
    }

    public Project getProject() {
        return object.getProject();
    }

    /*********************************************************
     *                     VirtualFile                       *
     *********************************************************/
    @NotNull
    @NonNls
    public String getName() {
        return name;
    }

    @Override
    public String getPresentableName() {
        return name;
    }

    @NotNull
    public FileType getFileType() {
        return UnknownFileType.INSTANCE;
    }

    @NotNull
    public DatabaseFileSystem getFileSystem() {
        return DatabaseFileSystem.getInstance();
    }

    public String getPath() {
        if (path == null) {
            path = DatabaseFileSystem.createPath(object);
        }
        return path;
    }

    @NotNull
    public String getUrl() {
        if (url == null) {
            url = DatabaseFileSystem.createUrl(object);
        }
        return url;
    }

    public boolean isWritable() {
        return false;
    }

    public boolean isDirectory() {
        return true;
    }

    public boolean isValid() {
        return true;
    }

    @Nullable
    public VirtualFile getParent() {
        BrowserTreeElement treeParent = object.getTreeParent();
        if (treeParent instanceof DBObjectList<?>) {
            DBObjectList objectList = (DBObjectList) treeParent;
            return NavigationPsiCache.getPsiDirectory(objectList).getVirtualFile();
        }
        return null;
    }

    public Icon getIcon() {
        return object.getOriginalIcon();
    }

    public VirtualFile[] getChildren() {
        return VirtualFile.EMPTY_ARRAY;
    }

    @NotNull
    public OutputStream getOutputStream(Object o, long l, long l1) throws IOException {
        return DevNullStreams.OUTPUT_STREAM;
    }

    @NotNull
    public byte[] contentsToByteArray() throws IOException {
        return EMPTY_BYTE_CONTENT;
    }

    public long getTimeStamp() {
        return 0;
    }

    public long getLength() {
        return 0;
    }

    public void refresh(boolean b, boolean b1, Runnable runnable) {

    }

    public InputStream getInputStream() throws IOException {
        return DevNullStreams.INPUT_STREAM;
    }

    public long getModificationStamp() {
        return 1;
    }

    @Override
    public String getExtension() {
        return null;
    }


}

