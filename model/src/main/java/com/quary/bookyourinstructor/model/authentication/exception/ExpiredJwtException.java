package com.quary.bookyourinstructor.model.authentication.exception;

import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;

public class ExpiredJwtException extends BaseInvalidInputException {

    private static final String MESSAGE = "JWT is expired";

    public ExpiredJwtException() {
        super(AuthenticationErrorCode.JWT_EXPIRED, MESSAGE);
    }

    public ExpiredJwtException(Throwable cause) {
        super(AuthenticationErrorCode.JWT_EXPIRED, MESSAGE, cause);
    }
}
