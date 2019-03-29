package com.xhan.xhanblog.exception;

public class ErrorCredentialException extends RuntimeException {
    public ErrorCredentialException() {
    }

    public ErrorCredentialException(String message) {
        super(message);
    }

    public ErrorCredentialException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorCredentialException(Throwable cause) {
        super(cause);
    }

    public ErrorCredentialException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
