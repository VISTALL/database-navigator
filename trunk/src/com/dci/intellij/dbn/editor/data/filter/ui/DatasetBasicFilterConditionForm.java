package com.dci.intellij.dbn.editor.data.filter.ui;

import com.dci.intellij.dbn.common.options.ui.ConfigurationEditorForm;
import com.dci.intellij.dbn.common.ui.ComboBoxSelectionKeyListener;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.data.editor.ui.DataEditorComponent;
import com.dci.intellij.dbn.data.editor.ui.TextFieldPopupType;
import com.dci.intellij.dbn.data.editor.ui.TextFieldWithPopup;
import com.dci.intellij.dbn.data.type.BasicDataType;
import com.dci.intellij.dbn.editor.data.filter.ConditionOperator;
import com.dci.intellij.dbn.editor.data.filter.DatasetBasicFilterCondition;
import com.dci.intellij.dbn.editor.data.filter.action.DeleteBasicFilterConditionAction;
import com.dci.intellij.dbn.editor.data.filter.action.EnableDisableBasicFilterConditionAction;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.SimpleTextAttributes;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatasetBasicFilterConditionForm extends ConfigurationEditorForm<DatasetBasicFilterCondition> {

    private JComboBox<DBColumn> columnComboBox;
    private JComboBox operatorComboBox;
    private JPanel actionsPanel;
    private JPanel mainPanel;
    private JPanel valueFieldPanel;
    private DataEditorComponent editorComponent;

    private boolean active = true;

    private boolean isAdjusting;
    private DatasetBasicFilterForm basicFilterForm;

    public DatasetBasicFilterConditionForm(DBDataset dataset, DatasetBasicFilterCondition condition) {
        super(condition);
        columnComboBox.setRenderer(cellRenderer);
        List<DBColumn> columns = new ArrayList<DBColumn>(dataset.getColumns()); 
        Collections.sort(columns);
        for (DBColumn column : columns) {
            columnComboBox.addItem(column);
        }

        ActionToolbar actionToolbar = ActionUtil.createActionToolbar(
                "DBNavigator.DataEditor.SimpleFilter.Condition", true,
                new EnableDisableBasicFilterConditionAction(this),
                new DeleteBasicFilterConditionAction(this));
        actionsPanel.add(actionToolbar.getComponent(), BorderLayout.CENTER);

        DBColumn column = dataset.getColumn(condition.getColumnName());
        BasicDataType dataType = null;
        if (column != null) {
            columnComboBox.setSelectedItem(column);
            dataType = column.getDataType().getNativeDataType().getBasicDataType();
        }


        updateOperatorsDropdown();
        setSelectedOperator(condition.getOperator());

        final TextFieldWithPopup textFieldWithPopup = new TextFieldWithPopup(dataset.getProject());
        textFieldWithPopup.createCalendarPopup(false);
        textFieldWithPopup.setPopupEnabled(TextFieldPopupType.CALENDAR, dataType == BasicDataType.DATE_TIME);
        
        valueFieldPanel.add(textFieldWithPopup, BorderLayout.CENTER);
        editorComponent = textFieldWithPopup;

        JTextField valueTextField = editorComponent.getTextField();
        valueTextField.setText(condition.getValue());
        setActive(condition.isActive());

        CompoundListener listener = new CompoundListener();
        columnComboBox.addActionListener(listener);
        operatorComboBox.addActionListener(listener);
        valueTextField.getDocument().addDocumentListener(listener);
        valueTextField.addKeyListener(ComboBoxSelectionKeyListener.create(columnComboBox, false));
        valueTextField.addKeyListener(ComboBoxSelectionKeyListener.create(operatorComboBox, true));

        updateValueTextField();
        columnComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DBColumn column = (DBColumn) columnComboBox.getSelectedItem();
                BasicDataType dataType = column.getDataType().getNativeDataType().getBasicDataType();
                textFieldWithPopup.setPopupEnabled(TextFieldPopupType.CALENDAR, dataType == BasicDataType.DATE_TIME);
            }
        });

        valueTextField.setToolTipText("<html>While editing value, <br> " +
                "press <b>Up/Down</b> keys to change column or <br> " +
                "press <b>Ctrl-Up/Ctrl-Down</b> keys to change operator</html>");

    }

    public void focus() {
        JTextField valueTextField = editorComponent.getTextField();
        valueTextField.selectAll();
        valueTextField.grabFocus();
    }

    private class CompoundListener extends DocumentAdapter implements ActionListener {
        protected void textChanged(DocumentEvent e) {
            basicFilterForm.updateNameAndPreview();
        }

        public void actionPerformed(ActionEvent e) {
            if (!isAdjusting) {
                basicFilterForm.updateNameAndPreview();
                if (e.getSource() == columnComboBox) {
                    updateOperatorsDropdown();
                }
                else if (e.getSource() == operatorComboBox) {
                    updateValueTextField();
                }
            }
        }
    }

    public void setBasicFilterPanel(DatasetBasicFilterForm basicFilterForm) {
        this.basicFilterForm = basicFilterForm;
    }

    public DBColumn getSelectedColumn() {
        DBColumn column = (DBColumn) columnComboBox.getSelectedItem();
        return (DBColumn) column.getUndisposedElement();
    }

    public ConditionOperator getSelectedOperator() {
        return (ConditionOperator) operatorComboBox.getSelectedItem();
    }

    public void setSelectedOperator(String operator) {
        if (operator != null) {
            ComboBoxModel model = operatorComboBox.getModel();
            for (int i=0; i< model.getSize(); i++) {
                ConditionOperator conditionOperator = (ConditionOperator) model.getElementAt(i);
                if (conditionOperator.getText().equals(operator)) {
                    operatorComboBox.setSelectedIndex(i);
                    return;
                }
            }
        }
        operatorComboBox.setSelectedIndex(0);
    }


    public String getValue() {
        return editorComponent.getText();
    }

    public DatasetBasicFilterCondition getCondition() {
        return getConfiguration();
    }

    public DatasetBasicFilterCondition createCondition() {
        return new DatasetBasicFilterCondition(
                basicFilterForm.getConfiguration(),
                getSelectedColumn().getName(),
                editorComponent.getText(), getSelectedOperator(),
                active);
    }

    public void remove() {
        DatasetBasicFilterCondition condition = getConfiguration();
        DatasetBasicFilterForm settingsEditor = (DatasetBasicFilterForm) condition.getFilter().getSettingsEditor();
        settingsEditor.removeConditionPanel(this);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        columnComboBox.setEnabled(active);
        operatorComboBox.setEnabled(active);
        editorComponent.getTextField().setEnabled(active);
        if (basicFilterForm != null) {
            basicFilterForm.updateNameAndPreview();
        }
    }

    private ListCellRenderer cellRenderer = new ColoredListCellRenderer() {
        protected void customizeCellRenderer(JList list, Object value, int index, boolean selected, boolean hasFocus) {
            DBColumn column = (DBColumn) value;
            if (column != null) {
                setIcon(column.getIcon());
                append(column.getName(), active ? SimpleTextAttributes.REGULAR_ATTRIBUTES : SimpleTextAttributes.GRAYED_ATTRIBUTES);
            }
        }
    };

    public JPanel getComponent() {
        return mainPanel;
    }

    public void applyChanges() throws ConfigurationException {
        DatasetBasicFilterCondition condition = getConfiguration();
        DBColumn column = getSelectedColumn();
        ConditionOperator operator = getSelectedOperator();
        String value = editorComponent.getText();

        condition.setColumnName(column.getName());
        condition.setOperator(operator == null ? "" : operator.toString());
        condition.setValue(value == null ? "" : value);
        condition.setActive(isActive());
    }

    private void updateOperatorsDropdown() {
        DBColumn column = getSelectedColumn();
        ConditionOperator[] conditionOperators =  ConditionOperator.getConditionOperators(column.getDataType().getTypeClass());
        ConditionOperator selectedOperator = getSelectedOperator();

        isAdjusting = true;
        operatorComboBox.removeAllItems();
        for (ConditionOperator conditionOperator : conditionOperators) {
            operatorComboBox.addItem(conditionOperator);
        }

        isAdjusting = false;
        setSelectedOperator(selectedOperator == null ? null : selectedOperator.getText());
    }

    private void updateValueTextField() {
        JTextField valueTextField = editorComponent.getTextField();
        ConditionOperator selectedOperator = getSelectedOperator();
        valueTextField.setEnabled(!selectedOperator.isFinal() && active);
        if (selectedOperator.isFinal()) valueTextField.setText(null);
    }

    public void resetChanges() {

    }

    @Override
    public void dispose() {
        super.dispose();
        editorComponent = null;
        basicFilterForm = null;
    }


}
