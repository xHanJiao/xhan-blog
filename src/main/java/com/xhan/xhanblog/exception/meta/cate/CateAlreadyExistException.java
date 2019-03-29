package com.xhan.xhanblog.exception.meta.cate;

public class CateAlreadyExistException extends CategoryException {
    public CateAlreadyExistException() {
    }

    public CateAlreadyExistException(String message) {
        super(message);
    }

    public CateAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public CateAlreadyExistException(Throwable cause) {
        super(cause);
    }

    public CateAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
