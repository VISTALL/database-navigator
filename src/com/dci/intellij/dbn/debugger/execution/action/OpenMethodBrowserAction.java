package com.dci.intellij.dbn.debugger.execution.action;

import com.dci.intellij.dbn.common.thread.BackgroundTask;
import com.dci.intellij.dbn.common.thread.SimpleLaterInvocator;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.debugger.execution.DBProgramRunConfiguration;
import com.dci.intellij.dbn.execution.method.MethodExecutionInput;
import com.dci.intellij.dbn.execution.method.MethodExecutionManager;
import com.dci.intellij.dbn.execution.method.browser.MethodBrowserSettings;
import com.dci.intellij.dbn.execution.method.browser.ui.MethodExecutionBrowserDialog;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.common.ui.ObjectTreeModel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;

public class OpenMethodBrowserAction extends AbstractSelectMethodAction {
    public OpenMethodBrowserAction(DBProgramRunConfiguration configuration) {
        super("Method Browser", null, configuration);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = ActionUtil.getProject(e);
        if (project != null) {
            BackgroundTask backgroundTask = new BackgroundTask(project, "Loading executable elements", false) {
                @Override
                public void execute(@NotNull ProgressIndicator progressIndicator) {
                    initProgressIndicator(progressIndicator, true);
                    final MethodBrowserSettings settings = MethodExecutionManager.getInstance(project).getBrowserSettings();
                    DBMethod currentMethod = getConfiguration().getExecutionInput() == null ? null : getConfiguration().getExecutionInput().getMethod();
                    if (currentMethod != null) {
                        settings.setConnectionHandler(currentMethod.getConnectionHandler());
                        settings.setSchema(currentMethod.getSchema());
                        settings.setMethod(currentMethod);
                    }

                    final ObjectTreeModel objectTreeModel = new ObjectTreeModel(settings.getSchema(), settings.getVisibleObjectTypes(), settings.getMethod());

                    new SimpleLaterInvocator() {
                        public void run() {
                            final MethodExecutionBrowserDialog browserDialog = new MethodExecutionBrowserDialog(project, settings, objectTreeModel);
                            browserDialog.show();
                            if (browserDialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
                                DBMethod method = browserDialog.getSelectedMethod();
                                MethodExecutionManager methodExecutionManager = MethodExecutionManager.getInstance(project);
                                MethodExecutionInput methodExecutionInput = methodExecutionManager.getExecutionInput(method);
                                if (methodExecutionInput != null) {
                                    getConfiguration().setExecutionInput(methodExecutionInput);
                                }
                            }
                        }
                    }.start();

                }
            };
            backgroundTask.start();
        }
    }
}