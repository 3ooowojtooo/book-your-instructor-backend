package com.quary.bookyourinstructor.model.event.exception;

import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;

public class CyclicEventNoFutureRealizationsFoundException extends BaseInvalidInputException {

    public CyclicEventNoFutureRealizationsFoundException() {
        super(EventErrorCode.CYCLIC_EVENT_FUTURE_REALIZATIONS_NOT_FOUND, "No future cyclic event realizations were found");
    }
}
