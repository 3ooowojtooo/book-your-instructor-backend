package bookyourinstructor.usecase.event.cyclic.exception;

import com.quary.bookyourinstructor.model.event.EventRealization;
import lombok.Getter;

import java.util.List;

@Getter
public class CyclicEventRealizationCollisionRuntimeException extends RuntimeException {

    private final List<EventRealization> collidingRealizations;

    public CyclicEventRealizationCollisionRuntimeException(List<EventRealization> collidingRealizations) {
        super("Cyclic event realization collides with other realizations: " + collidingRealizations);
        this.collidingRealizations = collidingRealizations;
    }
}
