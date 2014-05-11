package com.dci.intellij.dbn.ddl;

import com.dci.intellij.dbn.ddl.ui.DDLMappedNotificationPanel;
import com.dci.intellij.dbn.language.common.DBLanguageFileType;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.vfs.DBVirtualFile;
import com.dci.intellij.dbn.vfs.DatabaseFileSystem;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotifications;
import org.jetbrains.annotations.Nullable;

public class DDLMappedNotificationProvider extends EditorNotifications.Provider<DDLMappedNotificationPanel> {
    private static final Key<DDLMappedNotificationPanel> KEY = Key.create("ddl.mapped.notification.panel");
    private Project project;

    public DDLMappedNotificationProvider(Project project) {
        this.project = project;
    }

    @Override
    public Key<DDLMappedNotificationPanel> getKey() {
        return KEY;
    }

    @Nullable
    @Override
    public DDLMappedNotificationPanel createNotificationPanel(VirtualFile file, FileEditor fileEditor) {
        if (file instanceof DBVirtualFile) {
            return null;
        }
        if (file.getFileType() instanceof DBLanguageFileType) {
            DDLFileAttachmentManager attachmentManager = DDLFileAttachmentManager.getInstance(project);
            DBSchemaObject editableObject = attachmentManager.getEditableObject(file);
            if (editableObject != null) {
                DatabaseFileSystem databaseFileSystem = DatabaseFileSystem.getInstance();
                if (databaseFileSystem.isFileOpened(editableObject))
                System.out.println();
            }
        }
        return null;
    }
}
