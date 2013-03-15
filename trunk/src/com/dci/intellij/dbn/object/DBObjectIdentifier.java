package com.dci.intellij.dbn.object;

import com.dci.intellij.dbn.connection.ConnectionCache;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBObjectIdentifier<T extends DBObject> implements Comparable {
    protected String connectionId;
    protected Node[] nodes;

    public DBObjectIdentifier(T object) {
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
        this.connectionId = connectionHandler.getId();
    }

    public DBObjectIdentifier(String connectionId) {
        this.connectionId = connectionId;
    }

    public DBObjectIdentifier() {

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

    public String getPath() {
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

    public T lookupObject() {
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
        return (T) object;
    }

    public ConnectionHandler lookupConnectionHandler() {
        return ConnectionCache.findConnectionHandler(connectionId);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof DBObjectIdentifier) {
            DBObjectIdentifier that = (DBObjectIdentifier) o;
            int result = this.connectionId.compareTo(that.getConnectionId());
            if (result != 0) return result;

            if (this.nodes.length != that.nodes.length) {
                return this.nodes.length - that.nodes.length;
            }

            for (int i=0; i<this.nodes.length; i++) {
                Node thisNode = this.nodes[i];
                Node thatNode = that.nodes[i];
                result = thisNode.getType().compareTo(thatNode.getType());
                if (result != 0) return result;
                result = thisNode.getName().compareTo(thatNode.getName());
                if (result != 0) return result;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBObjectIdentifier that = (DBObjectIdentifier) o;

        if (!connectionId.equals(that.connectionId)) return false;
        if (!Arrays.equals(nodes, that.nodes)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = connectionId.hashCode();
        result = 31 * result + Arrays.hashCode(nodes);
        return result;
    }

    public String getName() {
        return nodes[nodes.length-1].getName();
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            if (!name.equals(node.name)) return false;
            if (type != node.type) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = type.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "[" + type.getName() + "] " + getName();
        }
    }

}
