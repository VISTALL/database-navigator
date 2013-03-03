package com.dci.intellij.dbn.object;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.List;

public class DBObjectIdentifier {
    private Project project;
    private String connectionId;
    private Node[] nodes;


    public DBObjectIdentifier(DBObject object) {
        project = object.getProject();
        connectionId = object.getConnectionHandler().getId();

        List<DBObject> chain = new ArrayList<DBObject>();
        chain.add(object);

        DBObject parentObject = object.getParentObject();
        while (parentObject != null) {
            chain.add(0, parentObject);
            parentObject = parentObject.getParentObject();
        }
        int length = chain.size();
        nodes = new Node[length];
        for (int i = 0; i<length; i++) {
            DBObject chainObject = chain.get(i);
            nodes[i] = new Node(chainObject.getObjectType(), chainObject.getName());
        }
    }

    public DBObjectIdentifier(ConnectionHandler connectionHandler) {
        this.project = connectionHandler.getProject();
        this.connectionId = connectionHandler.getId();
    }

    public DBObjectIdentifier add(DBObjectType objectType, String name) {
        Node node = new Node(objectType, name);
        if (nodes == null) {
            nodes = new Node[1];
            nodes[0] = node;
        } else {
            Node[] newNodes = new Node[nodes.length + 1];
            System.arraycopy(nodes, 0, newNodes, 0, nodes.length);
            newNodes[nodes.length] = node;
            nodes = newNodes;
        }
        return this;
    }

    public String getNameWithPath() {
        if (nodes.length == 1) {
            return nodes[0].getName();
        } else {
            StringBuilder buffer = new StringBuilder();
            for (Node node : nodes) {
                if (buffer.length() > 0) buffer.append(".");
                buffer.append(node.getName());
            }
            return buffer.toString();
        }
    }


    public String getConnectionId() {
        return connectionId;
    }

    public DBObject lookupObject() {
        ConnectionHandler connectionHandler = lookupConnectionHandler();

        DBObject object = null;
        for (Node node : nodes) {
            DBObjectType objectType = node.getType();
            String objectName = node.getName();
            if (object == null) {
                object = connectionHandler.getObjectBundle().getObject(objectType, objectName);
            } else {
                object = object.getChildObject(objectType, objectName, true);
            }
            if (object == null) break;
        }
        return object;
    }

    public ConnectionHandler lookupConnectionHandler() {
        ConnectionManager connectionManager = ConnectionManager.getInstance(project);
        return connectionManager.getConnectionHandler(connectionId);
    }

    public static class Node {
        private DBObjectType type;
        private String name;

        public Node(DBObjectType type, String name) {
            this.type = type;
            this.name = name;
        }

        public DBObjectType getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

}
