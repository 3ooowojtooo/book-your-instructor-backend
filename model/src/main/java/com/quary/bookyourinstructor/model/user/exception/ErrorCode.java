package com.quary.bookyourinstructor.model.user.exception;

public interface ErrorCode {

    default String getStringRepresentation() {
        return toString();
    }
}
