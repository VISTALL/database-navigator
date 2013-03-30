package com.dci.intellij.dbn.common.ui;

import com.dci.intellij.dbn.common.event.EventManager;
import com.dci.intellij.dbn.common.thread.ConditionalLaterInvocator;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionStatusListener;
import com.dci.intellij.dbn.connection.VirtualConnectionHandler;
import com.intellij.openapi.project.Project;
import org.picocontainer.Disposable;

import javax.swing.JLabel;
import java.awt.Color;

public class AutoCommitLabel extends JLabel implements ConnectionStatusListener, Disposable {
    private Project project;
    private ConnectionHandler connectionHandler;
    private boolean subscribed = false;

    public AutoCommitLabel() {
        super("");
        setFont(UIUtil.BOLD_FONT);
    }

    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        if (connectionHandler == null || connectionHandler instanceof VirtualConnectionHandler) {
            this.connectionHandler = null;
        } else {
            this.connectionHandler = connectionHandler;
            if (!subscribed) {
                subscribed = true;
                project = connectionHandler.getProject();
                EventManager.subscribe(project, ConnectionStatusListener.TOPIC, this);
            }
        }
        update();
    }

    private void update() {
        new ConditionalLaterInvocator() {
            @Override
            public void run() {
                if (connectionHandler != null) {
                    setVisible(true);
                    boolean disconnected = !connectionHandler.isConnected();
                    boolean autoCommit = connectionHandler.isAutoCommit();
                    setText(disconnected ? "Not connected to database" : autoCommit ? "Auto-Commit ON" : "Auto-Commit OFF");
                    setForeground(disconnected ?
                            new DBNColor(new Color(0x454545), new Color(0x808080)) : autoCommit ?
                            new DBNColor(new Color(0xd20000), new Color(0xBC3F3C)) :
                            new DBNColor(new Color(0xa000), new Color(0x629755)));
                    setToolTipText(
                            disconnected ? "The connection to database has been closed. No editing possible" :
                                    autoCommit ?
                                            "Auto-Commit is enabled for connection \"" + connectionHandler + "\". Data changes will be automatically committed to the database." :
                                            "Auto-Commit is disabled for connection \"" + connectionHandler + "\". Data changes will need to be manually committed to the database.");
                } else {
                    setVisible(false);
                }
            }
        }.start();
    }

    @Override
    public void statusChanged(String connectionId) {
        if (connectionHandler != null && connectionHandler.getId().equals(connectionId)) {
            update();
        }
    }

    @Override
    public void dispose() {
        EventManager.unsubscribe(this);
    }


}
