package com.quary.bookyourinstructor.configuration.tx;

import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import bookyourinstructor.usecase.util.tx.exception.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TransactionFacadeImpl implements TransactionFacade {

    private final PlatformTransactionManager platformTransactionManager;

    @Override
    public <T> T executeInTransaction(TransactionPropagation transactionPropagation, TransactionIsolation transactionIsolation,
                                      Supplier<T> action) throws ConstraintViolationException {
        try {
            TransactionTemplate template = new TransactionTemplate(platformTransactionManager);
            template.setPropagationBehavior(mapToPropagationBehaviour(transactionPropagation));
            template.setIsolationLevel(mapToIsolationLevel(transactionIsolation));
            return template.execute(status -> action.get());
        } catch (DataIntegrityViolationException ex) {
            throwIfConstraintViolationException(ex.getCause());
            throw ex;
        }
    }

    @Override
    public <T> T executeInTransaction(final Supplier<T> action) throws ConstraintViolationException {
        return executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.DEFAULT, action);
    }

    private static int mapToPropagationBehaviour(TransactionPropagation propagation) {
        switch (propagation) {
            case REQUIRED:
                return TransactionDefinition.PROPAGATION_REQUIRED;
            case SUPPORTS:
                return TransactionDefinition.PROPAGATION_SUPPORTS;
            case MANDATORY:
                return TransactionDefinition.PROPAGATION_MANDATORY;
            case REQUIRES_NEW:
                return TransactionDefinition.PROPAGATION_REQUIRES_NEW;
            case NOT_SUPPORTED:
                return TransactionDefinition.PROPAGATION_NOT_SUPPORTED;
            case NEVER:
                return TransactionDefinition.PROPAGATION_NEVER;
            case NESTED:
                return TransactionDefinition.PROPAGATION_NESTED;
            default:
                throw new IllegalStateException("Unknown transaction propagation level: " + propagation);
        }
    }

    private static int mapToIsolationLevel(TransactionIsolation transactionIsolation) {
        switch (transactionIsolation) {
            case DEFAULT:
                return TransactionDefinition.ISOLATION_DEFAULT;
            case READ_UNCOMMITTED:
                return TransactionDefinition.ISOLATION_READ_UNCOMMITTED;
            case READ_COMMITTED:
                return TransactionDefinition.ISOLATION_READ_COMMITTED;
            case REPEATABLE_READ:
                return TransactionDefinition.ISOLATION_REPEATABLE_READ;
            case SERIALIZABLE:
                return TransactionDefinition.ISOLATION_SERIALIZABLE;
            default:
                throw new IllegalStateException("Unknown transaction isolation level: " + transactionIsolation);
        }
    }

    private static void throwIfConstraintViolationException(Throwable ex) throws ConstraintViolationException {
        if (ex instanceof org.hibernate.exception.ConstraintViolationException) {
            final String constraintName = ((org.hibernate.exception.ConstraintViolationException) ex).getConstraintName();
            throw new ConstraintViolationException(constraintName, ex);
        }
    }

}
