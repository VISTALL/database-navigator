package com.dci.intellij.dbn.editor.data.state.column;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.options.setting.SettingsUtil;
import com.dci.intellij.dbn.common.ui.list.Selectable;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import org.jdom.Element;
import org.jetbrains.generate.tostring.util.StringUtil;

import javax.swing.Icon;
import java.util.Comparator;

public class DatasetColumnState implements Selectable<DatasetColumnState>{
    public static final Comparator<DatasetColumnState> NAME_COMPARATOR = new Comparator<DatasetColumnState>() {
        @Override
        public int compare(DatasetColumnState o1, DatasetColumnState o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    public static final Comparator<DatasetColumnState> POSITION_COMPARATOR = new Comparator<DatasetColumnState>() {
        @Override
        public int compare(DatasetColumnState o1, DatasetColumnState o2) {
            return o1.getOriginalPosition() - o2.getOriginalPosition();
        }
    };

    private DBObjectRef<DBColumn> columnRef;
    private boolean visible = true;
    private String name;
    private int position = -1;

    public DatasetColumnState(DBColumn column) {
        init(column);
    }

    public void init(DBColumn column) {
        columnRef = DBObjectRef.from(column);
        if (StringUtil.isEmpty(name)) {
            // not initialized yet
            name = columnRef.getName();
            position = column.getPosition() -1;
            visible = true;
        }
    }


    public DatasetColumnState(Element element) {
        readState(element);
    }

    public void readState(Element element) {
        name = element.getAttributeValue("name");
        position = SettingsUtil.getIntegerAttribute(element, "position", -1);
        visible = SettingsUtil.getBooleanAttribute(element, "visible", true);
    }

    public void writeState(Element element) {
        element.setAttribute("name", name);
        SettingsUtil.setIntegerAttribute(element, "position", position);
        SettingsUtil.setBooleanAttribute(element, "visible", visible);
    }

    public DBColumn getColumn() {
        return DBObjectRef.get(columnRef);
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
        DBColumn column = getColumn();
        if (column != null) {
            return column.getPosition();
        }
        return 0;
    }

    @Override
    public Icon getIcon() {
        DBColumn column = getColumn();
        return column == null ? Icons.DBO_COLUMN : column.getIcon();
    }

    @Override
    public String getName() {
        return name;
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
    public int compareTo(DatasetColumnState remote) {
        return position-remote.position;
    }
}
