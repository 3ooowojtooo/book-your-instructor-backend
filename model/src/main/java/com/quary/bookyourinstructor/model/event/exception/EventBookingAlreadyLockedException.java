package com.quary.bookyourinstructor.model.event.exception;

import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;

public class EventBookingAlreadyLockedException extends BaseInvalidInputException {

    private static final String MESSAGE = "Other user has locked booking of this event. Please try again in few minutes.";

    public EventBookingAlreadyLockedException(Throwable cause) {
        super(EventErrorCode.EVENT_BOOKING_ALREADY_LOCKED, MESSAGE, cause);
    }
}
