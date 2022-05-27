package bookyourinstructor.usecase.event.booklock;

import bookyourinstructor.usecase.event.booklock.exception.EventChangedRuntimeException;
import bookyourinstructor.usecase.event.store.EventLockStore;
import bookyourinstructor.usecase.event.store.EventStore;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.exception.ConstraintViolationException;
import com.quary.bookyourinstructor.model.event.CreateEventBookLockData;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.EventLock;
import com.quary.bookyourinstructor.model.event.exception.EventBookingAlreadyLockedException;
import com.quary.bookyourinstructor.model.event.exception.EventChangedException;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@RequiredArgsConstructor
public class CreateEventBookLockUseCase {

    private final TransactionFacade transactionFacade;
    private final TimeUtils timeUtils;
    private final EventLockStore eventLockStore;
    private final EventStore eventStore;
    private final Duration lockExpirationDuration;

    public EventLock createEventBookLock(CreateEventBookLockData data) throws EventBookingAlreadyLockedException, EventChangedException {
        try {
            final Instant now = timeUtils.nowInstant();
            final Instant expirationTime = now.plus(lockExpirationDuration);
            return transactionFacade.executeInTransaction(() -> {
                final Event event = findEventOrThrow(data.getEventId());
                validateEventVersion(event, data.getEventVersion());
                final EventLock eventLock = buildEventLock(event, data, expirationTime);
                return eventLockStore.saveEventLock(eventLock);
            });
        } catch (EventChangedRuntimeException e) {
            throw new EventChangedException();
        } catch (ConstraintViolationException ex) {
            throw new EventBookingAlreadyLockedException(ex);
        }
    }

    private Event findEventOrThrow(Integer id) {
        return eventStore.getById(id)
                .orElseThrow(() -> new IllegalStateException("Event with id " + id + " was not found"));
    }

    private void validateEventVersion(Event event, Integer version) throws EventChangedRuntimeException {
        if (!Objects.equals(event.getVersion(), version)) {
            throw new EventChangedRuntimeException();
        }
    }

    private EventLock buildEventLock(Event event, CreateEventBookLockData data, Instant lockExpirationTime) {
        return EventLock.newLock(event.getId(), event.getVersion(), data.getStudentId(), lockExpirationTime);
    }
}
