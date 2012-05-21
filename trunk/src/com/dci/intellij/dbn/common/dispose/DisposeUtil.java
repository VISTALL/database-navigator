package com.dci.intellij.dbn.common.dispose;

import com.intellij.openapi.Disposable;

import java.util.Collection;
import java.util.Map;

public class DisposeUtil {
    public static void dispose(Disposable disposable) {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public static void disposeCollection(Collection<? extends Disposable> collection) {
        if (collection != null) {
            for(Disposable disposable : collection) {
                if (disposable != null) {
                    disposable.dispose();
                }

            }
            collection.clear();
        }
    }
    
    public static void disposeMap(Map<?,? extends Disposable> map) {
        if (map != null) {
            for (Disposable disposable : map.values()) {
                disposable.dispose();
            }
            map.clear();
        }
    }
}