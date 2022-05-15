package com.quary.bookyourinstructor.model.authentication.exception;

import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;

public class InvalidJwtException extends BaseInvalidInputException {

    private static final String MESSAGE = "JWT is invalid";

    public InvalidJwtException() {
        super(AuthenticationErrorCode.JWT_INVALID, MESSAGE);
    }

    public InvalidJwtException(Throwable cause) {
        super(AuthenticationErrorCode.JWT_INVALID, MESSAGE, cause);
    }
}
