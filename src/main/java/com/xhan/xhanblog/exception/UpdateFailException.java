package com.xhan.xhanblog.exception;

public class UpdateFailException extends RuntimeException {
    public UpdateFailException() {
    }

    public UpdateFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateFailException(Throwable cause) {
        super(cause);
    }

    public UpdateFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UpdateFailException(String s) {
        super(s);
    }
}
