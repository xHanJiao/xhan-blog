package com.xhan.xhanblog.exception.comment;

public class DeleteCommentException extends CommentException {
    public DeleteCommentException() {
    }

    public DeleteCommentException(String message) {
        super(message);
    }

    public DeleteCommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteCommentException(Throwable cause) {
        super(cause);
    }

    public DeleteCommentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
