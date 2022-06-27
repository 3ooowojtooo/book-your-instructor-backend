package bookyourinstructor.usecase.event.common.helper;

import bookyourinstructor.usecase.event.common.data.ReportAbsenceData;
import bookyourinstructor.usecase.event.common.exception.EventChangedRuntimeException;
import bookyourinstructor.usecase.event.common.port.UserData;
import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.event.common.store.EventStudentAbsenceStore;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import com.quary.bookyourinstructor.model.event.*;
import com.quary.bookyourinstructor.model.event.exception.EventChangedException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

@RequiredArgsConstructor
public class StudentAbsenceReporter {

    private final EventStore eventStore;
    private final EventRealizationStore eventRealizationStore;
    private final EventStudentAbsenceStore eventStudentAbsenceStore;
    private final TransactionFacade transactionFacade;
    private final TimeUtils timeUtils;

    public void reportAbsence(ReportAbsenceData data) throws EventChangedException {
        Instant now = timeUtils.nowInstant();
        validateUserStudent(data.getUser());
        try {
            transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.REPEATABLE_READ, () -> {
                EventRealization eventRealization = findEventRealizationWithLockForUpdateOrThrow(data.getEventRealizationId());
                validateEventRealizationStatus(eventRealization);
                validateEventRealizationNotStarted(eventRealization, now);
                validateEventRealizationOwner(eventRealization, data.getUser().getId());
                validateEventRealizationNotReportedAbsence(eventRealization, data.getUser().getId());
                Event event = findEventWithLockForUpdateOrThrow(eventRealization.getEventId());
                validateEventVersion(event, data.getEventVersion());
                EventStudentAbsence absence = buildAbsence(event, eventRealization, data.getUser().getId());
                eventStudentAbsenceStore.save(absence);
                if (event.getType() == EventType.SINGLE) {
                    eventStore.setStatusByIdAndIncrementVersion(event.getId(), EventStatus.FREE);
                    eventRealizationStore.setStudentIdForEventRealization(null, eventRealization.getId());
                } else if (event.getType() == EventType.CYCLIC) {
                    eventStore.incrementVersion(event.getId());
                    declareAbsenceEventIfNecessary((CyclicEvent) event, eventRealization);
                }
                return null;
            });
        } catch (EventChangedRuntimeException e) {
            throw new EventChangedException();
        }
    }

    private static void validateUserStudent(UserData user) {
        checkArgument(user.isStudent(), "User is not a student");
    }

    private EventRealization findEventRealizationWithLockForUpdateOrThrow(final Integer eventRealizationId) {
        return eventRealizationStore.findByIdWithLockForUpdate(eventRealizationId)
                .orElseThrow(() -> new IllegalArgumentException("Event realization with id " + eventRealizationId + " was not found"));
    }

    private static void validateEventRealizationStatus(EventRealization eventRealization) {
        checkState(eventRealization.getStatus() == EventRealizationStatus.ACCEPTED, "You can only report abuse for accepted event");
    }

    private static void validateEventRealizationNotStarted(EventRealization eventRealization, Instant now) {
        checkState(now.isBefore(eventRealization.getStart()), "You can report absence only on not started events");
    }

    private static void validateEventRealizationOwner(EventRealization eventRealization, Integer ownerId) {
        checkArgument(Objects.equals(eventRealization.getStudentId(), ownerId), "You can only report absent for your own events");
    }

    private void validateEventRealizationNotReportedAbsence(EventRealization eventRealization, Integer studentId) {
        boolean absenceExists = eventStudentAbsenceStore.existsByRealizationIdAndStudentId(eventRealization.getId(), studentId);
        checkState(!absenceExists, "You already reported absence for this event");
    }

    private Event findEventWithLockForUpdateOrThrow(Integer id) {
        return eventStore.findByIdWithLockForUpdate(id)
                .orElseThrow(() -> new IllegalStateException("Event with id " + id + " was not found"));
    }

    private static void validateEventVersion(Event event, Integer version) throws EventChangedRuntimeException {
        if (!Objects.equals(event.getVersion(), version)) {
            throw new EventChangedRuntimeException();
        }
    }

    private static EventStudentAbsence buildAbsence(Event event, EventRealization eventRealization, Integer studentId) {
        return EventStudentAbsence.newAbsence(eventRealization.getId(), studentId, event.getName(), event.getDescription(),
                event.getLocation(), eventRealization.getStart(), eventRealization.getEnd());
    }

    private void declareAbsenceEventIfNecessary(CyclicEvent event, EventRealization eventRealization) {
        if (!event.isAbsenceEvent()) {
            return;
        }
        SingleEvent absenceEvent = buildAbsenceEvent(event, eventRealization);
        eventStore.saveSingleEvent(absenceEvent);
    }

    private SingleEvent buildAbsenceEvent(CyclicEvent event, EventRealization eventRealization) {
        LocalDateTime start = timeUtils.toLocalDateTimeUTCZone(eventRealization.getStart());
        LocalDateTime end = timeUtils.toLocalDateTimeUTCZone(eventRealization.getEnd());
        return SingleEvent.newSingleEventFree(event.getInstructorId(), event.getAbsenceEventName(), event.getAbsenceEventDescription(),
                event.getLocation(), event.getPrice(), start, end);
    }
}
