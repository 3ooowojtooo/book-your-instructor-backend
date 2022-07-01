package com.quary.bookyourinstructor.configuration.retry;

import bookyourinstructor.usecase.event.common.exception.ConcurrentDataModificationRuntimeException;
import bookyourinstructor.usecase.util.retry.RetryInstanceName;
import bookyourinstructor.usecase.util.retry.RetryManager;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Component
public class RetryManagerImpl implements RetryManager {

    private final RetryRegistry retryRegistry;

    @Override
    public <T> T runInLockRetry(RetryInstanceName retryName, Supplier<T> action) throws ConcurrentDataModificationRuntimeException{
        try {
            Retry retry = getRetry(retryName);
            return retry.executeSupplier(action);
        } catch (PessimisticLockingFailureException e) {
            throw new ConcurrentDataModificationRuntimeException();
        }
    }

    @Override
    public void runInLockRetry(RetryInstanceName retryName, Runnable action) throws ConcurrentDataModificationRuntimeException {
        try {
            Retry retry = getRetry(retryName);
            retry.executeRunnable(action);
        } catch (PessimisticLockingFailureException e) {
            throw new ConcurrentDataModificationRuntimeException();
        }
    }

    private Retry getRetry(RetryInstanceName retryName) {
        return retryRegistry.retry(retryName.name());
    }
}
