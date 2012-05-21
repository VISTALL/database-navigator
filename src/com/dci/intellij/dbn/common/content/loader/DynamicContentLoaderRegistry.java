package com.dci.intellij.dbn.common.content.loader;

public class DynamicContentLoaderRegistry {
    private static final ThreadLocal<Boolean> BULK_LOAD = new ThreadLocal<Boolean>();

    public static boolean isBulkLoad() {
        Boolean majorLoad = BULK_LOAD.get();
        return majorLoad == null ? false : majorLoad;
    }

    public static void registerBulkLoad(){
        BULK_LOAD.set(true);
    }

    public static void unregisterBulkLoad(){
        BULK_LOAD.set(false);
    }
}
