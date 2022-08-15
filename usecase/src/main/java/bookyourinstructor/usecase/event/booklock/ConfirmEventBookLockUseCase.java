package bookyourinstructor.usecase.event.booklock;

import bookyourinstructor.usecase.event.booklock.data.ConfirmEventBookLockData;
import bookyourinstructor.usecase.event.booklock.exception.EventBookLockExpiredRuntimeException;
import bookyourinstructor.usecase.event.common.exception.ConcurrentDataModificationRuntimeException;
import bookyourinstructor.usecase.event.common.exception.EventChangedRuntimeException;
import bookyourinstructor.usecase.event.common.store.EventLockStore;
import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.event.schedule.helper.ScheduleCreatingHelper;
import bookyourinstructor.usecase.util.retry.RetryInstanceName;
import bookyourinstructor.usecase.util.retry.RetryManager;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.EventLock;
import com.quary.bookyourinstructor.model.event.EventRealizationStatus;
import com.quary.bookyourinstructor.model.event.EventStatus;
import com.quary.bookyourinstructor.model.event.exception.ConcurrentDataModificationException;
import com.quary.bookyourinstructor.model.event.exception.EventBookLockExpiredException;
import com.quary.bookyourinstructor.model.event.exception.EventChangedException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkState;

@RequiredArgsConstructor
public class ConfirmEventBookLockUseCase {

    private final TransactionFacade transactionFacade;
    private final TimeUtils timeUtils;
    private final RetryManager retryManager;
    private final EventStore eventStore;
    private final EventLockStore eventLockStore;
    private final EventRealizationStore eventRealizationStore;
    private final ScheduleCreatingHelper scheduleCreatingHelper;

    public void confirmEventBookLock(ConfirmEventBookLockData data) throws EventChangedException, EventBookLockExpiredException,
            ConcurrentDataModificationException {
        final Instant now = timeUtils.nowInstant();
        try {
            retryManager.runInLockRetry(RetryInstanceName.CONFIRM_BOOK_LOCK, () -> confirmEventBookLockInternal(data, now));
        } catch (EventChangedRuntimeException e) {
            throw new EventChangedException();
        } catch (EventBookLockExpiredRuntimeException e) {
            throw new EventBookLockExpiredException();
        } catch (ConcurrentDataModificationRuntimeException e) {
            throw new ConcurrentDataModificationException();
        }
    }

    private void confirmEventBookLockInternal(ConfirmEventBookLockData data, Instant now) {
        transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.REPEATABLE_READ, () -> {
            final EventLock eventLock = findEventLockOrThrow(data.getBookLockId());
            validateEventLockOwner(eventLock, data.getStudentId());
            final Event event = findEventWithLockForUpdateOrThrow(eventLock.getEventId());
            validateEventVersion(event, eventLock.getEventVersion());
            validateEventFree(event);
            validateEventBookLockValidity(eventLock, now);
            eventRealizationStore.setStudentIdForEventRealizations(data.getStudentId(), event.getId());
            eventRealizationStore.setStatusForEventRealizationsWithStatus(EventRealizationStatus.FREE, EventRealizationStatus.BOOKED, event.getId());
            eventLockStore.deleteById(eventLock.getId());
            eventStore.setStatusAndStudentByIdAndIncrementVersion(event.getId(), data.getStudentId(), EventStatus.BOOKED);
            scheduleCreatingHelper.handleEventBooked(event, data.getStudentId());
        });
    }

    private EventLock findEventLockOrThrow(Integer id) {
        return eventLockStore.findById(id)
                .orElseThrow(EventBookLockExpiredRuntimeException::new);
    }

    private void validateEventLockOwner(EventLock eventLock, Integer ownerId) {
        checkState(Objects.equals(eventLock.getUserId(), ownerId), "You can confirm only book lock that you have previously booked");
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
