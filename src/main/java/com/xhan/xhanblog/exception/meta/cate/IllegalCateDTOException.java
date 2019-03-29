package com.xhan.xhanblog.exception.meta.cate;

public class IllegalCateDTOException extends CategoryException {
    public IllegalCateDTOException() {
    }

    public IllegalCateDTOException(String message) {
        super(message);
    }

    public IllegalCateDTOException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalCateDTOException(Throwable cause) {
        super(cause);
    }

    public IllegalCateDTOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
