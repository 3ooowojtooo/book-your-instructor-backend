package com.quary.bookyourinstructor.model.authentication.exception;

import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;

import static com.quary.bookyourinstructor.model.authentication.exception.AuthenticationErrorCode.INVALID_EMAIL_OR_PASSWORD;

public class InvalidEmailOrPasswordException extends BaseInvalidInputException {

    public InvalidEmailOrPasswordException() {
        super(INVALID_EMAIL_OR_PASSWORD, "Invalid email or password");
    }
}
