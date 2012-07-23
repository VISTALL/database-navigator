package com.dci.intellij.dbn.data.record.ui;

import com.dci.intellij.dbn.common.ui.MouseUtil;
import com.dci.intellij.dbn.common.util.TextAttributesUtil;
import com.dci.intellij.dbn.data.editor.color.DataGridTextAttributesKeys;
import com.dci.intellij.dbn.data.record.DatasetRecord;
import com.dci.intellij.dbn.editor.data.DatasetEditorManager;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterInput;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBConstraint;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.ui.SimpleTextAttributes;

import javax.swing.JDialog;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ColumnValueTextField extends JTextField {
    private DatasetRecord record;
    private DBColumn column;

    public ColumnValueTextField(DatasetRecord record, DBColumn column) {
        this.record = record;
        this.column = column;
        if (column.isPrimaryKey()) {
            SimpleTextAttributes textAttributes = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.PRIMARY_KEY);
            setForeground(textAttributes.getFgColor());
            Color background = textAttributes.getBgColor();
            if (background != null) {
                setBackground(background);
            }
        } else if (column.isForeignKey()) {
            addMouseListener(mouseListener);
            SimpleTextAttributes textAttributes = TextAttributesUtil.getSimpleTextAttributes(DataGridTextAttributesKeys.FOREIGN_KEY);
            setForeground(textAttributes.getFgColor());
            Color background = textAttributes.getBgColor();
            if (background != null) {
                setBackground(background);
            }
        }

    }

    protected void processMouseMotionEvent(MouseEvent e) {
        if (column.isForeignKey()) {
            if (e.isControlDown() && e.getID() != MouseEvent.MOUSE_DRAGGED && record.getColumnValue(column) != null) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setToolTipText("<html>Show referenced <b>" + column.getForeignKeyColumn().getDataset().getQualifiedName() + "</b> record<html>");
            } else {
                super.processMouseMotionEvent(e);
                setCursor(Cursor.getDefaultCursor());
                setToolTipText(null);
            }
        } else {
            super.processMouseMotionEvent(e);
        }
    }
    
    public DatasetFilterInput resolveForeignKeyRecord() {
        for (DBConstraint constraint : column.getConstraints()) {
            if (constraint.isForeignKey()) {
                DBDataset foreignKeyDataset = constraint.getForeignKeyConstraint().getDataset();
                DatasetFilterInput filterInput = new DatasetFilterInput(foreignKeyDataset);

                for (DBColumn constraintColumn : constraint.getColumns()) {
                    DBColumn foreignKeyColumn = ((DBColumn) constraintColumn.getUndisposedElement()).getForeignKeyColumn();
                    Object value = record.getColumnValue(column);
                    filterInput.setColumnValue(foreignKeyColumn, value);
                }
                return filterInput;
            }
        }
        return null;
    }
    
    private JDialog getEnclosingDialog() {
        return null;
    }
    
    MouseListener mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent event) {
            if (MouseUtil.isNavigationEvent(event)) {
                if (column.isForeignKey() && record.getColumnValue(column) != null) {
                    DatasetFilterInput filterInput = resolveForeignKeyRecord();
                    DatasetEditorManager datasetEditorManager = DatasetEditorManager.getInstance(column.getProject());
                    datasetEditorManager.navigateToRecord(filterInput, event);
                    event.consume();
                }
            }
        }        
        
    };
}
