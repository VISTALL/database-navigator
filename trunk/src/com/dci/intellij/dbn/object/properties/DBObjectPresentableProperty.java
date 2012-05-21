package com.dci.intellij.dbn.object.properties;

import com.dci.intellij.dbn.common.util.NamingUtil;
import com.dci.intellij.dbn.object.common.DBObject;
import com.intellij.pom.Navigatable;

import javax.swing.*;

public class DBObjectPresentableProperty extends PresentableProperty{
    private DBObject object;
    private boolean qualified = false;
    private String name;


    public DBObjectPresentableProperty(String name, DBObject object, boolean qualified) {
        this.object = object;
        this.qualified = qualified;
        this.name = name;
    }

    public DBObjectPresentableProperty(DBObject object, boolean qualified) {
        this.object = object;
        this.qualified = qualified;
    }

    public DBObjectPresentableProperty(DBObject object) {
        this.object = object;
    }

    public String getName() {
        return name == null ? NamingUtil.capitalize(object.getTypeName()) : name;
    }

    public String getValue() {
        return qualified ? object.getQualifiedName() : object.getName();
    }

    public Icon getIcon() {
        return object.getIcon();
    }

    @Override
    public Navigatable getNavigatable() {
        return object;
    }
}
