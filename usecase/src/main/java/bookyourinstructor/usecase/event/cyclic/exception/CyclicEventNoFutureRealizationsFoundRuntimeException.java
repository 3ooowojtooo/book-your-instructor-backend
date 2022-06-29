package bookyourinstructor.usecase.event.cyclic.exception;

public class CyclicEventNoFutureRealizationsFoundRuntimeException extends RuntimeException {

    public CyclicEventNoFutureRealizationsFoundRuntimeException() {
        super("No future cyclic event realizations were found");
    }
}
