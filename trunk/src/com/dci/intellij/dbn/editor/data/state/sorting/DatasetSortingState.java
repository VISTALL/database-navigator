package com.dci.intellij.dbn.editor.data.state.sorting;

import com.dci.intellij.dbn.data.sorting.MultiColumnSortingState;
import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.data.sorting.SortingInstruction;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import org.jdom.Element;

import java.util.List;

public class DatasetSortingState extends MultiColumnSortingState{
    private DBObjectRef<DBDataset> datasetRef;

    public DatasetSortingState(DBDataset dataset) {
        this.datasetRef = DBObjectRef.from(dataset);
    }

    public DBDataset getDataset() {
        return DBObjectRef.get(datasetRef);
    }

    public void writeState(Element element) {
        for (SortingInstruction sortingInstruction : getSortingInstructions()) {
            String columnName = sortingInstruction.getColumnName();
            SortDirection sortDirection = sortingInstruction.getDirection();
            if (columnName != null && !sortDirection.isIndefinite()) {
                Element columnElement = new Element("column");
                columnElement.setAttribute("name", columnName);
                columnElement.setAttribute("direction", sortDirection.name());
                element.addContent(columnElement);
            }
        }
    }

    public void readState(Element element) {
        List<Element> columnElements = element.getChildren();
        for (Element columnElement: columnElements) {
            String columnName = columnElement.getAttributeValue("name");
            String sortDirection = columnElement.getAttributeValue("direction");
            addSortingInstruction(columnName, SortDirection.valueOf(sortDirection));
        }
    }

}
