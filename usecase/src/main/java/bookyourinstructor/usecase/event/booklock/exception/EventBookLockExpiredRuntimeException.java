package bookyourinstructor.usecase.event.booklock.exception;

public class EventBookLockExpiredRuntimeException extends RuntimeException {

    public EventBookLockExpiredRuntimeException() {
        super("Event book lock expired. Try book the event again.");
    }
}
