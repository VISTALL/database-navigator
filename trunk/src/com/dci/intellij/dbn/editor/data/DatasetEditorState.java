package com.dci.intellij.dbn.editor.data;

import com.dci.intellij.dbn.data.model.sortable.SortableDataModelState;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import gnu.trove.THashMap;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public class DatasetEditorState extends SortableDataModelState implements FileEditorState {
    public boolean canBeMergedWith(FileEditorState fileEditorState, FileEditorStateLevel fileEditorStateLevel) {
        return false;
    }

    public void readState(@NotNull Element sourceElement) {
        try {
            setRowCount(Integer.parseInt(sourceElement.getAttributeValue("row-count")));
            setReadonly(Boolean.parseBoolean(sourceElement.getAttributeValue("readonly")));
            getSortingState().setColumnName(sourceElement.getAttributeValue("sort-column-name"));
            getSortingState().setDirectionAsString(sourceElement.getAttributeValue("sort-direction"));
        }
        catch(NumberFormatException numberformatexception) {}

        Element contentTypesElement = sourceElement.getChild("content-types");
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
        clone.setReadonly(isReadonly());
        clone.setRowCount(getRowCount());
        clone.setSortingState(getSortingState());
        if (contentTypesMap != null) {
            clone.contentTypesMap = new THashMap<String, String>(contentTypesMap);
        }

        return clone;
    }
}