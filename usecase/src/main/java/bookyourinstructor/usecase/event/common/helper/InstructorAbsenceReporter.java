package bookyourinstructor.usecase.event.common.helper;

import bookyourinstructor.usecase.event.common.data.ReportAbsenceData;
import bookyourinstructor.usecase.event.common.exception.ConcurrentDataModificationRuntimeException;
import bookyourinstructor.usecase.event.common.exception.EventChangedRuntimeException;
import bookyourinstructor.usecase.event.common.port.UserData;
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
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.EventRealizationStatus;
import com.quary.bookyourinstructor.model.event.exception.ConcurrentDataModificationException;
import com.quary.bookyourinstructor.model.event.exception.EventChangedException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

@RequiredArgsConstructor
public class InstructorAbsenceReporter {

    private final EventStore eventStore;
    private final EventRealizationStore eventRealizationStore;
    private final TimeUtils timeUtils;
    private final TransactionFacade transactionFacade;
    private final RetryManager retryManager;
    private final ScheduleCreatingHelper scheduleCreatingHelper;

    public void reportAbsence(ReportAbsenceData data) throws EventChangedException, ConcurrentDataModificationException {
        Instant now = timeUtils.nowInstant();
        validateUserInstructor(data.getUser());
        try {
            retryManager.runInLockRetry(RetryInstanceName.REPORT_ABSENCE_INSTRUCTOR, () -> reportAbsenceInternal(data, now));
        } catch (EventChangedRuntimeException e) {
            throw new EventChangedException();
        } catch (ConcurrentDataModificationRuntimeException e) {
            throw new ConcurrentDataModificationException();
        }
    }

    private static void validateUserInstructor(UserData user) {
        checkArgument(user.isInstructor(), "User is not an instructor");
    }

    private void reportAbsenceInternal(ReportAbsenceData data, Instant now) {
        transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.REPEATABLE_READ, () -> {
            EventRealization eventRealization = findEventRealizationWithLockForUpdateOrThrow(data.getEventRealizationId());
            validateEventRealizationStatus(eventRealization);
            validateEventRealizationNotStarted(eventRealization, now);
            Event event = findEventWithLockForUpdateOrThrow(eventRealization.getEventId());
            validateEventOwner(event, data.getUser().getId());
            validateEventVersion(event, data.getEventVersion());
            eventRealizationStore.setStatusForEventRealization(EventRealizationStatus.INSTRUCTOR_ABSENT, eventRealization.getId());
            eventStore.incrementVersion(event.getId());
            scheduleCreatingHelper.handleInstructorAbsence(event, eventRealization);
        });
    }

    private EventRealization findEventRealizationWithLockForUpdateOrThrow(final Integer eventRealizationId) {
        return eventRealizationStore.findByIdWithLockForUpdate(eventRealizationId)
                .orElseThrow(() -> new IllegalArgumentException("Event realization with id " + eventRealizationId + " was not found"));
    }

    private static void validateEventRealizationStatus(EventRealization eventRealization) {
        checkState(eventRealization.getStatus() == EventRealizationStatus.BOOKED, "You can only report absence for booked event");
    }

    private static void validateEventRealizationNotStarted(EventRealization eventRealization, Instant now) {
        checkState(now.isBefore(eventRealization.getStart()), "You can report absence only on not started events");
    }

    private Event findEventWithLockForUpdateOrThrow(Integer id) {
        return eventStore.findByIdWithLockForUpdate(id)
                .orElseThrow(() -> new IllegalStateException("Event with id " + id + " was not found"));
    }

    private static void validateEventOwner(Event event, Integer ownerId) {
        checkArgument(Objects.equals(event.getInstructorId(), ownerId), "You can only change realization of your event");
    }

    private static void validateEventVersion(Event event, Integer version) throws EventChangedRuntimeException {
        if (!Objects.equals(event.getVersion(), version)) {
            throw new EventChangedRuntimeException();
        }
    }
}
