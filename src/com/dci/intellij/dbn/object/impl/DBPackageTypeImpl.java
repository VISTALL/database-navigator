package com.dci.intellij.dbn.object.impl;

import com.dci.intellij.dbn.browser.DatabaseBrowserUtils;
import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.loader.DynamicContentLoader;
import com.dci.intellij.dbn.common.content.loader.DynamicContentResultSetLoader;
import com.dci.intellij.dbn.database.DatabaseMetadataInterface;
import com.dci.intellij.dbn.object.DBPackage;
import com.dci.intellij.dbn.object.DBPackageType;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.dci.intellij.dbn.object.common.property.DBObjectProperty;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBPackageTypeImpl extends DBTypeImpl implements DBPackageType {
    private DBPackage packagee;

    public DBPackageTypeImpl(DBPackage packagee, ResultSet resultSet) throws SQLException {
        super(packagee, resultSet);
        this.packagee = packagee;
    }

    protected void initLists() {
        attributes = getChildObjects().createObjectList(DBObjectType.TYPE_ATTRIBUTE, this, ATTRIBUTES_LOADER, true, false);
    }

    public void updateStatuses(ResultSet resultSet) throws SQLException {}

    public void updateProperties() {
        getProperties().set(DBObjectProperty.NAVIGABLE);
    }

    public DBPackage getPackage() {
        packagee = (DBPackage) packagee.getUndisposedElement();
        return packagee;
    }

    @Override
    public DBObjectType getObjectType() {
        return DBObjectType.PACKAGE_TYPE;
    }

    @Override
    public Icon getIcon() {
        return isCollection() ? Icons.DBO_TYPE_COLLECTION : Icons.DBO_TYPE;
    }

    @NotNull
    public List<BrowserTreeNode> buildAllPossibleTreeChildren() {
        return DatabaseBrowserUtils.createList(attributes);
    }

    public boolean isEmbedded() {
        return true;
    }

    @Override
    public void dispose() {
        super.dispose();
        packagee = null;
    }

    private static final DynamicContentLoader ATTRIBUTES_LOADER = new DynamicContentResultSetLoader() {
        public ResultSet createResultSet(DynamicContent dynamicContent, Connection connection) throws SQLException {
            DatabaseMetadataInterface metadataInterface = dynamicContent.getConnectionHandler().getInterfaceProvider().getMetadataInterface();
            DBPackageTypeImpl type = (DBPackageTypeImpl) dynamicContent.getParent();
            return metadataInterface.loadProgramTypeAttributes(
                    type.getSchema().getName(),
                    type.getPackage().getName(),
                    type.getName(), connection);
        }

        public DBObject createElement(DynamicContent dynamicContent, ResultSet resultSet, LoaderCache loaderCache) throws SQLException {
            DBTypeImpl type = (DBTypeImpl) dynamicContent.getParent();
            return new DBTypeAttributeImpl(type, resultSet);
        }
    };

}
