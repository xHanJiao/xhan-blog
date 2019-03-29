package com.xhan.xhanblog.exception.article;

public class ArticleSaveException extends ArticleException {
    public ArticleSaveException() {
    }

    public ArticleSaveException(String message) {
        super(message);
    }

    public ArticleSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticleSaveException(Throwable cause) {
        super(cause);
    }

    public ArticleSaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
