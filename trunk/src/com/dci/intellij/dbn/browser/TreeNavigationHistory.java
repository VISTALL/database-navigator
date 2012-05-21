package com.dci.intellij.dbn.browser;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.browser.options.DatabaseBrowserSettings;

import java.util.ArrayList;
import java.util.List;

public class TreeNavigationHistory{
    private List<BrowserTreeElement> history = new ArrayList<BrowserTreeElement>();
    private int offset;

    public void add(BrowserTreeElement treeElement) {
        if (history.size() > 0 && treeElement == history.get(offset)) {
            return;
        }
        while (history.size() - 1  > offset) {
            history.remove(offset);
        }

        DatabaseBrowserSettings browserSettings = DatabaseBrowserSettings.getInstance(treeElement.getProject());

        int historySize = browserSettings.getGeneralSettings().getNavigationHistorySize().value();
        while (history.size() > historySize) {
            history.remove(0);
        }
        history.add(treeElement);
        offset = history.size() -1;
    }

    public void clear() {
        history.clear();
    }

    public boolean hasNext() {
        return offset < history.size()-1;
    }

    public boolean hasPrevious() {
        return offset > 0;
    }

    public BrowserTreeElement next() {
        offset = offset + 1;
        return history.get(offset);
    }

    public BrowserTreeElement previous() {
        offset = offset -1;
        return history.get(offset);
    }

}
