package bookyourinstructor.usecase.event.cyclic.exception;

public class NoRealizationsOfCyclicEventFoundRuntimeException extends RuntimeException {

    public NoRealizationsOfCyclicEventFoundRuntimeException() {
        super("No cyclic event realizations were found");
    }
}
