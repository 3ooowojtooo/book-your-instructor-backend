package com.quary.bookyourinstructor.model.exception;

public interface ErrorCode {

    default String getStringRepresentation() {
        return toString();
    }
}
