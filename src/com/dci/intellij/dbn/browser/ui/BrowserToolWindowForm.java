package com.dci.intellij.dbn.browser.ui;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.browser.options.BrowserDisplayMode;
import com.dci.intellij.dbn.browser.options.DatabaseBrowserSettings;
import com.dci.intellij.dbn.common.event.EventManager;
import com.dci.intellij.dbn.common.ui.UIForm;
import com.dci.intellij.dbn.common.ui.UIFormImpl;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.properties.ui.ObjectPropertiesForm;
import com.dci.intellij.dbn.options.ProjectSettingsChangeListener;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.ui.GuiUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class BrowserToolWindowForm extends UIFormImpl implements UIForm, ProjectSettingsChangeListener {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JPanel browserPanel;
    private JPanel closeActionPanel;
    private JPanel objectPropertiesPanel;
    private DatabaseBrowserForm browserForm;

    private BrowserDisplayMode displayMode;
    private Project project;
    private ObjectPropertiesForm objectPropertiesForm;

    public BrowserToolWindowForm(Project project) {
        this.project = project;
        //toolWindow.setIcon(dbBrowser.getIcon(0));
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);

        displayMode = DatabaseBrowserSettings.getInstance(project).getGeneralSettings().getDisplayMode();
        updateBrowserPanel();

        ActionToolbar actionToolbar = ActionUtil.createActionToolbar("", true, "DBNavigator.ActionGroup.Browser.Controls");

        actionsPanel.add(actionToolbar.getComponent());

        /*ActionToolbar objectPropertiesActionToolbar = ActionUtil.createActionToolbar("", false, "DBNavigator.ActionGroup.Browser.ObjectProperties");
        closeActionPanel.add(objectPropertiesActionToolbar.getComponent(), BorderLayout.CENTER);*/

        objectPropertiesPanel.setVisible(browserManager.getShowObjectProperties().value());
        objectPropertiesForm = new ObjectPropertiesForm(project);
        objectPropertiesPanel.add(objectPropertiesForm.getComponent());
        GuiUtils.replaceJSplitPaneWithIDEASplitter(mainPanel);

        EventManager.subscribe(project, ProjectSettingsChangeListener.TOPIC, this);
    }

    public DatabaseBrowserTree getBrowserTree(ConnectionHandler connectionHandler) {
        if (browserForm instanceof TabbedBrowserForm) {
            TabbedBrowserForm tabbedBrowserForm = (TabbedBrowserForm) browserForm;
            return tabbedBrowserForm.getBrowserTree(connectionHandler);
        }

        if (browserForm instanceof SimpleBrowserForm) {
            return browserForm.getBrowserTree();
        }

        return null;
    }



    public void showObjectProperties() {
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
        DatabaseBrowserTree activeBrowserTree = browserManager.getActiveBrowserTree();
        BrowserTreeElement treeElement = activeBrowserTree == null ? null : activeBrowserTree.getSelectedElement();
        if (treeElement instanceof DBObject) {
            DBObject object = (DBObject) treeElement;
            objectPropertiesForm.setObject(object);
        }

        objectPropertiesPanel.setVisible(true);
    }

    public void hideObjectProperties() {
        objectPropertiesPanel.setVisible(false);
    }


    public void projectSettingsChanged(Project project) {
        BrowserDisplayMode configDisplayMode = getConfigDisplayMode();
        if (displayMode != configDisplayMode) {
            browserForm.dispose();
            displayMode = configDisplayMode;
            updateBrowserPanel();
            browserPanel.updateUI();
        }
    }

    private void updateBrowserPanel() {
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
        browserForm =
                displayMode == BrowserDisplayMode.TABBED ? new TabbedBrowserForm(browserManager) :
                displayMode == BrowserDisplayMode.SIMPLE ? new SimpleBrowserForm(browserManager) : null;

        browserPanel.removeAll();
        browserPanel.add(browserForm.getComponent(), BorderLayout.CENTER);
    }

    private BrowserDisplayMode getConfigDisplayMode() {
        return DatabaseBrowserSettings.getInstance(project).getGeneralSettings().getDisplayMode();
    }

    @Nullable
    public DatabaseBrowserTree getActiveBrowserTree() {
        return browserForm.getBrowserTree();
    }

    public DatabaseBrowserForm getBrowserForm() {
        return browserForm;
    }

    public JPanel getComponent() {
        return mainPanel;
    }

    public void dispose() {
        super.dispose();
        EventManager.unsubscribe(project, this);
        objectPropertiesForm.dispose();
        objectPropertiesForm = null;
        browserForm.dispose();
        browserForm = null;
        project = null;
    }
}
