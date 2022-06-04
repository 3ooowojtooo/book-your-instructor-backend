package bookyourinstructor.usecase.event.cyclic.exception;

public class CyclicEventRealizationOutOfEventBoundRuntimeException extends RuntimeException {

    public CyclicEventRealizationOutOfEventBoundRuntimeException() {
        super("Cyclic event realization new time boundaries are out of event's time bounds");
    }
}
