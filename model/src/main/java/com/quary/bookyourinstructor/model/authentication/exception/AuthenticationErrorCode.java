package com.quary.bookyourinstructor.model.authentication.exception;

import com.quary.bookyourinstructor.model.exception.ErrorCode;

public enum AuthenticationErrorCode implements ErrorCode {
    INVALID_EMAIL_OR_PASSWORD,
    USER_WITH_EMAIL_ALREADY_EXISTS,
    JWT_EXPIRED,
    JWT_INVALID,
    ACCESS_DENIED
}
