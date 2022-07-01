package bookyourinstructor.usecase.util.retry;

import bookyourinstructor.usecase.event.common.exception.ConcurrentDataModificationRuntimeException;

import java.util.function.Supplier;

public interface RetryManager {

    <T> T runInLockRetry(RetryInstanceName retryName, Supplier<T> action) throws ConcurrentDataModificationRuntimeException;

    void runInLockRetry(RetryInstanceName retryName, Runnable action) throws ConcurrentDataModificationRuntimeException;
}
