package com.quary.bookyourinstructor.model.user.authentication.exception;

import com.quary.bookyourinstructor.model.user.exception.BaseInvalidInputException;

import static com.quary.bookyourinstructor.model.user.authentication.exception.AuthenticationErrorCode.INVALID_EMAIL_OR_PASSWORD;

public class InvalidEmailOrPasswordException extends BaseInvalidInputException {

    public InvalidEmailOrPasswordException() {
        super(INVALID_EMAIL_OR_PASSWORD, "Invalid email or password");
    }
}
