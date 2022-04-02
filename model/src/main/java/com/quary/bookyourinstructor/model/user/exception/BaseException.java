package com.quary.bookyourinstructor.model.user.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends Exception {

    private final ErrorCode errorCode;

    protected BaseException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    protected BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected BaseException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
