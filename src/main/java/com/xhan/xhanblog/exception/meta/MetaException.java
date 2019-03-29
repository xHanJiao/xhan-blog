package com.xhan.xhanblog.exception.meta;

public class MetaException extends RuntimeException {
    public MetaException() {
    }

    public MetaException(String message) {
        super(message);
    }

    public MetaException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetaException(Throwable cause) {
        super(cause);
    }

    public MetaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
