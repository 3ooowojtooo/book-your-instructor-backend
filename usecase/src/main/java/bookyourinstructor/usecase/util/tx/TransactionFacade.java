package bookyourinstructor.usecase.util.tx;

import bookyourinstructor.usecase.util.tx.exception.ConstraintViolationException;

import java.util.function.Supplier;

public interface TransactionFacade {

    <T> T executeInTransaction(Supplier<T> action) throws ConstraintViolationException;

    <T> T executeInTransaction(TransactionPropagation transactionPropagation, Supplier<T> action) throws ConstraintViolationException;
}
