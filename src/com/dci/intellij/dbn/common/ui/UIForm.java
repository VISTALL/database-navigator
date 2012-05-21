package com.dci.intellij.dbn.common.ui;

import com.intellij.openapi.Disposable;

import javax.swing.JComponent;

public interface UIForm extends Disposable {
    JComponent getComponent();
    boolean isDisposed();
}
