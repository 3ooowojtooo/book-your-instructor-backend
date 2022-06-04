package bookyourinstructor.usecase.event.common;

import bookyourinstructor.usecase.event.common.data.AcceptEventData;
import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.EventRealizationStatus;
import com.quary.bookyourinstructor.model.event.EventStatus;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkState;

@RequiredArgsConstructor
public class AcceptEventUseCase {

    private final EventStore eventStore;
    private final EventRealizationStore eventRealizationStore;
    private final TransactionFacade transactionFacade;

    public void acceptEvent(AcceptEventData data) {
        transactionFacade.executeInTransaction(() -> {
            Event event = findEvent(data.getEventId());
            validateEventDraft(event);
            validateEventOwner(event, data.getUserId());
            eventStore.setStatusById(data.getEventId(), EventStatus.FREE);
            eventRealizationStore.setStatusForEventRealizations(EventRealizationStatus.ACCEPTED, data.getEventId());
            return null;
        });
    }

    private Event findEvent(Integer id) {
        return eventStore.findById(id)
                .orElseThrow(() -> new IllegalStateException("Event with id " + id + " was not found"));
    }

    private void validateEventDraft(Event event) {
        checkState(event.getStatus() == EventStatus.DRAFT, "Only draft event can be accepted");
    }

    private void validateEventOwner(Event event, Integer ownerId) {
        checkState(Objects.equals(event.getInstructorId(), ownerId), "You can accept only yours event");
    }
}
