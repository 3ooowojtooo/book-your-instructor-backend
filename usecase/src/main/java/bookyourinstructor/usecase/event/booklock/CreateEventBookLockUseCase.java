package bookyourinstructor.usecase.event.booklock;

import bookyourinstructor.usecase.event.booklock.data.CreateEventBookLockData;
import bookyourinstructor.usecase.event.common.exception.ConcurrentDataModificationRuntimeException;
import bookyourinstructor.usecase.event.common.exception.EventChangedRuntimeException;
import bookyourinstructor.usecase.event.common.store.EventLockStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.util.retry.RetryInstanceName;
import bookyourinstructor.usecase.util.retry.RetryManager;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import bookyourinstructor.usecase.util.tx.exception.ConstraintViolationException;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.EventLock;
import com.quary.bookyourinstructor.model.event.exception.ConcurrentDataModificationException;
import com.quary.bookyourinstructor.model.event.exception.EventBookingAlreadyLockedException;
import com.quary.bookyourinstructor.model.event.exception.EventChangedException;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import static com.quary.bookyourinstructor.model.event.EventStatus.FREE;

@RequiredArgsConstructor
public class CreateEventBookLockUseCase {

    private final TransactionFacade transactionFacade;
    private final TimeUtils timeUtils;
    private final RetryManager retryManager;
    private final EventLockStore eventLockStore;
    private final EventStore eventStore;
    private final Duration lockExpirationDuration;

    public EventLock createEventBookLock(CreateEventBookLockData data) throws EventBookingAlreadyLockedException,
            EventChangedException, ConcurrentDataModificationException {
        try {
            final Instant now = timeUtils.nowInstant();
            return retryManager.runInLockRetry(RetryInstanceName.CREATE_BOOK_LOCK, () -> createEventBookLockInternal(data, now));
        } catch (EventChangedRuntimeException e) {
            throw new EventChangedException();
        } catch (ConstraintViolationException ex) {
            throw new EventBookingAlreadyLockedException(ex);
        } catch (ConcurrentDataModificationRuntimeException ex) {
            throw new ConcurrentDataModificationException();
        }
    }

    private EventLock createEventBookLockInternal(CreateEventBookLockData data, Instant now) {
        final Instant lockExpirationTime = now.plus(lockExpirationDuration);
        return transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.REPEATABLE_READ, () -> {
            eventLockStore.deleteByEventIdAndPastExpirationTime(data.getEventId(), now);
            final Event event = findEventWithLockForShareOrThrow(data.getEventId());
            validateEventVersion(event, data.getEventVersion());
            validateEventFree(event);
            final EventLock eventLock = buildEventLock(event, data, lockExpirationTime);
            return eventLockStore.saveEventLock(eventLock);
        });
    }

    private Event findEventWithLockForShareOrThrow(Integer id) {
        return eventStore.findByIdWithLockForShare(id)
                .orElseThrow(() -> new IllegalStateException("Event with id " + id + " was not found"));
    }

    private void validateEventVersion(Event event, Integer version) throws EventChangedRuntimeException {
        if (!Objects.equals(event.getVersion(), version)) {
            throw new EventChangedRuntimeException();
        }
    }

    private void validateEventFree(Event event) {
        if (event.getStatus() != FREE) {
            throw new IllegalStateException("Event with id " + event.getId() + " is not free.");
        }
    }

    private EventLock buildEventLock(Event event, CreateEventBookLockData data, Instant lockExpirationTime) {
        return EventLock.newLock(event.getId(), event.getVersion(), data.getStudentId(), lockExpirationTime);
    }
}
