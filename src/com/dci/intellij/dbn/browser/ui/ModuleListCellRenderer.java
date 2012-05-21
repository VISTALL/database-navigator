package com.dci.intellij.dbn.browser.ui;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.connection.ModuleConnectionManager;
import com.dci.intellij.dbn.connection.ProjectConnectionManager;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;

import javax.swing.*;

public class ModuleListCellRenderer extends ColoredListCellRenderer {

    protected void customizeCellRenderer(JList list, Object value, int index, boolean selected, boolean hasFocus) {
        BrowserTreeElement treeElement = (BrowserTreeElement) value;
        setIcon(treeElement.getIcon(0));

        String displayName;
        if (treeElement instanceof ModuleConnectionManager) {
            ModuleConnectionManager connectionManager = (ModuleConnectionManager) treeElement;
            displayName = connectionManager.getModule().getName();
        } else if (treeElement instanceof ProjectConnectionManager) {
            displayName = "PROJECT";
        } else {
            displayName = treeElement.getPresentableText();
        }

        append(displayName, SimpleTextAttributes.REGULAR_ATTRIBUTES);
    }
}
