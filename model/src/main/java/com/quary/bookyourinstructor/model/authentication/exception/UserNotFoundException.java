package com.quary.bookyourinstructor.model.authentication.exception;

import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;

public class UserNotFoundException extends BaseInvalidInputException {

    private static final String MESSAGE = "User not found";

    public UserNotFoundException() {
        super(AuthenticationErrorCode.USER_NOT_FOUND, MESSAGE);
    }
}
