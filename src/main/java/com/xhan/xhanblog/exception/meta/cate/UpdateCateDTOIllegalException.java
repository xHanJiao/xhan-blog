package com.xhan.xhanblog.exception.meta.cate;

public class UpdateCateDTOIllegalException extends CategoryException {
    public UpdateCateDTOIllegalException() {
    }

    public UpdateCateDTOIllegalException(String message) {
        super(message);
    }

    public UpdateCateDTOIllegalException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateCateDTOIllegalException(Throwable cause) {
        super(cause);
    }

    public UpdateCateDTOIllegalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
