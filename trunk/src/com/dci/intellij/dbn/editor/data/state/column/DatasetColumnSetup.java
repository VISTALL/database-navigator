package com.dci.intellij.dbn.editor.data.state.column;

import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatasetColumnSetup {
    private List<DatasetColumnState> columns;

    public List<DatasetColumnState> getColumns() {
        return columns;
    }

    public synchronized void init(DBDataset dataset) {
        if (columns == null) columns = new ArrayList<DatasetColumnState>();

        if (dataset != null) {
            for (DBColumn column : dataset.getColumns()) {
                DatasetColumnState columnsState = getColumnState(column.getName());
                if (columnsState == null) {
                    if (!column.isHidden()) {
                        columnsState = new DatasetColumnState(column);
                        columns.add(columnsState);
                    }
                } else {
                    columnsState.init(column);
                }
            }
        }
        Collections.sort(columns);
    }

    public DatasetColumnState getColumnState(String columnName) {
        if (columns != null) {
            for (DatasetColumnState columnsState : columns) {
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
                if (columns == null) columns = new ArrayList<DatasetColumnState>();
                String columnName = childElement.getAttributeValue("name");
                DatasetColumnState columnState = getColumnState(columnName);
                if (columnState == null) {
                    columnState = new DatasetColumnState(childElement);
                    columns.add(columnState);
                } else {
                    columnState.readState(childElement);
                }
            }
            Collections.sort(columns);
        }
    }

    public void writeState(Element element) {
        if (columns != null) {
            for (DatasetColumnState columnState : columns) {
                Element childElement = new Element("column");
                element.addContent(childElement);
                columnState.writeState(childElement);
            }
        }
    }

    public void moveColumn(int fromIndex, int toIndex) {
        int visibleFromIndex = fromIndex;
        int visibleToIndex = toIndex;

        int visibleIndex = -1;
        for (int i=0; i< columns.size(); i++) {
            DatasetColumnState columnState = columns.get(i);
            if (columnState.isVisible()) {
                visibleIndex++;
                if (visibleIndex == fromIndex) visibleFromIndex = i;
                if (visibleIndex == toIndex) visibleToIndex = i;
            }
        }

        DatasetColumnState columnState = columns.remove(visibleFromIndex);
        columns.add(visibleToIndex, columnState);
        for (int i=0; i< columns.size(); i++) {
            columns.get(i).setPosition(i);
        }
    }

    public boolean isVisible(String name) {
        DatasetColumnState columnState = getColumnState(name);
        return columnState == null || columnState.isVisible();
    }
}
