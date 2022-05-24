package bookyourinstructor.usecase.event.single;

import bookyourinstructor.usecase.event.EventRealizationStore;
import bookyourinstructor.usecase.event.EventStore;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.NewSingleEventData;
import com.quary.bookyourinstructor.model.event.SingleEvent;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class DeclareSingleEventUseCase {

    private final EventStore eventStore;
    private final EventRealizationStore eventRealizationStore;
    private final TimeUtils timeUtils;
    private final TransactionFacade transactionFacade;

    public EventRealization declareNewSingleEvent(final NewSingleEventData eventData) {
        return transactionFacade.executeInTransaction(() -> {
            SingleEvent event = buildSingleEvent(eventData);
            SingleEvent savedEvent = eventStore.saveSingleEvent(event);
            EventRealization eventRealization = buildEventRealization(savedEvent);
            return eventRealizationStore.saveEventRealization(eventRealization);
        });
    }

    private static SingleEvent buildSingleEvent(final NewSingleEventData eventData) {
        return SingleEvent.newSingleEvent(eventData.getInstructorId(), eventData.getName(), eventData.getDescription(),
                eventData.getLocation(), eventData.getStartDateTime(), eventData.getEndDateTime());
    }

    private EventRealization buildEventRealization(final SingleEvent event) {
        final OffsetDateTime start = timeUtils.toOffsetDataTime(event.getStartDateTime());
        final OffsetDateTime end = timeUtils.toOffsetDataTime(event.getEndDateTime());
        return EventRealization.newDraft(event.getId(), start, end);
    }
}
