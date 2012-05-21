package com.dci.intellij.dbn.common.content.loader;

public class DynamicContentLoaderException extends Exception{
    public DynamicContentLoaderException() {
    }

    public DynamicContentLoaderException(String message) {
        super(message);
    }

    public DynamicContentLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DynamicContentLoaderException(Throwable cause) {
        super(cause);
    }
}
