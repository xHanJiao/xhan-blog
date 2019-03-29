package com.xhan.xhanblog.exception.article;

public class ContentTooLongException extends ArticleException {

    public ContentTooLongException() {
    }

    public ContentTooLongException(String message) {
        super(message);
    }

    public ContentTooLongException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentTooLongException(Throwable cause) {
        super(cause);
    }

    public ContentTooLongException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
