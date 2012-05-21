package com.dci.intellij.dbn.common.event;

import com.dci.intellij.dbn.common.AbstractProjectComponent;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EventManager extends AbstractProjectComponent implements Disposable{
    private Map<Object, MessageBusConnection> connectionCache = new HashMap<Object, MessageBusConnection>();
    
    private EventManager(Project project) {
        super(project);
    }

    public static EventManager getInstance(Project project) {
        return project.getComponent(EventManager.class);
    }

    public MessageBusConnection connect(Object handler) {
        MessageBusConnection connection = connectionCache.get(handler);
        if (connection == null) {
            MessageBus messageBus = getProject().getMessageBus();
            connection = messageBus.connect();
            connectionCache.put(handler, connection);
        }
        return connection;
    }
    
    public <T> void subscribe(Topic<T> topic, T handler) {
        MessageBusConnection messageBusConnection = connect(handler);
        messageBusConnection.subscribe(topic, handler);
    }

    public void unsubscribe(Object ... handlers) {
        for (Object handler : handlers) {
            MessageBusConnection connection = connectionCache.remove(handler);
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public <T> T syncPublisher(Topic<T> topic) {
        MessageBus messageBus = getProject().getMessageBus();
        return messageBus.syncPublisher(topic);
    }

    public static <T> void subscribe(Project project, Topic<T> topic, T handler) {
        getInstance(project).subscribe(topic, handler);
    }

    public static <T> void unsubscribe(Project project, T handler) {
        getInstance(project).unsubscribe(handler);
    }

    public static <T> T syncPublisher(Project project, Topic<T> topic) {
        return getInstance(project).syncPublisher(topic);
    }

    @NonNls
    @NotNull
    public String getComponentName() {
        return "DBNavigator.Project.EventManager";
    }
    

    public void dispose() {
        for (MessageBusConnection connection : connectionCache.values()) {
            connection.disconnect();
        }
        connectionCache.clear();
    }

}
