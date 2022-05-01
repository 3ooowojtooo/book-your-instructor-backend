package com.quary.bookyourinstructor.model.authentication.exception;

import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;

import static java.lang.String.format;

public class UserWithEmailAlreadyExists extends BaseInvalidInputException {

    private static final String MESSAGE = "User with email %s already exists";

    public UserWithEmailAlreadyExists(final String email) {
        super(AuthenticationErrorCode.USER_WITH_EMAIL_ALREADY_EXISTS, format(MESSAGE, email));
    }
}
