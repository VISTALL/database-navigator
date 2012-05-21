package com.dci.intellij.dbn.browser.ui;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.common.ui.UIForm;

public interface DatabaseBrowserForm extends UIForm {
    DatabaseBrowserTree getBrowserTree();

    void selectElement(BrowserTreeElement treeElement, boolean requestFocus);

    void updateTreeNode(BrowserTreeElement treeElement, int eventType);

    void updateTree();

    void repaintTree();

    void rebuild();
}
