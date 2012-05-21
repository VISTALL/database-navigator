package com.dci.intellij.dbn.common.thread;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;

public abstract class ConditionalLaterInvocator implements Runnable{
    public void start() {
        Application application = ApplicationManager.getApplication();
        if (application.isDispatchThread()) {
            run();
        } else {
            application.invokeLater(this/*, ModalityState.NON_MODAL*/);
        }
    }
}