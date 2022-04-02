package com.quary.bookyourinstructor.controller;

import com.quary.bookyourinstructor.exception.ExceptionResponse;
import com.quary.bookyourinstructor.exception.SimpleHttpResponseException;
import com.quary.bookyourinstructor.model.user.exception.BaseInternalException;
import com.quary.bookyourinstructor.model.user.exception.BaseInvalidInputException;
import com.quary.bookyourinstructor.model.user.exception.CommonErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(BaseInvalidInputException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidInputException(final BaseInvalidInputException exception) {
        final ExceptionResponse response = ExceptionResponse.of(exception);
        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(BaseInternalException.class)
    public ResponseEntity<ExceptionResponse> handleInternalException(final BaseInternalException exception) {
        final ExceptionResponse response = ExceptionResponse.of(exception);
        return ResponseEntity.internalServerError()
                .body(response);
    }

    @ExceptionHandler(SimpleHttpResponseException.class)
    public ResponseEntity<ExceptionResponse> handleSimpleHttpResponseExceptionError(final SimpleHttpResponseException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(exception.getResponse());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUncaughtException(final Exception exception) {
        final ExceptionResponse response = ExceptionResponse.of(CommonErrorCode.UNKNOWN_ERROR, exception.getMessage());
        return ResponseEntity.internalServerError()
                .body(response);
    }
}
