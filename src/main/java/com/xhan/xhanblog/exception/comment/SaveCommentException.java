package com.xhan.xhanblog.exception.comment;

public class SaveCommentException extends RuntimeException {
    public SaveCommentException() {
    }

    public SaveCommentException(String message) {
        super(message);
    }

    public SaveCommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveCommentException(Throwable cause) {
        super(cause);
    }

    public SaveCommentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
