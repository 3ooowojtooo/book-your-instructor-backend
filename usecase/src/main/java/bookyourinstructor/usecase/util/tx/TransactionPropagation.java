package bookyourinstructor.usecase.util.tx;

public enum TransactionPropagation {
    REQUIRED,
    SUPPORTS,
    MANDATORY,
    REQUIRES_NEW,
    NOT_SUPPORTED,
    NEVER,
    NESTED
}
