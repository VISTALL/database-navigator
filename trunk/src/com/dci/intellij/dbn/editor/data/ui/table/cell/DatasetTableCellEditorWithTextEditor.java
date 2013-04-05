package com.dci.intellij.dbn.editor.data.ui.table.cell;

import com.dci.intellij.dbn.common.ui.KeyUtil;
import com.dci.intellij.dbn.data.editor.ui.TextFieldWithTextEditor;
import com.dci.intellij.dbn.data.model.ColumnInfo;
import com.dci.intellij.dbn.data.type.DBDataType;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorModelCell;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.project.Project;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.KeyEvent;

public class DatasetTableCellEditorWithTextEditor extends DatasetTableCellEditor {
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

    public DatasetTableCellEditorWithTextEditor(DatasetEditorTable table) {
        super(table, createTextField(table.getDataset().getProject()));
        TextFieldWithTextEditor editorComponent = getEditorComponent();
        JTextField textField = editorComponent.getTextField();
        textField.setBorder(new EmptyBorder(EMPTY_INSETS));
        JButton button = editorComponent.getButton();
        button.setMargin(EMPTY_INSETS);
        button.setBackground(textField.getBackground());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static TextFieldWithTextEditor createTextField(Project project) {
        return new TextFieldWithTextEditor(project) {
            @Override
            public void setEditable(boolean editable) {
                super.setEditable(editable);
                Color background = getTextField().getBackground();
                setBackground(background);
                getButton().setBackground(background);
            }
        };
    }

    public TextFieldWithTextEditor getEditorComponent() {
        return (TextFieldWithTextEditor) super.getEditorComponent();
    }

    @Override
    public void prepareEditor(final DatasetEditorModelCell cell) {
        getEditorComponent().setUserValueHolder(cell);
        setCell(cell);
        ColumnInfo columnInfo = cell.getColumnInfo();
        DBDataType dataType = columnInfo.getDataType();
        if (dataType.isNative()) {
            highlight(cell.hasError() ? HIGHLIGHT_TYPE_ERROR : HIGHLIGHT_TYPE_NONE);
            if (dataType.getNativeDataType().isLOB()) {
                setEditable(false);
            } else {
                String userValue = (String) cell.getUserValue();
                setEditable(userValue == null || (userValue.length() < 1000 && userValue.indexOf('\n') == -1));
            }
            selectText(getTextField());
        }
    }

    @Override
    public void setEditable(boolean editable) {
        TextFieldWithTextEditor editorComponent = getEditorComponent();
        editorComponent.setEditable(editable);
    }

    /********************************************************
     *                      KeyListener                     *
     ********************************************************/
    public void keyPressed(KeyEvent keyEvent) {
        Shortcut[] shortcuts = KeyUtil.getShortcuts(IdeActions.ACTION_SHOW_INTENTION_ACTIONS);
        if (KeyUtil.match(shortcuts, keyEvent)) {
            getEditorComponent().openEditor();
        } else {
            super.keyPressed(keyEvent);
        }
    }

}
