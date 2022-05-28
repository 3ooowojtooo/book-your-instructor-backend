package bookyourinstructor.usecase.event.booklock.exception;

public class EventChangedRuntimeException extends RuntimeException {

    public EventChangedRuntimeException() {
        super("Event has been changed (versions mismatch)");
    }
}
