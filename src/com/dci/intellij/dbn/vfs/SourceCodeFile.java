package com.dci.intellij.dbn.vfs;

import com.dci.intellij.dbn.common.DevNullStreams;
import com.dci.intellij.dbn.common.util.DocumentUtil;
import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.DatabaseDDLInterface;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.language.common.DBLanguage;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.DBLanguageFile;
import com.dci.intellij.dbn.language.common.psi.PsiUtil;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.PsiDocumentManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SourceCodeFile extends DatabaseContentFile implements DatabaseFile, DocumentListener {
    private static final Key<VirtualFile> FILE_KEY = Key.create("FILE_KEY");

    private String originalContent;
    private String lastSavedContent;
    private String content;
    private Timestamp changeTimestamp;
    public int documentHashCode;

    public SourceCodeFile(final DatabaseEditableObjectFile databaseFile, DBContentType contentType) {
        super(databaseFile, contentType);
        updateChangeTimestamp();
        setCharset(databaseFile.getConnectionHandler().getSettings().getDetailSettings().getCharset());
        try {
            String content = getObject().loadCodeFromDatabase(contentType);
            this.content = StringUtil.removeCharacter(content, '\r');
        } catch (SQLException e) {
            content = "";
            MessageUtil.showErrorDialog(
                    "Could not load sourcecode for " +
                            databaseFile.getObject().getQualifiedNameWithType() + " from database.", e);
        }
    }

    public PsiFile initializePsiFile(DatabaseFileViewProvider fileViewProvider, DBLanguage language) {
        ConnectionHandler connectionHandler = getConnectionHandler();

        DBSchemaObject underlyingObject = getObject();
        String parseRootId = getParseRootId();
        if (parseRootId != null) {
            DBLanguageDialect languageDialect = connectionHandler.getLanguageDialect(language);
            if (languageDialect != null) {
                DBLanguageFile file = (DBLanguageFile) languageDialect.getParserDefinition().createFile(fileViewProvider);
                file.setParseRootId(parseRootId);
                file.setUnderlyingObject(underlyingObject);
                fileViewProvider.forceCachedPsi(file);
                Document document = DocumentUtil.getDocument(fileViewProvider.getVirtualFile());
                document.putUserData(FILE_KEY, getDatabaseFile());
                PsiDocumentManagerImpl.cachePsi(document, file);
                return file;
            }
        }
        return null;
    }

    public String getParseRootId() {
        return getObject().getCodeParseRootId(contentType);
    }

    public DBLanguageFile getPsiFile() {
        return (DBLanguageFile) PsiUtil.getPsiFile(getProject(), this);
    }

    public int getEditorHeaderEndOffset() {
        DatabaseDDLInterface ddlInterface = getObject().getConnectionHandler().getInterfaceProvider().getDDLInterface();
        return ddlInterface.getEditorHeaderEndOffset(getObject().getObjectType().getTypeId(), getObject().getName(), content);
    }


    public void updateChangeTimestamp() {
        try {
            Timestamp timestamp = getObject().loadChangeTimestamp(getContentType());
            if (timestamp != null) {
                changeTimestamp = timestamp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Timestamp getChangeTimestamp() {
        return changeTimestamp;
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public String getLastSavedContent() {
        return lastSavedContent == null ? originalContent : lastSavedContent;
    }

    public void setContent(String content) {
        if (originalContent == null) {
            originalContent = this.content;
        }
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void reloadFromDatabase() throws SQLException {
        updateChangeTimestamp();
        originalContent = null;

        String content = getObject().loadCodeFromDatabase(contentType);
        this.content = StringUtil.removeCharacter(content, '\r');

        getDatabaseFile().updateDDLFiles(getContentType());
        setModified(false);
    }

    public void updateToDatabase() throws SQLException {
        DBSchemaObject object = getObject();
        object.executeUpdateDDL(getContentType(), getLastSavedContent(), content);
        updateChangeTimestamp();
        getDatabaseFile().updateDDLFiles(getContentType());
        setModified(false);
        lastSavedContent = content;
    }

    @NotNull
    public OutputStream getOutputStream(Object o, long l, long l1) throws IOException {
        return DevNullStreams.OUTPUT_STREAM;
    }

    @NotNull
    public byte[] contentsToByteArray() {
        return content.getBytes(getCharset());
    }

    public String createDDLStatement() {
        String content = this.content.trim();
        if (content.length() > 0) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(getObject().createDDLStatement(content));

            return buffer.toString();
        }
        return "";
    }

    public long getLength() {
        return content.length();
    }

    public int getDocumentHashCode() {
        return documentHashCode;
    }

    public void setDocumentHashCode(int documentHashCode) {
        this.documentHashCode = documentHashCode;
    }

    @Override
    public <T> void putUserData(Key<T> key, T value) {
        if (key == FileDocumentManagerImpl.DOCUMENT_KEY && (contentType == DBContentType.CODE || contentType == DBContentType.CODE_BODY)) {
            databaseFile.putUserData(FileDocumentManagerImpl.DOCUMENT_KEY, (Reference<Document>) value);
        }
        super.putUserData(key, value);
    }

    /**
     * ******************************************************
     * DocumentListener                    *
     * *******************************************************
     */
    public void beforeDocumentChange(DocumentEvent event) {

    }

    public void documentChanged(DocumentEvent event) {
        setModified(true);
    }

    public boolean equals(Object obj) {
        if (obj instanceof SourceCodeFile) {
            SourceCodeFile virtualFile = (SourceCodeFile) obj;
            return virtualFile.getObject().equals(getObject()) && virtualFile.getContentType() == getContentType();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (getObject().getQualifiedNameWithType() + getContentType().getDescription()).hashCode();
    }

    @Override
    public void dispose() {
        originalContent = null;
        lastSavedContent = null;
        content = null;
        super.dispose();
    }
}
