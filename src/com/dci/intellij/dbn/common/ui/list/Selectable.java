package com.dci.intellij.dbn.common.ui.list;

import javax.swing.*;

public interface Selectable {
    Icon getIcon();
    String getName();
    String getError();
    boolean isSelected();
    boolean isMasterSelected();
    void setSelected(boolean enabled);
}
