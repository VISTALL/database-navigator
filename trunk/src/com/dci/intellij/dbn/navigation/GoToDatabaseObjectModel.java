package com.dci.intellij.dbn.navigation;

import com.dci.intellij.dbn.common.content.loader.DynamicContentLoaderRegistry;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.VirtualConnectionHandler;
import com.dci.intellij.dbn.navigation.options.ObjectsLookupSettings;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.dci.intellij.dbn.object.common.list.DBObjectListVisitor;
import com.dci.intellij.dbn.options.GlobalProjectSettings;
import com.intellij.ide.util.gotoByName.ChooseByNameModel;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GoToDatabaseObjectModel implements ChooseByNameModel {
    private Project project;
    private boolean cancelled = false;
    private ConnectionHandler connectionHandler;
    private ObjectsLookupSettings objectsLookupSettings;
    private Object[] EMPTY_ARRAY = new Object[0];
    private String[] EMPTY_STRING_ARRAY = new String[0];


    public GoToDatabaseObjectModel(@NotNull Project project, @Nullable ConnectionHandler connectionHandler) {
        this.project = project;
        this.connectionHandler = connectionHandler;
        objectsLookupSettings = GlobalProjectSettings.getInstance(project).getNavigationSettings().getObjectsLookupSettings();
    }

    public String getPromptText() {
        String connectionIdentifier = connectionHandler == null || connectionHandler instanceof VirtualConnectionHandler ?
                "All Connections" :
                connectionHandler.getName();
        return "Enter database object name (" + connectionIdentifier + ")";
    }

    public String getNotInMessage() {
        return null;
    }

    public String getNotFoundMessage() {
        return "Database object not found";
    }

    public String getCheckBoxName() {
        return objectsLookupSettings.getForceDatabaseLoad().value() ? "Load database objects" : null;
    }

    public char getCheckBoxMnemonic() {
        return 0;
    }

    public boolean loadInitialCheckBoxState() {
        return false;
    }

    public void saveInitialCheckBoxState(boolean state) {
    }

    public ListCellRenderer getListCellRenderer() {
        return new DatabaseObjectListCellRenderer();
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean willOpenEditor() {
        return false;
    }

    public String[] getNames(boolean checkBoxState) {
        boolean databaseLoadActive = objectsLookupSettings.getForceDatabaseLoad().value();

        if (checkBoxState && !databaseLoadActive) return EMPTY_STRING_ARRAY;

        boolean forceLoad = checkBoxState && databaseLoadActive;
        if (forceLoad) DynamicContentLoaderRegistry.registerBulkLoad();

        ObjectNamesCollector collector = new ObjectNamesCollector(forceLoad);
        scanObjectLists(collector);

        if (forceLoad) DynamicContentLoaderRegistry.unregisterBulkLoad();
        Set<String> bucket = collector.getBucket();
        return bucket == null ?
                EMPTY_STRING_ARRAY :
                bucket.toArray(new String[bucket.size()]);
    }

    public Object[] getElementsByName(String name, boolean checkBoxState, String pattern) {
        boolean forceLoad = checkBoxState && objectsLookupSettings.getForceDatabaseLoad().value();
        ObjectCollector collector = new ObjectCollector(name, forceLoad);
        scanObjectLists(collector);
        return collector.getBucket() == null ? EMPTY_ARRAY : collector.getBucket().toArray();
    }

    private void scanObjectLists(DBObjectListVisitor visitor) {
        if (connectionHandler == null || connectionHandler instanceof VirtualConnectionHandler) {
            ConnectionManager connectionManager = ConnectionManager.getInstance(project);
            Set<ConnectionHandler> connectionHandlers = connectionManager.getConnectionHandlers();
            for (ConnectionHandler connectionHandler : connectionHandlers) {
                if (breakLoad()) break;
                DBObjectListContainer objectListContainer = connectionHandler.getObjectBundle().getObjectListContainer();
                objectListContainer.visitLists(visitor, false);
            }
        } else {
            DBObjectListContainer objectListContainer = connectionHandler.getObjectBundle().getObjectListContainer();
            objectListContainer.visitLists(visitor, false);
        }
    }

    private class ObjectNamesCollector implements DBObjectListVisitor {
        private boolean forceLoad;
        private DBObject parentObject;
        private Set<String> bucket;

        private ObjectNamesCollector(boolean forceLoad) {
            this.forceLoad = forceLoad;
        }

        public void visitObjectList(DBObjectList<DBObject> objectList) {
            if (isListScannable(objectList) && isParentRelationValid(objectList)) {
                boolean isObjectTypeActive = objectsLookupSettings.isEnabled(objectList.getObjectType());
                DBObject originalParentObject = parentObject;
                for (DBObject object : objectList.getObjects()) {
                    if (breakLoad()) break;
                    if (isObjectTypeActive) {
                        if (bucket == null) bucket = new THashSet<String>();
                        bucket.add(object.getName());
                    }

                    parentObject = object;
                    object.getChildObjects().visitLists(this, false);
                }
                parentObject = originalParentObject;
            }
        }

        private boolean isListScannable(DBObjectList<DBObject> objectList) {
            return objectList != null && (objectList.isLoaded() || objectList.isSourceContentLoaded() || forceLoad);
        }

        private boolean isParentRelationValid(DBObjectList<DBObject> objectList) {
            return parentObject == null || objectList.getObjectType().isChildOf(parentObject.getObjectType());
        }

        public Set<String> getBucket() {
            return bucket;
        }
    }



    private class ObjectCollector implements DBObjectListVisitor {
        private String objectName;
        private boolean forceLoad;
        private DBObject parentObject;
        private List<DBObject> bucket;

        private ObjectCollector(String objectName, boolean forceLoad) {
            this.objectName = objectName;
            this.forceLoad = forceLoad;
        }

        public void visitObjectList(DBObjectList<DBObject> objectList) {
            if (isListScannable(objectList) && isParentRelationValid(objectList)) {
                boolean isActiveObjectType = objectsLookupSettings.isEnabled(objectList.getObjectType());
                DBObject originalParentObject = parentObject;
                for (DBObject object : objectList.getObjects()) {
                    if (breakLoad()) break;
                    if (isActiveObjectType && object.getName().equals(objectName)) {
                        if (bucket == null) bucket = new ArrayList<DBObject>();
                        bucket.add(object);
                    }

                    parentObject = object;
                    object.getChildObjects().visitLists(this, false);
                }
                parentObject = originalParentObject;
            }
        }

        private boolean isListScannable(DBObjectList<DBObject> objectList) {
            return objectList != null && (objectList.isLoaded() || objectList.isSourceContentLoaded() || forceLoad);
        }

        private boolean isParentRelationValid(DBObjectList<DBObject> objectList) {
            return parentObject == null || objectList.getObjectType().isChildOf(parentObject.getObjectType());
        }

        public List<DBObject> getBucket() {
            return bucket;
        }
    }


    private boolean breakLoad() {
        return cancelled || !ApplicationManager.getApplication().isActive();
    }

    public String getElementName(Object element) {
        if (element instanceof DBObject) {
            DBObject object = (DBObject) element;
            return object.getQualifiedName();
        }

        return element == null ? null : element.toString();
    }

    @NotNull
    public String[] getSeparators() {
        return new String[]{"."};
    }

    public String getFullName(Object element) {
        return getElementName(element);
    }

    public String getHelpId() {
        return null;
    }

    public class DatabaseObjectListCellRenderer extends ColoredListCellRenderer {
        @Override
        protected void customizeCellRenderer(JList list, Object value, int index, boolean selected, boolean hasFocus) {
            if (value instanceof DBObject) {
                DBObject object = (DBObject) value;
                setIcon(object.getIcon());
                append(object.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                append(" [" + object.getConnectionHandler().getName() + "]", SimpleTextAttributes.GRAY_ATTRIBUTES);
                if (object.getParentObject() != null) {
                    append(" - " + object.getParentObject().getQualifiedName(), SimpleTextAttributes.GRAY_ATTRIBUTES);
                }
            } else append(value.toString(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }
    }
}
