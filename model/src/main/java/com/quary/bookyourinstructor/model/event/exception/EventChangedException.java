package com.quary.bookyourinstructor.model.event.exception;

import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;

public class EventChangedException extends BaseInvalidInputException {

    public EventChangedException() {
        super(EventErrorCode.EVENT_CHANGED_EXCEPTION, "Event has been changed (versions mismatch)");
    }
}
