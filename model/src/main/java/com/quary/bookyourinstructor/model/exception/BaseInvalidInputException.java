package com.quary.bookyourinstructor.model.exception;

public abstract class BaseInvalidInputException extends BaseException {

    protected BaseInvalidInputException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected BaseInvalidInputException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    protected BaseInvalidInputException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
