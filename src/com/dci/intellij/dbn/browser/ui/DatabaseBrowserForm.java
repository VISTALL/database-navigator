package com.dci.intellij.dbn.browser.ui;

import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.common.ui.UIFormImpl;
import com.intellij.openapi.project.Project;

public abstract class DatabaseBrowserForm extends UIFormImpl {
    private Project project;

    protected DatabaseBrowserForm(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public abstract DatabaseBrowserTree getBrowserTree();

    public abstract void selectElement(BrowserTreeNode treeNode, boolean requestFocus);

    public abstract void updateTree();

    public abstract void rebuild();

    @Override
    public void dispose() {
        project = null;
    }
}
