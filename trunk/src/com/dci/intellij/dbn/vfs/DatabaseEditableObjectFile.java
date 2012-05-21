package com.dci.intellij.dbn.vfs;

import com.dci.intellij.dbn.common.Constants;
import com.dci.intellij.dbn.common.thread.ConditionalLaterInvocator;
import com.dci.intellij.dbn.common.util.DocumentUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.mapping.FileConnectionMappingProvider;
import com.dci.intellij.dbn.ddl.DDLFileBindingManager;
import com.dci.intellij.dbn.ddl.DDLFileType;
import com.dci.intellij.dbn.ddl.ObjectToDDLContentSynchronizer;
import com.dci.intellij.dbn.ddl.options.DDLFileGeneralSettings;
import com.dci.intellij.dbn.ddl.options.DDLFileSettings;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilter;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterManager;
import com.dci.intellij.dbn.editor.data.options.DataEditorSettings;
import com.dci.intellij.dbn.language.sql.SQLFileType;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.common.property.DBObjectProperty;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DatabaseEditableObjectFile extends DatabaseObjectFile<DBSchemaObject> implements FileConnectionMappingProvider {
    public ThreadLocal<Document> FAKE_DOCUMENT = new ThreadLocal<Document>();
    private List<DatabaseContentFile> contentFiles;

    public DatabaseEditableObjectFile(DBSchemaObject object) {
        super(object);
        name = object.getName();
    }

    public ConnectionHandler getActiveConnection() {
        return object.getConnectionHandler();
    }

    public DBSchema getCurrentSchema() {
        return object.getSchema();
    }

    public ConnectionHandler getConnectionHandler() {
        return object.getConnectionHandler();
    }

    public boolean preOpen() {
        Project project = object.getProject();
        if (object.getContentType() == DBContentType.DATA) {
            DBDataset dataset = (DBDataset) object;
            DatasetFilterManager filterManager = DatasetFilterManager.getInstance(project);
            DatasetFilter filter = filterManager.getActiveFilter(dataset);

            if (filter == null) {
                DataEditorSettings settings = DataEditorSettings.getInstance(project);
                if (settings.getFilterSettings().isPromptFilterDialog()) {
                    int exitCode = filterManager.openFiltersDialog(dataset, true, false, settings.getFilterSettings().getDefaultFilterType());
                    return exitCode != DialogWrapper.CANCEL_EXIT_CODE;
                }
            }
        }
        else if (object.getContentType() == DBContentType.CODE ||
                 object.getContentType() == DBContentType.CODE_SPEC_AND_BODY) {

            DDLFileGeneralSettings ddlFileSettings = DDLFileSettings.getInstance(project).getGeneralSettings();
            if (ddlFileSettings.getLookupDDLFilesEnabled().value()) {
                List<VirtualFile> boundDDLFiles = getBoundDDLFiles();
                if (boundDDLFiles == null || boundDDLFiles.isEmpty()) {
                    DDLFileBindingManager fileBindingManager = DDLFileBindingManager.getInstance(project);
                    List<VirtualFile> virtualFiles = fileBindingManager.lookupUnboundDDLFiles(object);
                    if (virtualFiles.size() > 0) {
                        int exitCode = fileBindingManager.showFileBindingDialog(object, virtualFiles);
                        return exitCode != DialogWrapper.CANCEL_EXIT_CODE;
                    } else if (ddlFileSettings.getCreateDDLFilesEnabled().value()) {
                        int exitCode = Messages.showYesNoDialog(
                                "Could not find any DDL file for " + object.getQualifiedNameWithType() + ". Do you want to create one? \n" +
                                "(You can disable this check in \"DDL File\" options)",
                                Constants.DBN_TITLE_PREFIX + "No DDL file found", Messages.getQuestionIcon());
                        if (exitCode == DialogWrapper.OK_EXIT_CODE) {
                            fileBindingManager.createDDLFile(object);
                        }
                    }
                }
            }
        }
        return true;
    }

    public synchronized List<DatabaseContentFile> getContentFiles() {
        if (contentFiles == null) {
            contentFiles = new ArrayList<DatabaseContentFile>();
            DBContentType objectContentType = object.getContentType();
            if (objectContentType.isBundle()) {
                DBContentType[] contentTypes = objectContentType.getSubContentTypes();
                for (DBContentType contentType : contentTypes) {
                    DatabaseContentFile virtualFile =
                            contentType.isCode() ? new SourceCodeFile(this, contentType) :
                            contentType.isData() ? new DatasetFile(this, contentType) : null;
                    contentFiles.add(virtualFile);
                }
            } else {
                DatabaseContentFile virtualFile =
                        objectContentType.isCode() ? new SourceCodeFile(this, objectContentType) :
                        objectContentType.isData() ? new DatasetFile(this, objectContentType) : null;
                contentFiles.add(virtualFile);
            }
        }
        return contentFiles;
    }

    @Nullable
    public List<VirtualFile> getBoundDDLFiles() {
        DDLFileBindingManager fileBindingManager = DDLFileBindingManager.getInstance(object.getProject());
        if (object.getProperties().is(DBObjectProperty.EDITABLE)) {
            return fileBindingManager.getBoundDDLFiles(object);
        }
        return null;
    }

    public void updateDDLFiles() {
        for (DatabaseContentFile contentFile : getContentFiles()) {
            updateDDLFiles(contentFile.getContentType());
        }
    }

    public void updateDDLFiles(final DBContentType sourceContentType) {
        new ConditionalLaterInvocator() {
            public void run() {
                ObjectToDDLContentSynchronizer synchronizer = new ObjectToDDLContentSynchronizer(sourceContentType, DatabaseEditableObjectFile.this);
                ApplicationManager.getApplication().runWriteAction(synchronizer);
            }
        }.start();
    }

    public DatabaseContentFile getContentFile(DBContentType contentType) {
        for (DatabaseContentFile contentFile : getContentFiles()) {
            if (contentFile.getContentType() == contentType) {
                return contentFile;
            }
        }
        return null;
    }

    /*********************************************************
     *                     VirtualFile                       *
     *********************************************************/
    @NotNull
    public FileType getFileType() {
        DDLFileType type = object.getDDLFileType(null);
        return type == null ? SQLFileType.INSTANCE : type.getLanguageFileType();
    }

    public boolean isWritable() {
        return true;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    public DatabaseContentFile getDebuggableContentFile(){
        DBContentType contentType = object.getContentType();
        if (contentType == DBContentType.CODE) {
            return getContentFile(DBContentType.CODE);
        }

        if (contentType == DBContentType.CODE_SPEC_AND_BODY) {
            return getContentFile(DBContentType.CODE_BODY);
        }
        return null;
    }

    @NotNull
    public byte[] contentsToByteArray() throws IOException {
        DBContentType mainContentType = getMainContentType();
        if (mainContentType != null) {
            return getContentFile(mainContentType).contentsToByteArray();
        }
        return new byte[0];
    }

    @Override
    public <T> T getUserData(Key<T> key) {
        DBContentType mainContentType = getMainContentType();
        boolean isCode = mainContentType == DBContentType.CODE || mainContentType == DBContentType.CODE_BODY;
        if (key == FileDocumentManagerImpl.DOCUMENT_KEY && isCode) {
            if (FAKE_DOCUMENT.get() != null) {
                return (T) new WeakReference<Document>(FAKE_DOCUMENT.get());
            }

            DatabaseContentFile mainContentFile = getMainContentFile();
            Document document = DocumentUtil.getDocument(mainContentFile);
            return (T) new WeakReference<Document>(document);
        }
        return super.getUserData(key);
    }

    public DBContentType getMainContentType() {
        DBContentType contentType = object.getContentType();
        return
            contentType == DBContentType.CODE ? DBContentType.CODE :
            contentType == DBContentType.CODE_SPEC_AND_BODY ? DBContentType.CODE_BODY : null;
    }

    public DatabaseContentFile getMainContentFile() {
        DBContentType mainContentType = getMainContentType();
        return getContentFile(mainContentType);
    }

    @Override
    public String getExtension() {
        return "psql";
    }


}

