package com.dci.intellij.dbn.common.content.loader;

import com.dci.intellij.dbn.common.LoggerFactory;
import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentElement;
import com.dci.intellij.dbn.common.options.setting.SettingsUtil;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionUtil;
import com.dci.intellij.dbn.object.common.DBObject;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class DynamicContentResultSetLoader<T extends DynamicContentElement> implements DynamicContentLoader<T> {
    private static final Logger LOGGER = LoggerFactory.createLogger();

    public abstract ResultSet createResultSet(DynamicContent<T> dynamicContent, Connection connection) throws SQLException;
    public abstract T createElement(DynamicContent<T> dynamicContent, ResultSet resultSet, LoaderCache loaderCache) throws SQLException;

    private class DebugInfo {                                                         
        private String id = UUID.randomUUID().toString();
        private long startTimestamp = System.currentTimeMillis();
    }

    private DebugInfo preLoadContent(DynamicContent dynamicContent) {
        if (SettingsUtil.isDebugEnabled()) {
            DebugInfo debugInfo = new DebugInfo();
            LOGGER.info(
                    "[DBN-INFO] Loading " + dynamicContent.getContentDescription() +
                    " (id = " + debugInfo.id + ")");
            return debugInfo;
        }
        return null;
    }

    private void postLoadContent(DynamicContent dynamicContent, DebugInfo debugInfo) {
        if (debugInfo != null) {
            LOGGER.info(
                    "[DBN-INFO] Done loading " + dynamicContent.getContentDescription() +
                    " (id = " + debugInfo.id + ") - " +
                    (System.currentTimeMillis() - debugInfo.startTimestamp) + "ms"   );
        }
    }

    public void loadContent(DynamicContent<T> dynamicContent) throws DynamicContentLoaderException {
        DebugInfo debugInfo = preLoadContent(dynamicContent);

        ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();
        if (progressIndicator != null) {
            progressIndicator.setText("Loading " + dynamicContent.getContentDescription());
        }
        ConnectionHandler connectionHandler = dynamicContent.getConnectionHandler();
        LoaderCache loaderCache = new LoaderCache();
        Connection connection = null;
        ResultSet resultSet = null;
        int count = 0;
        try {
            connection = connectionHandler.getPoolConnection();
            resultSet = createResultSet(dynamicContent, connection);
            List<T> list = null;
            while (resultSet != null && resultSet.next()) {
                if (dynamicContent.isDisposed()) return;
                
                T element = null;
                try {
                    element = createElement(dynamicContent, resultSet, loaderCache);
                } catch (RuntimeException e){
                    System.out.println("RuntimeException: " + e.getMessage());
                }

                if (element != null && dynamicContent.accepts(element)) {
                    if (list == null) list = new ArrayList<T>();
                    list.add(element);
                    if (progressIndicator != null && count%10 == 0) {
                        String description = element.getDescription();
                        if (description != null)
                            progressIndicator.setText2(description);    
                    }
                    count++;
                }
            }
            dynamicContent.setElements(list, true);

            postLoadContent(dynamicContent, debugInfo);
        } catch (Exception e) {
            LOGGER.warn("Error loading database content (" + dynamicContent.getContentDescription() + "): " + StringUtil.trim(e.getMessage()));
            throw new DynamicContentLoaderException(e);
        } finally {
            ConnectionUtil.closeResultSet(resultSet);
            connectionHandler.freePoolConnection(connection);
        }
    }

    public void reloadContent(DynamicContent<T> dynamicContent) throws DynamicContentLoaderException {
        loadContent(dynamicContent);
    }

    public class LoaderCache {
        private String name;
        private DBObject object;
        public DBObject getObject(String name) {
            if (name.equals(this.name)) {
                return object;
            }
            return null;
        }

        public void setObject(String name, DBObject object) {
            this.name = name;
            this.object = object;
        }
    }
}
