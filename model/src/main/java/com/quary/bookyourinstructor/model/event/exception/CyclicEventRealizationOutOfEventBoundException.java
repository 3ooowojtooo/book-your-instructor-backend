package com.quary.bookyourinstructor.model.event.exception;

import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;

public class CyclicEventRealizationOutOfEventBoundException extends BaseInvalidInputException {

    public CyclicEventRealizationOutOfEventBoundException(Throwable cause) {
        super(EventErrorCode.CYCLIC_EVENT_REALIZATION_BOUNDARIES_OUT_OF_EVENT_BOUNDARIES,
                "Cyclic event realization new time boundaries are out of event's time bounds", cause);
    }
}
