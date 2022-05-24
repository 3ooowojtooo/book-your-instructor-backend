package com.quary.bookyourinstructor.model.event.exception;

import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;

public class InvalidCyclicEventBoundariesException extends BaseInvalidInputException {

    public InvalidCyclicEventBoundariesException() {
        super(EventErrorCode.INVALID_CYCLIC_EVENT_BOUNDARIES, "None event realizations found within given date boundaries for new cyclic event");
    }
}
