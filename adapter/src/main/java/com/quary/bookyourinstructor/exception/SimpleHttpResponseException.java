package com.quary.bookyourinstructor.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleHttpResponseException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final ExceptionResponse response;

    public SimpleHttpResponseException of(final HttpStatus httpStatus, final ExceptionResponse response) {
        return new SimpleHttpResponseException(httpStatus, response);
    }
}
