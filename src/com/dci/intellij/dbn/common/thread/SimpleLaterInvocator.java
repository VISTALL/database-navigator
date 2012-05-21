package com.dci.intellij.dbn.common.thread;

import com.intellij.openapi.application.ApplicationManager;

public abstract class SimpleLaterInvocator implements Runnable{
    public void start() {
        ApplicationManager.getApplication().invokeLater(this/*, ModalityState.NON_MODAL*/);
    }
}
