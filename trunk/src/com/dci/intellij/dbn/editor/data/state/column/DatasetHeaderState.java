package com.dci.intellij.dbn.editor.data.state.column;

import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatasetHeaderState {
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

    public DatasetColumnState getColumnState(String columnName) {
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
                } else {
                    columnState.readState(childElement);
                }
            }
            Collections.sort(columnStates);
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

    public void moveColumn(int fromIndex, int toIndex) {
        DatasetColumnState columnState = columnStates.remove(fromIndex);
        columnStates.add(toIndex, columnState);
        for (int i=0; i<columnStates.size(); i++) {
            columnStates.get(i).setPosition(i);
        }
    }

    public boolean isVisible(String name) {
        DatasetColumnState columnState = getColumnState(name);
        return columnState == null || columnState.isVisible();
    }
}
