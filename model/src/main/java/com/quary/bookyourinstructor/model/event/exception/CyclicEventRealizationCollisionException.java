package com.quary.bookyourinstructor.model.event.exception;

import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;
import lombok.Getter;

import java.util.List;

@Getter
public class CyclicEventRealizationCollisionException extends BaseInvalidInputException {

    private final List<EventRealization> collidingRealizations;

    public CyclicEventRealizationCollisionException(List<EventRealization> collidingRealizations, Throwable cause) {
        super(EventErrorCode.COLLIDING_CYCLIC_EVENT_REALIZATION, "Cyclic event realization collides with other realizations: " + collidingRealizations,
                cause);
        this.collidingRealizations = collidingRealizations;
    }
}
