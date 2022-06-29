package bookyourinstructor.usecase.event.common.exception;

public class ConcurrentDataModificationRuntimeException extends RuntimeException {

    public ConcurrentDataModificationRuntimeException() {
        super("Data has been concurrently modified.");
    }
}
