package com.dci.intellij.dbn.editor.data.state;

import com.dci.intellij.dbn.common.options.setting.SettingsUtil;
import com.dci.intellij.dbn.data.model.sortable.SortableDataModelState;
import com.dci.intellij.dbn.editor.data.state.column.DatasetHeaderState;
import com.dci.intellij.dbn.editor.data.state.sorting.DatasetSortingState;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import gnu.trove.THashMap;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public class DatasetEditorState extends SortableDataModelState implements FileEditorState {
    public static final DatasetEditorState VOID = new DatasetEditorState();
    private DatasetHeaderState headerState = new DatasetHeaderState();
    private DatasetSortingState dataSortingState = new DatasetSortingState();
    private DBObjectRef<DBDataset> datasetRef;

    public DatasetEditorState(DBDataset dataset) {
        datasetRef = DBObjectRef.from(dataset);
        headerState.init(dataset);
    }

    public DatasetEditorState() {
    }

    public boolean canBeMergedWith(FileEditorState fileEditorState, FileEditorStateLevel fileEditorStateLevel) {
        return false;
    }

    public DatasetHeaderState getHeaderState() {
        return headerState;
    }

    public DatasetSortingState getDataSortingState() {
        return dataSortingState;
    }

    public void readState(@NotNull Element element) {
        setRowCount(SettingsUtil.getIntegerAttribute(element, "row-count", 100));
        setReadonly(SettingsUtil.getBooleanAttribute(element, "readonly", false));


        getSortingState().setColumnName(element.getAttributeValue("sort-column-name"));
        getSortingState().setDirectionAsString(element.getAttributeValue("sort-direction"));

        Element columnsElement = element.getChild("columns");
        headerState.readState(columnsElement);

        Element contentTypesElement = element.getChild("content-types");
        if (contentTypesElement != null) {
            for (Object o : contentTypesElement.getChildren()) {
                Element contentTypeElement = (Element) o;
                String columnName = contentTypeElement.getAttributeValue("column-name");
                String contentTypeName = contentTypeElement.getAttributeValue("type-name");
                setTextContentType(columnName, contentTypeName);
            }
        }
    }

    public void writeState(Element targetElement) {
        targetElement.setAttribute("row-count", Integer.toString(getRowCount()));
        targetElement.setAttribute("readonly", Boolean.toString(isReadonly()));
        targetElement.setAttribute("sort-column-name", getSortingState().getColumnName());
        targetElement.setAttribute("sort-direction", getSortingState().getDirectionAsString());

        Element columnsElement = new Element("columns");
        targetElement.addContent(columnsElement);
        headerState.writeState(columnsElement);

        Element contentTypesElement = new Element("content-types");
        targetElement.addContent(contentTypesElement);
        if (contentTypesMap != null && contentTypesMap.size() > 0) {
            for (String columnName : contentTypesMap.keySet()) {
                Element contentTypeElement = new Element("content-type");
                String contentTypeName = contentTypesMap.get(columnName);
                contentTypeElement.setAttribute("column-name", columnName);
                contentTypeElement.setAttribute("type-name", contentTypeName);
                contentTypesElement.addContent(contentTypeElement);
            }
        }
    }

    public DatasetEditorState clone() {
        DatasetEditorState clone = new DatasetEditorState();
        clone.datasetRef = datasetRef;
        clone.setReadonly(isReadonly());
        clone.setRowCount(getRowCount());
        clone.setSortingState(getSortingState());
        if (contentTypesMap != null) {
            clone.contentTypesMap = new THashMap<String, String>(contentTypesMap);
        }

        return clone;
    }
}