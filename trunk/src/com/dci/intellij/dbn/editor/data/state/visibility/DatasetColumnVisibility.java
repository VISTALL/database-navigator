package com.dci.intellij.dbn.editor.data.state.visibility;

import com.dci.intellij.dbn.common.ui.list.Selectable;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;

import javax.swing.Icon;
import java.util.Comparator;

public class DatasetColumnVisibility  implements Selectable<DatasetColumnVisibility>{
    public static final Comparator<DatasetColumnVisibility> NAME_COMPARATOR = new Comparator<DatasetColumnVisibility>() {
        @Override
        public int compare(DatasetColumnVisibility o1, DatasetColumnVisibility o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    public static final Comparator<DatasetColumnVisibility> POSITION_COMPARATOR = new Comparator<DatasetColumnVisibility>() {
        @Override
        public int compare(DatasetColumnVisibility o1, DatasetColumnVisibility o2) {
            return o1.originalPosition - o2.originalPosition;
        }
    };

    private DBObjectRef<DBColumn> columnRef;
    private boolean visible = true;
    private int position;
    private int originalPosition;

    public DatasetColumnVisibility(DBColumn column) {
        this.columnRef = column.getRef();
        this.position = column.getPosition() -1;
        this.originalPosition = column.getPosition();
    }

    public DBColumn getColumn() {
        DBColumn column = columnRef.get();
        if (column != null) {
            this.originalPosition = column.getPosition();
        }
        return column;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getOriginalPosition() {
        return originalPosition;
    }

    public void setOriginalPosition(int originalPosition) {
        this.originalPosition = originalPosition;
    }

    @Override
    public Icon getIcon() {
        return getColumn().getIcon();
    }

    @Override
    public String getName() {
        return getColumn().getName();
    }

    @Override
    public String getError() {
        return null;
    }

    @Override
    public boolean isSelected() {
        return visible;
    }

    @Override
    public boolean isMasterSelected() {
        return true;
    }

    @Override
    public void setSelected(boolean selected) {
        visible = selected;
    }

    @Override
    public int compareTo(DatasetColumnVisibility remote) {
        return position-remote.position;
    }
}
