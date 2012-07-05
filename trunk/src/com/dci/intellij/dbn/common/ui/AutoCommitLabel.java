package com.dci.intellij.dbn.common.ui;

import com.dci.intellij.dbn.common.event.EventManager;
import com.dci.intellij.dbn.common.util.UIUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionSettingsChangeListener;
import org.picocontainer.Disposable;

import javax.swing.JLabel;
import java.awt.Color;

public class AutoCommitLabel extends JLabel implements ConnectionSettingsChangeListener, Disposable {
    private ConnectionHandler connectionHandler;

    public AutoCommitLabel() {
        super("");
        setFont(UIUtil.BOLD_FONT);
    }

    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        EventManager.subscribe(connectionHandler.getProject(), ConnectionSettingsChangeListener.TOPIC, this);
        update();
    }

    private void update() {
        if (connectionHandler != null) {
            boolean autoCommit = connectionHandler.isAutoCommit();
            setText(autoCommit ? "Auto Commit ON" : "Auto Commit OFF");
            setForeground(autoCommit ? new Color(210, 0, 0): new Color(0, 190, 0));
            setToolTipText(autoCommit ?
                    "Auto Commit is enabled for connection \"" + connectionHandler + "\". Data changes will be automatically committed to the database." :
                    "Auto Commit is disabled for connection \"" + connectionHandler + "\". Data changes will need to be manually committed to the database.");
        }
    }

    @Override
    public void connectionSettingsChanged(String connectionId) {
        if (connectionHandler != null && connectionId.equals(connectionHandler.getId())) {
            update();
        }
    }

    @Override
    public void dispose() {
        if (connectionHandler != null) {
            EventManager.unsubscribe(connectionHandler.getProject(), this);
        }
    }
}
