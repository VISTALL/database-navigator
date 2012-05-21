package com.dci.intellij.dbn.common.thread;

import com.intellij.openapi.application.ApplicationManager;

public abstract class WriteActionRunner {

    public final void start() {
        Runnable writeAction = new Runnable() {
            public void run() {
                WriteActionRunner.this.run();
            }
        };
        ApplicationManager.getApplication().runWriteAction(writeAction);
    }

    public abstract void run();

}
