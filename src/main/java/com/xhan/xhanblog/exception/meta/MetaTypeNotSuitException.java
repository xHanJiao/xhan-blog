package com.xhan.xhanblog.exception.meta;

public class MetaTypeNotSuitException extends MetaException {
    public MetaTypeNotSuitException() {
    }

    public MetaTypeNotSuitException(String message) {
        super(message);
    }

    public MetaTypeNotSuitException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetaTypeNotSuitException(Throwable cause) {
        super(cause);
    }

    public MetaTypeNotSuitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
