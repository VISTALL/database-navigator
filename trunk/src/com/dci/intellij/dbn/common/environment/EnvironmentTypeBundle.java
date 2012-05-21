package com.dci.intellij.dbn.common.environment;

import com.dci.intellij.dbn.common.util.CollectionUtil;
import com.dci.intellij.dbn.common.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class EnvironmentTypeBundle implements Iterable<EnvironmentType>, Cloneable{
    private List<EnvironmentType> environmentTypes = new ArrayList<EnvironmentType>();
    public static EnvironmentTypeBundle DEFAULT = new EnvironmentTypeBundle();

    public EnvironmentTypeBundle() {
        List<EnvironmentType> environmentTypes = Arrays.asList(
                EnvironmentType.DEVELOPMENT,
                EnvironmentType.TEST,
                EnvironmentType.PRODUCTION,
                EnvironmentType.OTHER);
        setElements(environmentTypes);
    }
    
    public EnvironmentTypeBundle(EnvironmentTypeBundle source) {
        setElements(source.environmentTypes);
    }  
    
    private void setElements(List<EnvironmentType> environmentTypes) {
        this.environmentTypes.clear();
        CollectionUtil.cloneCollectionElements(environmentTypes, this.environmentTypes);        
    }
    
    public EnvironmentType get(String name) {
        for (EnvironmentType environmentType : this) {
            if (StringUtil.equals(environmentType.getName(), name)) {
                return environmentType;
            }
        }
        return EnvironmentType.DEFAULT;
    }

    @Override
    public Iterator<EnvironmentType> iterator() {
        return environmentTypes.iterator();
    }

    public void clear() {
        environmentTypes.clear();
    }

    public void add(EnvironmentType environmentType) {
        environmentTypes.add(environmentType);
    }
    
    public void add(int index, EnvironmentType environmentType) {
        environmentTypes.add(index, environmentType);
    }
    

    public int size() {
        return environmentTypes.size();
    }
    
    public EnvironmentType get(int index) {
        return environmentTypes.get(index);
    }

    public EnvironmentType remove(int index) {
        return environmentTypes.remove(index);
    }

    public EnvironmentTypeBundle clone() {
        return new EnvironmentTypeBundle(this);
    }
}
