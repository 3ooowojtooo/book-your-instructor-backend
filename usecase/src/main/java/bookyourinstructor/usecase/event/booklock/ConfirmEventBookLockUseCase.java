package bookyourinstructor.usecase.event.booklock;

import bookyourinstructor.usecase.event.booklock.data.ConfirmEventBookLockData;
import bookyourinstructor.usecase.event.booklock.exception.EventBookLockExpiredRuntimeException;
import bookyourinstructor.usecase.event.common.exception.EventChangedRuntimeException;
import bookyourinstructor.usecase.event.store.EventLockStore;
import bookyourinstructor.usecase.event.store.EventRealizationStore;
import bookyourinstructor.usecase.event.store.EventStore;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.EventLock;
import com.quary.bookyourinstructor.model.event.EventStatus;
import com.quary.bookyourinstructor.model.event.exception.EventBookLockExpiredException;
import com.quary.bookyourinstructor.model.event.exception.EventChangedException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Objects;

@RequiredArgsConstructor
public class ConfirmEventBookLockUseCase {

    private final TransactionFacade transactionFacade;
    private final TimeUtils timeUtils;
    private final EventStore eventStore;
    private final EventLockStore eventLockStore;
    private final EventRealizationStore eventRealizationStore;

    public void confirmEventBookLock(ConfirmEventBookLockData data) throws EventChangedException, EventBookLockExpiredException {
        final Instant now = timeUtils.nowInstant();
        try {
            transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.REPEATABLE_READ, () -> {
                final EventLock eventLock = findEventLock(data.getBookLockId());
                final Event event = findEventWithLockForUpdateOrThrow(eventLock.getEventId());
                validateEventVersion(event, eventLock.getEventVersion());
                validateEventFree(event);
                validateEventBookLockValidity(eventLock, now);
                eventRealizationStore.setStudentIdForEventRealizations(data.getStudentId(), event.getId());
                eventLockStore.deleteById(eventLock.getId());
                eventStore.setStatusById(event.getId(), EventStatus.BOOKED);
                return null;
            });
        } catch (EventChangedRuntimeException e) {
            throw new EventChangedException();
        } catch (EventBookLockExpiredRuntimeException e) {
            throw new EventBookLockExpiredException();
        }
    }

    private EventLock findEventLock(Integer id) {
        return eventLockStore.findById(id)
                .orElseThrow(() -> new IllegalStateException("Event lock with id " + id + " was not found"));
    }

    private Event findEventWithLockForUpdateOrThrow(Integer id) {
        return eventStore.findByIdWithLockForUpdate(id)
                .orElseThrow(() -> new IllegalStateException("Event with id " + id + " was not found"));
    }

    private void validateEventVersion(Event event, Integer version) throws EventChangedRuntimeException {
        if (!Objects.equals(event.getVersion(), version)) {
            throw new EventChangedRuntimeException();
        }
    }

    private void validateEventFree(Event event) {
        if (event.getStatus() != EventStatus.FREE) {
            throw new IllegalStateException("Event with id " + event.getId() + " is not free, so it cannot be booked.");
        }
    }

    private void validateEventBookLockValidity(EventLock eventLock, Instant now) throws EventBookLockExpiredRuntimeException {
        if (eventLock.getExpirationTime().isBefore(now) || eventLock.getExpirationTime().equals(now)) {
            throw new EventBookLockExpiredRuntimeException();
        }
    }

}
