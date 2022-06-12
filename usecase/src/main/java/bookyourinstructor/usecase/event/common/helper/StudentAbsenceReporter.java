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
                validateEventNotStarted(eventRealization, now);
                validateEventRealizationOwner(eventRealization, data.getUser().getId());
                Event event = findEventWithLockForUpdateOrThrow(eventRealization.getEventId());
                validateEventVersion(event, data.getEventVersion());
                EventStudentAbsence absence = buildAbsence(event, eventRealization, data.getUser().getId());
                eventStudentAbsenceStore.save(absence);
                if (event.getType() == EventType.SINGLE) {
                    eventStore.setStatusByIdAndIncrementVersion(event.getId(), EventStatus.FREE);
                    eventRealizationStore.setStudentIdForEventRealization(null, eventRealization.getId());
                } else if (event.getType() == EventType.CYCLIC) {
                    eventStore.incrementVersion(event.getId());
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

    private static void validateEventNotStarted(EventRealization eventRealization, Instant now) {
        checkState(now.isBefore(eventRealization.getStart()), "You can report absence only on not started events");
    }

    private static void validateEventRealizationOwner(EventRealization eventRealization, Integer ownerId) {
        checkArgument(Objects.equals(eventRealization.getStudentId(), ownerId), "You can only report absent for your own events");
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
}
