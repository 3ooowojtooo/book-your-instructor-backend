package bookyourinstructor.usecase.event.cyclic;

import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.event.cyclic.data.UpdateCyclicEventRealizationData;
import bookyourinstructor.usecase.event.cyclic.exception.CyclicEventRealizationCollisionRuntimeException;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.EventRealizationStatus;
import com.quary.bookyourinstructor.model.event.EventType;
import com.quary.bookyourinstructor.model.event.exception.CyclicEventRealizationCollisionException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
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

    public void updateCyclicEventRealization(final UpdateCyclicEventRealizationData data) throws CyclicEventRealizationCollisionException {
        try {
            transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.READ_COMMITTED, () -> {
                EventRealization eventRealization = findEventRealizationOrThrow(data.getEventRealizationId());
                Event event = findEventOrThrow(eventRealization.getEventId());
                validateEventCyclic(event);
                validateEventOwner(event, data.getInstructorId());
                validateEventRealizationDraft(eventRealization);
                validateNoCollisionWithOtherRealizations(eventRealization, data.getStart(), data.getEnd());
                eventRealization.setNewTimeBoundaries(data.getStart(), data.getEnd());
                eventRealizationStore.saveEventRealization(eventRealization);
                return null;
            });
        } catch (CyclicEventRealizationCollisionRuntimeException e) {
            throw new CyclicEventRealizationCollisionException(e.getCollidingRealizations(), e);
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

    private void validateEventCyclic(Event event) {
        checkState(event.getType() == EventType.CYCLIC, "You can only change realization of cyclic event");
    }

    private void validateEventRealizationDraft(EventRealization eventRealization) {
        checkState(eventRealization.getStatus() == EventRealizationStatus.DRAFT, "You can only change draft event realization");
    }

    private void validateEventOwner(Event event, Integer ownerId) {
        checkArgument(Objects.equals(event.getInstructorId(), ownerId), "You can only change realization of your event");
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
