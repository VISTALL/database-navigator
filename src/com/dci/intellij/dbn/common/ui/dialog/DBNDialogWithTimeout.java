package com.dci.intellij.dbn.common.ui.dialog;

import com.dci.intellij.dbn.common.Time;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import java.util.Timer;
import java.util.TimerTask;

public abstract class DBNDialogWithTimeout extends DBNDialog{
    private int secondsLeft;
    private DBNDialogWithTimeoutForm form;
    private Timer timeoutTimer;

    protected DBNDialogWithTimeout(Project project, String title, int timeoutSeconds) {
        super(project, title, false);
        secondsLeft = timeoutSeconds;
        form = new DBNDialogWithTimeoutForm(createContentComponent(), secondsLeft);


        timeoutTimer = new Timer("Timeout dialog task [" + project.getName() + "]");
        timeoutTimer.schedule(new TimeoutTask(), Time.ONE_SECOND, Time.ONE_SECOND);
    }

    private class TimeoutTask extends TimerTask {
        public void run() {
            if (secondsLeft > 0) {
                secondsLeft = secondsLeft -1;
                form.updateTimeLeft(secondsLeft);
                if (secondsLeft == 0) {
                    performDefaultAction();
                }
            }
        }
    }

    @Nullable
    @Override
    protected final JComponent createCenterPanel() {
        return form.getComponent();
    }

    protected abstract JComponent createContentComponent();

    public abstract int performDefaultAction();

    @Override
    protected void dispose() {
        super.dispose();
        timeoutTimer.cancel();
        timeoutTimer.purge();
    }

}
