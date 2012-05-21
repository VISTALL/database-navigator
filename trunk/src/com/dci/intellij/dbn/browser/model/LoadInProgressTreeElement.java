package com.dci.intellij.dbn.browser.model;

import com.dci.intellij.dbn.browser.DatabaseBrowserManager;
import com.dci.intellij.dbn.code.sql.color.SQLTextAttributesKeys;
import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentType;
import com.dci.intellij.dbn.common.ui.tree.TreeUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.GenericDatabaseElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vcs.FileStatus;

import javax.swing.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LoadInProgressTreeElement implements BrowserTreeElement {
    private BrowserTreeElement parent;
    private ImageRefreshTask imageRefreshTask;
    private int iconIndex;

    private Timer reloader = new Timer("Load in progress tree leaf reloader");
    private static Icon[] icons = new Icon[12];
    static {
        for (int i = 0; i <= 12 - 1; i++) {
            icons[i] = IconLoader.getIcon("/process/step_" + (i + 1) + ".png");
        }
    }

    public LoadInProgressTreeElement(BrowserTreeElement parent) {
        this.parent = parent;
        imageRefreshTask = new ImageRefreshTask();
        reloader.schedule(imageRefreshTask, 0, 100);
    }

    public boolean isTreeStructureLoaded() {
        return true;
    }

    public void initTreeElement() {}

    public boolean canExpand() {
        return false;
    }

    public int getTreeDepth() {
        return parent.getTreeDepth() + 1;
    }

    public BrowserTreeElement getTreeChild(int index) {
        return null;
    }

    public BrowserTreeElement getTreeParent() {
        return parent;
    }

    public List<? extends BrowserTreeElement> getTreeChildren() {
        return null;
    }

    public void rebuildTreeChildren() {
    }

    public int getTreeChildCount() {
        return 0;
    }

    public boolean isLeafTreeElement() {
        return true;
    }

    public int getIndexOfTreeChild(BrowserTreeElement child) {
        return -1;
    }

    public Icon getIcon(int flags) {
        iconIndex++;
        if (iconIndex == icons.length) iconIndex = 0;
        return icons[iconIndex];
    }

    public String getPresentableText() {
        return "Loading...";
    }

    public String getPresentableTextDetails() {
        return null;
    }

    public String getPresentableTextConditionalDetails() {
        return null;
    }

    public ConnectionHandler getConnectionHandler() {
        return parent.getConnectionHandler();
    }

    public Project getProject() {
        return parent.getProject();
    }

    public GenericDatabaseElement getUndisposedElement() {
        return this;
    }

    public DynamicContent getDynamicContent(DynamicContentType dynamicContentType) {
        return null;
    }

    public boolean isDisposed() {
        return parent.isDisposed();
    }

    /*********************************************************
    *                    ItemPresentation                    *
    *********************************************************/
    public String getLocationString() {
        return null;
    }

    public Icon getIcon(boolean open) {
        return null;
    }

    public TextAttributesKey getTextAttributesKey() {
        return SQLTextAttributesKeys.IDENTIFIER;
    }

    /*********************************************************
    *                    NavigationItem                      *
    *********************************************************/
    public void navigate(boolean requestFocus) {}
    public boolean canNavigate() { return false;}
    public boolean canNavigateToSource() {return false;}

    public String getName() {
        return null;
    }

    public ItemPresentation getPresentation() {
        return this;
    }

    public FileStatus getFileStatus() {
        return FileStatus.NOT_CHANGED;
    }

    /*********************************************************
    *                    ToolTipProvider                    *
    *********************************************************/
    public String getToolTip() {
        return null;
    }

    public void dispose() {
        imageRefreshTask.cancel();
    }

    private class ImageRefreshTask extends TimerTask {
        int iterations = 0;
        public void run() {
            // do not allow more than 2000 iterations.
            if (iterations > 2000) {
                cancel();
            }
            iterations++;
            if (!isDisposed()) {
                DatabaseBrowserManager.updateTree(LoadInProgressTreeElement.this, TreeUtil.NODES_CHANGED);
            }
        }
    }
}
