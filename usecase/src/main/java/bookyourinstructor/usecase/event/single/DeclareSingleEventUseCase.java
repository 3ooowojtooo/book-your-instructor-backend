package bookyourinstructor.usecase.event.single;

import bookyourinstructor.usecase.event.store.EventRealizationStore;
import bookyourinstructor.usecase.event.store.EventStore;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.SingleEvent;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class DeclareSingleEventUseCase {

    private final EventStore eventStore;
    private final EventRealizationStore eventRealizationStore;
    private final TimeUtils timeUtils;
    private final TransactionFacade transactionFacade;

    public DeclareSingleEventResult declareNewSingleEvent(final NewSingleEventData eventData) {
        return transactionFacade.executeInTransaction(() -> {
            SingleEvent event = buildSingleEvent(eventData);
            SingleEvent savedEvent = eventStore.saveSingleEvent(event);
            EventRealization eventRealization = buildEventRealization(savedEvent);
            EventRealization savedEventRealization = eventRealizationStore.saveEventRealization(eventRealization);
            return buildResult(savedEvent, savedEventRealization);
        });
    }

    private static SingleEvent buildSingleEvent(final NewSingleEventData eventData) {
        return SingleEvent.newSingleEvent(eventData.getInstructorId(), eventData.getName(), eventData.getDescription(),
                eventData.getLocation(), eventData.getStartDateTime(), eventData.getEndDateTime());
    }

    private EventRealization buildEventRealization(final SingleEvent event) {
        final Instant start = timeUtils.toInstantFromUTCZone(event.getStartDateTime());
        final Instant end = timeUtils.toInstantFromUTCZone(event.getEndDateTime());
        return EventRealization.newDraft(event.getId(), start, end);
    }

    private static DeclareSingleEventResult buildResult(final SingleEvent event, final EventRealization eventRealization) {
        return new DeclareSingleEventResult(event.getId(), eventRealization);
    }
}
