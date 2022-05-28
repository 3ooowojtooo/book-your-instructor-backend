package bookyourinstructor.usecase.util.tx;

public enum TransactionIsolation {
    DEFAULT,
    READ_UNCOMMITTED,
    READ_COMMITTED,
    REPEATABLE_READ,
    SERIALIZABLE
}
