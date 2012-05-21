package com.dci.intellij.dbn.browser.model;

import com.dci.intellij.dbn.connection.ConnectionManager;
import com.intellij.openapi.project.Project;

import java.util.List;

public class SimpleBrowserTreeModel extends BrowserTreeModel {
    public SimpleBrowserTreeModel(Project project, List<ConnectionManager> connectionManagers) {
        super(new SimpleBrowserTreeRoot(project, connectionManagers));
    }


    public void dispose() {
        super.dispose();
    }
}
