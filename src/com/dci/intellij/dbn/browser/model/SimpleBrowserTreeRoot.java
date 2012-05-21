package com.dci.intellij.dbn.browser.model;

import com.dci.intellij.dbn.code.sql.color.SQLTextAttributesKeys;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentType;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.GenericDatabaseElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FileStatus;

import javax.swing.*;
import java.util.List;

public class SimpleBrowserTreeRoot implements BrowserTreeElement{
    private List<ConnectionManager> connectionManagers;
    private Project project;

    public SimpleBrowserTreeRoot(Project project, List<ConnectionManager> connectionManagers) {
        this.project = project;
        this.connectionManagers = connectionManagers;
    }

    public Project getProject() {
        return project;
    }

    /**************************************************
     *              BrowserTreeElement            *
     **************************************************/
    public boolean isTreeStructureLoaded() {
        return true;
    }

    public void initTreeElement() {}

    public boolean canExpand() {
        return true;
    }

    public int getTreeDepth() {
        return 0;
    }

    public BrowserTreeElement getTreeParent() {
        return null;
    }

    public List<? extends BrowserTreeElement> getTreeChildren() {
        return connectionManagers;
    }

    public void rebuildTreeChildren() {
        for (ConnectionManager connectionManager : connectionManagers) {
            connectionManager.rebuildTreeChildren();
        }
    }

    public BrowserTreeElement getTreeChild(int index) {
        return connectionManagers.get(index);
    }

    public int getTreeChildCount() {
        return connectionManagers.size();
    }

    public boolean isLeafTreeElement() {
        return false;
    }

    public int getIndexOfTreeChild(BrowserTreeElement child) {
        return connectionManagers.indexOf(child);
    }

    public Icon getIcon(int flags) {
        return Icons.DATABASE_BROWSER;
    }

    public String getPresentableText() {
        return "Connection Managers";
    }

    public String getPresentableTextDetails() {
        return null;
    }

    public String getPresentableTextConditionalDetails() {
        return null;
    }

    /**************************************************
     *              GenericDatabaseElement            *
     **************************************************/
    public ConnectionHandler getConnectionHandler() {
        return null;
    }

    public GenericDatabaseElement getUndisposedElement() {
        return this;
    }

    public DynamicContent getDynamicContent(DynamicContentType dynamicContentType) {
        return null;
    }

    public boolean isDisposed() {
        return false;
    }

   /*********************************************************
    *                    ToolTipProvider                    *
    *********************************************************/
    public String getToolTip() {
        return null;
    }

    /*********************************************************
     *                  NavigationItem                       *
     *********************************************************/
    public String getName() {
        return getPresentableText();
    }
    
    public void navigate(boolean b) {}

    public boolean canNavigate() {
        return false;
    }

    public boolean canNavigateToSource() {
        return false;
    }

    public ItemPresentation getPresentation() {
        return this;
    }

    public FileStatus getFileStatus() {
        return FileStatus.NOT_CHANGED;
    }

    /*********************************************************
     *                 ItemPresentation                      *
     *********************************************************/
    public String getLocationString() {
        return null;
    }

    public Icon getIcon(boolean open) {
        return getIcon(0);
    }

    public TextAttributesKey getTextAttributesKey() {
        return SQLTextAttributesKeys.IDENTIFIER;
    }

    /*********************************************************
     *                       Disposable                      *
     *********************************************************/
    public void dispose() {
        connectionManagers = null;
    }
}
