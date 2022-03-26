package com.quary.bookyourinstructor.model.user.exception;

public class InvalidEmailOrPasswordException extends Exception {

    public InvalidEmailOrPasswordException() {
        super("Invalid email or password");
    }
}
