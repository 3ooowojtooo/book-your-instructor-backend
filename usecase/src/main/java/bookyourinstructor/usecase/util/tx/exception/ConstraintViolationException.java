package bookyourinstructor.usecase.util.tx.exception;

import lombok.Getter;

@Getter
public class ConstraintViolationException extends RuntimeException {

    private final String constraintName;

    public ConstraintViolationException(String constraintName, Throwable cause) {
        super("Database constraint violated. Name of the constraint: " + constraintName, cause);
        this.constraintName = constraintName;
    }
}
