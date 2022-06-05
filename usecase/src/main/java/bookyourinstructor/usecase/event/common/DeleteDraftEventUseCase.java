package bookyourinstructor.usecase.event.common;

import bookyourinstructor.usecase.event.common.data.DeleteDraftEventData;
import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.EventStatus;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@RequiredArgsConstructor
public class DeleteDraftEventUseCase {

    private final EventStore eventStore;
    private final EventRealizationStore eventRealizationStore;
    private final TransactionFacade transactionFacade;

    public void deleteDraftEvent(DeleteDraftEventData data) {
        transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.READ_COMMITTED, () -> {
            Optional<Event> eventOpt = eventStore.findById(data.getEventId());
            if (eventOpt.isPresent()) {
                Event event = eventOpt.get();
                validateEventOwner(event, data.getInstructorId());
                validateEventDraft(event);
                eventStore.deleteById(data.getEventId());
                eventRealizationStore.deleteRealizationsByEventId(data.getEventId());
            }
            return null;
        });
    }

    private static void validateEventOwner(Event event, Integer ownerId) {
        checkArgument(Objects.equals(event.getInstructorId(), ownerId), "You can only delete yours draft event");
    }

    private static void validateEventDraft(Event event) {
        checkArgument(event.getStatus() == EventStatus.DRAFT, "You can only delete draft event");
    }
}
