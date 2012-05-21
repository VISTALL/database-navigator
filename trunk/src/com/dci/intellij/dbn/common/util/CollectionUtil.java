package com.dci.intellij.dbn.common.util;

import java.util.Collection;

public class CollectionUtil {
    public static <T extends Cloneable<T>> void cloneCollectionElements(Collection<T> source, Collection<T> target) {
        for (T cloneable : source) {
            T clone = cloneable.clone();
            target.add(clone);
        }
    }
}
