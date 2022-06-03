package com.quary.bookyourinstructor.model.event.exception;

import com.quary.bookyourinstructor.model.exception.ErrorCode;

public enum EventErrorCode implements ErrorCode {
    INVALID_CYCLIC_EVENT_BOUNDARIES,
    EVENT_BOOKING_ALREADY_LOCKED,
    EVENT_CHANGED,
    EVENT_BOOK_LOCK_EXPIRED,
    COLLIDING_CYCLIC_EVENT_REALIZATION
}
