package com.dci.intellij.dbn.ddl.ui;

import com.dci.intellij.dbn.common.ui.UIForm;
import com.dci.intellij.dbn.common.ui.UIFormImpl;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.util.List;

public class SelectDDLFileForm extends UIFormImpl implements UIForm {
    private JLabel objectLabel;
    private JPanel mainPanel;
    private JTextArea hintTextArea;
    private JList filesList;
    private JPanel headerPanel;

    public SelectDDLFileForm(DBSchemaObject object, List<VirtualFile> virtualFiles, String hint) {
        objectLabel.setText(object.getSchema().getName() + "." + object.getName());
        objectLabel.setIcon(object.getOriginalIcon());
        Project project = object.getProject();
        if (getEnvironmentSettings(project).getVisibilitySettings().getDialogHeaders().value()) {
            headerPanel.setBackground(object.getEnvironmentType().getColor());
        }
        hintTextArea.setText(hint);
        hintTextArea.setBackground(mainPanel.getBackground());
        hintTextArea.setFont(mainPanel.getFont());
        DefaultListModel listModel = new DefaultListModel();
        for (VirtualFile virtualFile : virtualFiles) {
            listModel.addElement(virtualFile);
        }
        filesList.setModel(listModel);
        filesList.setCellRenderer(new FileListCellRenderer(project));
        filesList.setSelectedIndex(0);
    }

    public Object[] getSelection() {
        return filesList.getSelectedValues();
    }

    public void selectAll() {
        filesList.setSelectionInterval(0, filesList.getModel().getSize() -1);
    }

    public void selectNone() {
        filesList.clearSelection();
    }

    public JPanel getComponent() {
        return mainPanel;
    }

    public void dispose() {
        super.dispose();
    }
}
