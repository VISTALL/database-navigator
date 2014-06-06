package com.dci.intellij.dbn.editor.data.state.visibility;

import com.dci.intellij.dbn.common.ui.list.Selectable;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;

import javax.swing.Icon;

public class DatasetColumnVisibility  implements Selectable {
    private DBObjectRef<DBColumn> columnRef;
    private boolean visible;
    private int position;

    public DatasetColumnVisibility(DBColumn column) {
        this.columnRef = column.getRef();
    }

    public DBColumn getColumn() {
        return columnRef.get();
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
        return false;
    }

    @Override
    public void setSelected(boolean selected) {
        visible = selected;
    }
}
