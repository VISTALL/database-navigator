package com.dci.intellij.dbn.vfs;

import com.dci.intellij.dbn.common.DevNullStreams;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.mapping.FileConnectionMappingProvider;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.psql.PSQLLanguage;
import com.dci.intellij.dbn.language.sql.SQLLanguage;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.DBView;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.io.IOException;
import java.io.InputStream;

public abstract class DatabaseContentFile extends VirtualFile implements FileConnectionMappingProvider, DBVirtualFile {
    protected DatabaseFileSystem fileSystem;
    protected DatabaseEditableObjectFile databaseFile;
    protected DBContentType contentType;
    private boolean modified;

    public ConnectionHandler getActiveConnection() {
        return getObject().getConnectionHandler();
    }

    @Override
    public boolean isInLocalFileSystem() {
        return false;
    }

    public DBSchema getCurrentSchema() {
        return getObject().getSchema();
    }

    public DatabaseContentFile(DatabaseEditableObjectFile databaseFile, DBContentType contentType) {
        fileSystem = databaseFile.getFileSystem();
        this.databaseFile = databaseFile;
        this.contentType = contentType;
    }

    public DatabaseEditableObjectFile getDatabaseFile() {
        return databaseFile;
    }

    public DBContentType getContentType() {
        return contentType;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public DBSchemaObject getObject() {
        return databaseFile.getObject();
    }

    @Override
    public ConnectionHandler getConnectionHandler() {
        return databaseFile.getConnectionHandler();
    }

    public DBLanguageDialect getLanguageDialect() {
        DBLanguage language =
                getObject() instanceof DBView ?
                        SQLLanguage.INSTANCE :
                        PSQLLanguage.INSTANCE;
        
        return getObject().getLanguageDialect(language);
    }

    /*********************************************************
     *                     VirtualFile                       *
     *********************************************************/
    @NotNull
    @NonNls
    public String getName() {
        return getObject().getName();
    }

    @NotNull
    public FileType getFileType() {
        return getObject().getDDLFileType(contentType).getLanguageFileType();
    }

    @NotNull
    public VirtualFileSystem getFileSystem() {
        return fileSystem;
    }

    public String getPath() {
        return DatabaseFileSystem.createPath(getObject(), getContentType());
    }

    @NotNull
    public String getUrl() {
        return DatabaseFileSystem.createUrl(getObject());
    }

    public Project getProject() {
        return getObject().getProject();
    }

    public boolean isWritable() {
        return true;
    }

    public boolean isDirectory() {
        return false;
    }

    public boolean isValid() {
        return true;
    }

    @Nullable
    public VirtualFile getParent() {
        return databaseFile.getObject().getParentObject().getVirtualFile();
    }

    public Icon getIcon() {
        return getObject().getOriginalIcon();
    }

    public VirtualFile[] getChildren() {
        return VirtualFile.EMPTY_ARRAY;
    }

    public long getTimeStamp() {
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
}
