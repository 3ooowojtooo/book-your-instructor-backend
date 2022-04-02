package com.quary.bookyourinstructor.model.user.exception;

public class BaseInternalException extends BaseException {

    protected BaseInternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected BaseInternalException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    protected BaseInternalException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
