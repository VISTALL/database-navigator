package com.dci.intellij.dbn.browser.model;

import com.dci.intellij.dbn.browser.ui.ToolTipProvider;
import com.dci.intellij.dbn.connection.GenericDatabaseElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.Disposable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public interface BrowserTreeElement extends NavigationItem, ItemPresentation, ToolTipProvider, Disposable, GenericDatabaseElement {

    List<BrowserTreeElement> EMPTY_LIST = new ArrayList<BrowserTreeElement>();

    void initTreeElement();

    boolean canExpand();

    int getTreeDepth();

    boolean isTreeStructureLoaded();

    BrowserTreeElement getTreeChild(int index);

    BrowserTreeElement getTreeParent();

    List<? extends BrowserTreeElement> getTreeChildren();

    void rebuildTreeChildren();

    int getTreeChildCount();

    boolean isLeafTreeElement();

    int getIndexOfTreeChild(BrowserTreeElement child);

    Icon getIcon(int flags);

    String getPresentableText();

    String getPresentableTextDetails();

    String getPresentableTextConditionalDetails();

    boolean isDisposed();
}
