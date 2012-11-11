package com.dci.intellij.dbn.code.common.completion;

import com.dci.intellij.dbn.common.thread.SimpleLaterInvocator;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;

public class CodeCompletionPopupTrigger implements DocumentListener{
    public static final DocumentListener INSTANCE = new CodeCompletionPopupTrigger();

    private AnAction codeCompletionAction;
    private int executionSequence;


    public void beforeDocumentChange(DocumentEvent event) {
    }

    public void documentChanged(DocumentEvent event) {
        CharSequence newFragment = event.getNewFragment();
        if (newFragment.length() > 0 && newFragment.charAt(newFragment.length()-1) == '.') {
            if (codeCompletionAction == null) codeCompletionAction = ActionManager.getInstance().getAction(IdeActions.ACTION_CODE_COMPLETION);

            Thread delayThread = new Thread(new Runnable() {
                public void run() {
                    executionSequence++;
                    int oldExecutionSequence = executionSequence;
                    try {
                        Thread.currentThread().join(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (oldExecutionSequence == executionSequence) {
                        new SimpleLaterInvocator() {
                            public void run() {
                                DataContext dataContext = DataManager.getInstance().getDataContext();
                                Project project = PlatformDataKeys.PROJECT.getData(dataContext);
                                Editor editor = PlatformDataKeys.EDITOR.getData(dataContext);
                                if (project != null && editor!= null) {
                                    AnActionEvent actionEvent = new AnActionEvent(null, dataContext, "", codeCompletionAction.getTemplatePresentation(), ActionManager.getInstance(), 2);
                                    codeCompletionAction.actionPerformed(actionEvent);
                                }
                            }
                        }.start();
                    }
                }
            });
            delayThread.start();
        } else {
            executionSequence++;
        }
    }
}
