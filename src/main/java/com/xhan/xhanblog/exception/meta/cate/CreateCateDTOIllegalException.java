package com.xhan.xhanblog.exception.meta.cate;

public class CreateCateDTOIllegalException extends CategoryException {
    public CreateCateDTOIllegalException() {
    }

    public CreateCateDTOIllegalException(String message) {
        super(message);
    }

    public CreateCateDTOIllegalException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateCateDTOIllegalException(Throwable cause) {
        super(cause);
    }

    public CreateCateDTOIllegalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
