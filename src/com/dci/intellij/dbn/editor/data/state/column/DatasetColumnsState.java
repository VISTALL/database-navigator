package com.dci.intellij.dbn.editor.data.state.column;

import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatasetColumnsState {
    private List<DatasetColumnState> columnStates;

    public List<DatasetColumnState> getColumnStates() {
        return columnStates;
    }

    public synchronized void init(DBDataset dataset) {
        if (columnStates == null) columnStates = new ArrayList<DatasetColumnState>();

        for (DBColumn column : dataset.getColumns()) {
            DatasetColumnState columnsState = getColumnState(column.getName());
            if (columnsState == null) {
                if (!column.isHidden()) {
                    columnsState = new DatasetColumnState(column);
                    columnStates.add(columnsState);
                }
            } else {
                columnsState.init(column);
            }
        }
        Collections.sort(columnStates);
    }

    private DatasetColumnState getColumnState(String columnName) {
        if (columnStates != null) {
            for (DatasetColumnState columnsState : columnStates) {
                if (columnName.equals(columnsState.getName())) {
                    return columnsState;
                }
            }
        }
        return null;
    }

    public void readState(Element element) {
        if (element != null) {
            List<Element> childElements = element.getChildren();
            for (Element childElement : childElements) {
                if (columnStates == null) columnStates = new ArrayList<DatasetColumnState>();
                String columnName = childElement.getAttributeValue("name");
                DatasetColumnState columnState = getColumnState(columnName);
                if (columnState == null) {
                    columnState = new DatasetColumnState(childElement);
                    columnStates.add(columnState);
                }
            }
        }
    }

    public void writeState(Element element) {
        if (columnStates != null) {
            for (DatasetColumnState columnState : columnStates) {
                Element childElement = new Element("column");
                element.addContent(childElement);
                columnState.writeState(childElement);
            }
        }
    }

}
