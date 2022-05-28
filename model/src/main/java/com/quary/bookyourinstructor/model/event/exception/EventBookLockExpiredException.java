package com.quary.bookyourinstructor.model.event.exception;

import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;

public class EventBookLockExpiredException extends BaseInvalidInputException {

    public EventBookLockExpiredException() {
        super(EventErrorCode.EVENT_BOOK_LOCK_EXPIRED, "Event book lock expired. Try book the event again.");
    }
}
