package com.xhan.xhanblog.exception.comment;

public class CommentIllegalException extends CommentException {
    public CommentIllegalException() {
    }

    public CommentIllegalException(String message) {
        super(message);
    }

    public CommentIllegalException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentIllegalException(Throwable cause) {
        super(cause);
    }

    public CommentIllegalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
