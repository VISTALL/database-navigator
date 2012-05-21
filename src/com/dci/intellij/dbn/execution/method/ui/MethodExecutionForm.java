package com.dci.intellij.dbn.execution.method.ui;

import com.dci.intellij.dbn.common.dispose.DisposeUtil;
import com.dci.intellij.dbn.common.ui.UIForm;
import com.dci.intellij.dbn.common.ui.UIFormImpl;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.DatabaseCompatibilityInterface;
import com.dci.intellij.dbn.database.DatabaseFeature;
import com.dci.intellij.dbn.execution.method.MethodExecutionInput;
import com.dci.intellij.dbn.execution.method.action.SetExecutionSchemaComboBoxAction;
import com.dci.intellij.dbn.object.DBArgument;
import com.dci.intellij.dbn.object.DBMethod;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.ui.DocumentAdapter;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MethodExecutionForm extends UIFormImpl implements UIForm {
    private JPanel mainPanel;
    private JPanel argumentsPanel;
    private JPanel headerPanel;
    private JSeparator topSeparator;
    private JPanel executionSchemaActionPanel;
    private JLabel executionSchemaLabel;
    private JLabel noArgumentsLabel;
    private JCheckBox usePoolConnectionCheckBox;
    private JCheckBox commitCheckBox;
    private JSeparator spacer;
    private JLabel connectionLabel;
    private JScrollPane argumentsScrollPane;


    private List<MethodExecutionArgumentForm> argumentForms = new ArrayList<MethodExecutionArgumentForm>();
    private MethodExecutionInput executionInput;
    private Set<ChangeListener> changeListeners = new HashSet<ChangeListener>();
    private boolean debug;

    public MethodExecutionForm(MethodExecutionInput executionInput, boolean showHeader, boolean debug) {
        this.executionInput = executionInput;
        this.debug = debug;
        DBMethod method = executionInput.getMethod();

        ConnectionHandler connectionHandler = executionInput.getConnectionHandler();
        DatabaseCompatibilityInterface compatibilityInterface = DatabaseCompatibilityInterface.getInstance(connectionHandler);
        if (compatibilityInterface.supportsFeature(DatabaseFeature.AUTHID_METHOD_EXECUTION)) {
            ActionToolbar actionToolbar = ActionUtil.createActionToolbar("", true, new SetExecutionSchemaComboBoxAction(executionInput));
            executionSchemaActionPanel.add(actionToolbar.getComponent(), BorderLayout.CENTER);
        } else {
            executionSchemaActionPanel.setVisible(false);
            executionSchemaLabel.setVisible(false);
        }
        connectionLabel.setText(connectionHandler.getPresentableText());
        connectionLabel.setIcon(connectionHandler.getIcon());


        //objectPanel.add(new ObjectDetailsPanel(method).getComponent(), BorderLayout.NORTH);

        if (showHeader) {
            JLabel label = new JLabel(method.getQualifiedName(), method.getOriginalIcon(), SwingConstants.LEFT);
            label.setMinimumSize(new Dimension(1, (int) label.getMinimumSize().getHeight()));
            headerPanel.add(label);
            if (getEnvironmentSettings(method.getProject()).getVisibilitySettings().getDialogHeaders().value()) {
                headerPanel.setBackground(method.getEnvironmentType().getColor());
            }
        }
        headerPanel.setVisible(showHeader);

        argumentsPanel.setLayout(new BoxLayout(argumentsPanel, BoxLayout.Y_AXIS));
        int[] metrics = new int[]{0, 0};

        //topSeparator.setVisible(false);
        spacer.setVisible(false);
        List<DBArgument> arguments = new ArrayList(method.getArguments());
        noArgumentsLabel.setVisible(arguments.size() == 0);
        for (DBArgument argument: arguments) {
            if (argument.isInput()) {
                spacer.setVisible(true);
                metrics = addArgumentPanel(argument, metrics);
                //topSeparator.setVisible(true);
            }
        }

        for (MethodExecutionArgumentForm component : argumentForms) {
            component.adjustMetrics(metrics);
        }

        if (argumentForms.size() > 0) {
            argumentsScrollPane.getVerticalScrollBar().setUnitIncrement(argumentForms.get(0).getScrollUnitIncrement());
        }


        Dimension preferredSize = mainPanel.getPreferredSize();
        int width = (int) preferredSize.getWidth() + 24;
        int height = (int) Math.min(preferredSize.getHeight(), 380);
        mainPanel.setPreferredSize(new Dimension(width, height));
        commitCheckBox.setSelected(executionInput.isCommitAfterExecution());
        usePoolConnectionCheckBox.setSelected(executionInput.isUsePoolConnection());

        for (MethodExecutionArgumentForm argumentComponent : argumentForms){
            argumentComponent.addDocumentListener(documentListener);
        }
        commitCheckBox.addActionListener(actionListener);
        usePoolConnectionCheckBox.addActionListener(actionListener);
        usePoolConnectionCheckBox.setEnabled(!debug);
    }

    public void setExecutionInput(MethodExecutionInput executionInput) {
        if (!executionInput.equals(this.executionInput)) {
            System.out.println("");
        }
        this.executionInput = executionInput;
    }

    public MethodExecutionInput getExecutionInput() {
        return executionInput;
    }

    public JPanel getComponent() {
        return mainPanel;
    }

    private int[] addArgumentPanel(DBArgument argument, int[] gridMetrics) {
        MethodExecutionArgumentForm argumentComponent = new MethodExecutionArgumentForm(argument, this);
        argumentsPanel.add(argumentComponent.getComponent());
        argumentForms.add(argumentComponent);
        return argumentComponent.getMetrics(gridMetrics);
   }

    public void updateExecutionInput() {
        for (MethodExecutionArgumentForm argumentComponent : argumentForms) {
            argumentComponent.updateExecutionInput();
        }

        executionInput.setUsePoolConnection(usePoolConnectionCheckBox.isSelected());
        executionInput.setCommitAfterExecution(commitCheckBox.isSelected());
        //DBSchema schema = (DBSchema) schemaList.getSelectedValue();
        //executionInput.setExecutionSchema(schema);
    }

    public void addChangeListener(ChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    private DocumentListener documentListener = new DocumentAdapter() {
        protected void textChanged(DocumentEvent e) {
            notifyChangeListeners();
        }
    };

    private ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            notifyChangeListeners();
        }
    };

    private void notifyChangeListeners() {
        if (changeListeners != null) {
            for (ChangeListener changeListener : changeListeners) {
                changeListener.stateChanged(new ChangeEvent(this));
            }
        }
    }

    public void touch() {
        commitCheckBox.setSelected(!commitCheckBox.isSelected());    
        commitCheckBox.setSelected(!commitCheckBox.isSelected());
    }


    public void dispose() {
        super.dispose();
        DisposeUtil.disposeCollection(argumentForms);
        changeListeners.clear();
        argumentForms = null;
        executionInput = null;
        changeListeners = null;
    }
}
