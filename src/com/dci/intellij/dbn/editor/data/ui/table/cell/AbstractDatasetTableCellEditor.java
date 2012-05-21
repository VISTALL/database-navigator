package com.dci.intellij.dbn.editor.data.ui.table.cell;

import com.dci.intellij.dbn.common.locale.Formatter;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.data.editor.ui.DataEditorComponent;
import com.dci.intellij.dbn.data.type.DBDataType;
import com.dci.intellij.dbn.editor.data.options.DataEditorGeneralSettings;
import com.dci.intellij.dbn.editor.data.options.DataEditorSettings;
import com.dci.intellij.dbn.editor.data.ui.table.model.DatasetEditorModelCell;
import com.intellij.openapi.project.Project;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.text.ParseException;
import java.util.EventObject;

public abstract class AbstractDatasetTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    private DataEditorComponent editorComponent;
    private JTextField textField;
    private int clickCountToStart = 1;
    private DatasetEditorModelCell cell;
    protected DataEditorSettings settings;

    public AbstractDatasetTableCellEditor(DataEditorComponent editorComponent, Project project) {
        this.editorComponent = editorComponent;
        this.textField = editorComponent.getTextField();
        this.settings = DataEditorSettings.getInstance(project);

        this.clickCountToStart = 2;
        textField.addActionListener(new EditorDelegate());
    }

    public JComponent getEditorComponent() {
        return (JComponent) editorComponent;
    }

    public JTextField getTextField() {
        return textField;
    }

    public boolean isCellEditable(EventObject event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            return mouseEvent.getClickCount() >= clickCountToStart;
        }
        return true;
    }

    public boolean shouldSelectCell(EventObject event) {
        return true;
    }

    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,  int column) {
        cell = (DatasetEditorModelCell) value;
        if (cell != null) {
            Object userValue = cell.getUserValue();
            if (userValue instanceof String) {
                textField.setText((String) userValue);
            } else {
                String stringValue = getFormatter().formatObject(userValue);
                textField.setText(stringValue);
            }
        } else {
            textField.setText("");
        }
        return (Component) editorComponent;
    }

    public Object getCellEditorValue() {
        DBDataType dataType = cell.getColumnInfo().getDataType();
        Class clazz = dataType.getTypeClass();
        try {
            String textValue = textField.getText();
            
            
            boolean trim = true;
            if (clazz == String.class) {
                DataEditorGeneralSettings generalSettings = settings.getGeneralSettings();
                boolean isEmpty = StringUtil.isEmptyOrSpaces(textValue);
                trim = (isEmpty && generalSettings.getConvertEmptyStringsToNull().value()) ||
                       (!isEmpty && generalSettings.getTrimWhitespaces().value());
            }
            
            if (trim) textValue = textValue.trim();
            
            if (textValue.length() > 0) {
                Object value = getFormatter().parseObject(clazz, textValue);
                return dataType.getNativeDataType().getDataTypeDefinition().convert(value);
            } else {
                return null;
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Can not convert " + textField.getText() + " to " + dataType.getName());
        }
    }

    public Object getCellEditorValueLenient() {
        return textField.getText().trim();        
    }

    private Formatter getFormatter() {
        Project project = cell.getRow().getModel().getDataset().getProject();
        return Formatter.getInstance(project);
    }


    /********************************************************
     *                    EditorDelegate                    *
     ********************************************************/
    protected class EditorDelegate implements ActionListener, ItemListener, Serializable {

        public void actionPerformed(ActionEvent e) {
            AbstractDatasetTableCellEditor.this.stopCellEditing();
        }

        public void itemStateChanged(ItemEvent e) {
            AbstractDatasetTableCellEditor.this.stopCellEditing();
        }
    }
}
