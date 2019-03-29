package com.xhan.xhanblog.exception;

import org.springframework.dao.DataAccessException;

public class DeleteRecordException extends RuntimeException{

    public DeleteRecordException() {
    }

    public DeleteRecordException(String message) {
        super(message);
    }

    public DeleteRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteRecordException(Throwable cause) {
        super(cause);
    }

    public DeleteRecordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
