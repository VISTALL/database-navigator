package com.dci.intellij.dbn.browser;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.common.filter.Filter;
import com.dci.intellij.dbn.object.common.DBObjectBundle;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

public class DatabaseBrowserUtils {
    public static TreePath createTreePath(BrowserTreeElement treeElement) {
        boolean isTabbedMode = DatabaseBrowserManager.getInstance(treeElement.getProject()).isTabbedMode();

        int treeDepth = treeElement.getTreeDepth();
        BrowserTreeElement[] path = new BrowserTreeElement[isTabbedMode ? treeDepth -1 : treeDepth + 1];
        while (treeElement != null) {
            treeDepth = treeElement.getTreeDepth();
            path[isTabbedMode ? treeDepth -2 : treeDepth] = treeElement;
            if (treeElement instanceof DatabaseBrowserManager) break;
            if (isTabbedMode && treeElement instanceof DBObjectBundle) break;
            treeElement = treeElement.getTreeParent();
        }
        return new TreePath(path);
    }

    public static boolean treeVisibilityChanged(
            List<BrowserTreeElement> possibleTreeElements,
            List<BrowserTreeElement> actualTreeElements,
            Filter<BrowserTreeElement> filter) {
        for (BrowserTreeElement treeElement : possibleTreeElements) {
            if (treeElement != null) {
                if (filter.accepts(treeElement)) {
                    if (!actualTreeElements.contains(treeElement)) return true;
                } else {
                    if (actualTreeElements.contains(treeElement)) return true;
                }
            }
        }
        return false;
    }

    public static List<BrowserTreeElement> createList(BrowserTreeElement ... treeElements) {
        List<BrowserTreeElement> treeElementList = new ArrayList<BrowserTreeElement>(treeElements.length);
        for (BrowserTreeElement treeElement : treeElements) {
            if (treeElement != null) {
                treeElementList.add(treeElement);
            }
        }
        return treeElementList;
    }
}
