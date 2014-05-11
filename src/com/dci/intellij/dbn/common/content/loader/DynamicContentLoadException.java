package com.dci.intellij.dbn.common.content.loader;

public class DynamicContentLoadException extends Exception{
    public DynamicContentLoadException() {
    }

    public DynamicContentLoadException(String message) {
        super(message);
    }

    public DynamicContentLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public DynamicContentLoadException(Throwable cause) {
        super(cause);
    }
}
