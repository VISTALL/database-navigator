package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.ui.DBNComboBoxAction;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.EditorUtil;
import com.dci.intellij.dbn.common.util.NamingUtil;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilter;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterGroup;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterManager;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;

public class SelectDatasetFilterComboBoxAction extends DBNComboBoxAction {
    private DatasetEditor datasetEditor;
    public SelectDatasetFilterComboBoxAction() {
        Presentation presentation = getTemplatePresentation();
        presentation.setText("No Filter");
        presentation.setIcon(Icons.DATASET_FILTER_EMPTY);
    }

    @Override
    public JComponent createCustomComponent(Presentation presentation) {
        return super.createCustomComponent(presentation);
    }

    @NotNull
    protected DefaultActionGroup createPopupActionGroup(JComponent button) {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        if (datasetEditor != null) {
            DBDataset dataset = datasetEditor.getDataset();
            OpenFilterSettingsAction openFilterSettingsAction = new OpenFilterSettingsAction(datasetEditor);
            openFilterSettingsAction.setInjectedContext(true);
            actionGroup.add(openFilterSettingsAction);
            actionGroup.addSeparator();
            actionGroup.add(new SelectDatasetFilterAction(dataset, DatasetFilterManager.EMPTY_FILTER));
            actionGroup.addSeparator();

            DatasetFilterManager filterManager = DatasetFilterManager.getInstance(dataset.getProject());
            DatasetFilterGroup filterGroup = filterManager.getFilterGroup(dataset);
            for (DatasetFilter filter : filterGroup.getFilters()) {
                actionGroup.add(new SelectDatasetFilterAction(dataset, filter));
            }
        }
        return actionGroup;
    }

    @Override
    public void update(AnActionEvent e) {
        Project project = ActionUtil.getProject(e);
        datasetEditor = (DatasetEditor) EditorUtil.getFileEditor(project, e.getPlace());

        Presentation presentation = e.getPresentation();
        boolean enabled =
                datasetEditor != null &&
                datasetEditor.getActiveConnection().isConnected() &&
                !datasetEditor.isInserting() &&
                !datasetEditor.isLoading();
        if (datasetEditor != null) {
            DBDataset dataset = datasetEditor.getDataset();

            DatasetFilterManager filterManager = DatasetFilterManager.getInstance(dataset.getProject());
            DatasetFilter activeFilter = filterManager.getActiveFilter(dataset);

            if (activeFilter == null) {
                presentation.setText("No Filter");
                presentation.setIcon(Icons.DATASET_FILTER_EMPTY);
            } else {
                //e.getPresentation().setText(activeFilter.getName());
                presentation.setText(NamingUtil.enhanceNameForDisplay(activeFilter.getName()));
                presentation.setIcon(activeFilter.getIcon());
            }
        }

        //if (!enabled) presentation.setIcon(null);
        presentation.setEnabled(enabled);
    }
}
