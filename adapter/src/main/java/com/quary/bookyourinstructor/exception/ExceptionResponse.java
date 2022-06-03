package com.quary.bookyourinstructor.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.quary.bookyourinstructor.model.exception.BaseException;
import com.quary.bookyourinstructor.model.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ExceptionResponse {

    private final String errorCode;
    private final String message;
    private final Object data;

    private ExceptionResponse(String errorCode, String message) {
        this(errorCode, message, null);
    }

    public static ExceptionResponse of(final BaseException baseException) {
        return new ExceptionResponse(baseException.getErrorCode().getStringRepresentation(), baseException.getMessage());
    }

    public static ExceptionResponse of(final ErrorCode errorCode, final String message) {
        return new ExceptionResponse(errorCode.getStringRepresentation(), message);
    }

    public static ExceptionResponse of(final ErrorCode errorCode) {
        return new ExceptionResponse(errorCode.getStringRepresentation(), null);
    }

    public static ExceptionResponse of(final BaseException baseException, final Object data) {
        return new ExceptionResponse(baseException.getErrorCode().getStringRepresentation(),
                baseException.getMessage(), data);
    }
}
