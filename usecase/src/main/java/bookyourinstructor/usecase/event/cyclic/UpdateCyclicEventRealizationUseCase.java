package bookyourinstructor.usecase.event.cyclic;

import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.event.cyclic.data.UpdateCyclicEventRealizationData;
import bookyourinstructor.usecase.event.cyclic.exception.CyclicEventRealizationCollisionRuntimeException;
import bookyourinstructor.usecase.event.cyclic.exception.CyclicEventRealizationOutOfEventBoundRuntimeException;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import com.quary.bookyourinstructor.model.event.*;
import com.quary.bookyourinstructor.model.event.exception.CyclicEventRealizationCollisionException;
import com.quary.bookyourinstructor.model.event.exception.CyclicEventRealizationOutOfEventBoundException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

@RequiredArgsConstructor
public class UpdateCyclicEventRealizationUseCase {

    private final EventStore eventStore;
    private final EventRealizationStore eventRealizationStore;
    private final TransactionFacade transactionFacade;
    private final TimeUtils timeUtils;

    public void updateCyclicEventRealization(final UpdateCyclicEventRealizationData data)
            throws CyclicEventRealizationCollisionException, CyclicEventRealizationOutOfEventBoundException {
        try {
            transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.READ_COMMITTED, () -> {
                EventRealization eventRealization = findEventRealizationOrThrow(data.getEventRealizationId());
                Event event = findEventOrThrow(eventRealization.getEventId());
                CyclicEvent cyclicEvent = validateEventCyclic(event);
                validateNewRealizationTimeNotBeforeCreationTime(data, cyclicEvent);
                validateEventOwner(event, data.getInstructorId());
                validateEventRealizationDraft(eventRealization);
                validateNewRealizationTimeWithinEventBoundaries(cyclicEvent, data.getStart(), data.getEnd());
                validateNoCollisionWithOtherRealizations(eventRealization, data.getStart(), data.getEnd());
                eventRealization.setNewTimeBoundaries(data.getStart(), data.getEnd());
                eventRealizationStore.saveEventRealization(eventRealization);
                return null;
            });
        } catch (CyclicEventRealizationCollisionRuntimeException e) {
            throw new CyclicEventRealizationCollisionException(e.getCollidingRealizations(), e);
        } catch (CyclicEventRealizationOutOfEventBoundRuntimeException e) {
            throw new CyclicEventRealizationOutOfEventBoundException(e);
        }
    }

    private EventRealization findEventRealizationOrThrow(final Integer eventRealizationId) {
        return eventRealizationStore.findById(eventRealizationId)
                .orElseThrow(() -> new IllegalArgumentException("Event realization with id " + eventRealizationId + " was not found"));
    }

    private Event findEventOrThrow(final Integer eventId) {
        return eventStore.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event with id " + eventId + " was not found"));
    }

    private CyclicEvent validateEventCyclic(Event event) {
        checkState(event.getType() == EventType.CYCLIC, "You can only change realization of cyclic event");
        return (CyclicEvent) event;
    }

    private void validateEventRealizationDraft(EventRealization eventRealization) {
        checkState(eventRealization.getStatus() == EventRealizationStatus.DRAFT, "You can only change draft event realization");
    }

    private void validateNewRealizationTimeNotBeforeCreationTime(UpdateCyclicEventRealizationData data, Event event) {
        checkArgument(!data.getStart().isBefore(event.getCreatedAt()) && !data.getEnd().isBefore(event.getCreatedAt()),
                "New cyclic event start / end date time cannot be before event creation time: " + event.getCreatedAt());
    }

    private void validateEventOwner(Event event, Integer ownerId) {
        checkArgument(Objects.equals(event.getInstructorId(), ownerId), "You can only change realization of your event");
    }

    private void validateNewRealizationTimeWithinEventBoundaries(CyclicEvent event, Instant newStart, Instant newEnd)
            throws CyclicEventRealizationOutOfEventBoundRuntimeException {
        LocalDateTime newStartLocalDateTime = timeUtils.toLocalDateTimeUTCZone(newStart);
        LocalDateTime newEndLocalDateTime = timeUtils.toLocalDateTimeUTCZone(newEnd);
        if (newStartLocalDateTime.isBefore(event.getStartBoundary()) || newEndLocalDateTime.isAfter(event.getEndBoundary())) {
            throw new CyclicEventRealizationOutOfEventBoundRuntimeException();
        }
    }

    private void validateNoCollisionWithOtherRealizations(EventRealization currentRealization, Instant newStart, Instant newEnd)
            throws CyclicEventRealizationCollisionRuntimeException {
        List<EventRealization> eventRealizations = eventRealizationStore.findAllRealizations(currentRealization.getEventId());
        List<EventRealization> collidingRealizations = eventRealizations.stream()
                .filter(realization -> !realization.equals(currentRealization))
                .filter(realization -> realization.collidesWithTimeBoundaries(newStart, newEnd))
                .collect(Collectors.toList());
        if (!collidingRealizations.isEmpty()) {
            throw new CyclicEventRealizationCollisionRuntimeException(collidingRealizations);
        }
    }
}
