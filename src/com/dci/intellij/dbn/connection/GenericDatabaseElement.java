package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.common.content.DynamicContent;
import com.dci.intellij.dbn.common.content.DynamicContentType;
import com.intellij.openapi.project.Project;

public interface GenericDatabaseElement {
    ConnectionHandler getConnectionHandler();
    Project getProject();
    GenericDatabaseElement getUndisposedElement();
    DynamicContent getDynamicContent(DynamicContentType dynamicContentType);
}
