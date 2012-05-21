package com.dci.intellij.dbn.vfs;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.util.DocumentUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.DBLanguageFile;
import com.dci.intellij.dbn.language.sql.SQLFileType;
import com.dci.intellij.dbn.object.DBSchema;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.PsiDocumentManagerImpl;
import com.intellij.util.LocalTimeCounter;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class SQLConsoleFile extends VirtualFile implements DatabaseFile, DBVirtualFile {
    private long modificationTimestamp = LocalTimeCounter.currentTime();
    private CharSequence content = "";
    private ConnectionHandler connectionHandler;
    private DBSchema currentSchema;
    protected String path;
    protected String url;


    public SQLConsoleFile(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.currentSchema = connectionHandler.getUserSchema();
        path = DatabaseFileSystem.createPath(connectionHandler);
        url = DatabaseFileSystem.createUrl(connectionHandler);
        setCharset(connectionHandler.getSettings().getDetailSettings().getCharset());
    }

    public PsiFile initializePsiFile(DatabaseFileViewProvider fileViewProvider, DBLanguage language) {
        ConnectionHandler connectionHandler = getConnectionHandler();
        DBLanguageDialect languageDialect = connectionHandler.getLanguageDialect(language);
        if (languageDialect != null) {
            DBLanguageFile file = (DBLanguageFile) languageDialect.getParserDefinition().createFile(fileViewProvider);
            fileViewProvider.forceCachedPsi(file);
            Document document = DocumentUtil.getDocument(this);
            PsiDocumentManagerImpl.cachePsi(document, file);
            return file;
        }
        return null;
    }

    public Icon getIcon() {
        return Icons.SQL_CONSOLE;
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public void setCurrentSchema(DBSchema currentSchema) {
        this.currentSchema = currentSchema;
    }

    public DBSchema getCurrentSchema() {
        return currentSchema;
    }

    @NotNull
    @Override
    public String getName() {
        return connectionHandler.getName();
    }

    @NotNull
    @Override
    public VirtualFileSystem getFileSystem() {
        return DatabaseFileSystem.getInstance();
    }

    @Override
    public String getPath() {
        return path;
    }

    @NotNull
    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public VirtualFile getParent() {
        return null;
    }

    @Override
    public VirtualFile[] getChildren() {
        return VirtualFile.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return SQLFileType.INSTANCE;
    }

    @NotNull
    public OutputStream getOutputStream(Object requestor, final long modificationTimestamp, long newTimeStamp) throws IOException {
        return new ByteArrayOutputStream() {
            public void close() {
                SQLConsoleFile.this.modificationTimestamp = modificationTimestamp;
                content = toString();
            }
        };
    }

    @NotNull
    public byte[] contentsToByteArray() throws IOException {
        Charset charset = getCharset();
        return content.toString().getBytes(charset.name());
    }

    @Override
    public long getTimeStamp() {
        return 0;
    }

  public long getModificationStamp() {
    return modificationTimestamp;
  }

    @Override
    public long getLength() {
        try {
            return contentsToByteArray().length;
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
            return 0;
        }
    }

    @Override
    public void refresh(boolean asynchronous, boolean recursive, Runnable postRunnable) {
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(contentsToByteArray());
    }

    @Override
    public String getExtension() {
        return "sql";
    }
}
