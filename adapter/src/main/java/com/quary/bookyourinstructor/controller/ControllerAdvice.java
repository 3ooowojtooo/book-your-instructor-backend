package com.quary.bookyourinstructor.controller;

import com.quary.bookyourinstructor.exception.ExceptionResponse;
import com.quary.bookyourinstructor.exception.SimpleHttpResponseException;
import com.quary.bookyourinstructor.model.authentication.exception.AuthenticationErrorCode;
import com.quary.bookyourinstructor.model.exception.BaseInternalException;
import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;
import com.quary.bookyourinstructor.model.exception.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(BaseInvalidInputException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidInputException(final BaseInvalidInputException exception) {
        log.error("Thrown invalid input exception", exception);
        final ExceptionResponse response = ExceptionResponse.of(exception);
        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(BaseInternalException.class)
    public ResponseEntity<ExceptionResponse> handleInternalException(final BaseInternalException exception) {
        log.error("Thrown internal exception", exception);
        final ExceptionResponse response = ExceptionResponse.of(exception);
        return ResponseEntity.internalServerError()
                .body(response);
    }

    @ExceptionHandler(SimpleHttpResponseException.class)
    public ResponseEntity<ExceptionResponse> handleSimpleHttpResponseExceptionError(final SimpleHttpResponseException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(exception.getResponse());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedExceptionError(final AccessDeniedException exception) {
        log.error("Thrown access denied exception", exception);
        final ExceptionResponse response = ExceptionResponse.of(AuthenticationErrorCode.ACCESS_DENIED);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUncaughtException(final Exception exception) {
        log.error("Thrown exception", exception);
        final ExceptionResponse response = ExceptionResponse.of(CommonErrorCode.UNKNOWN_ERROR, exception.getMessage());
        return ResponseEntity.internalServerError()
                .body(response);
    }
}
