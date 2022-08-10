package bookyourinstructor.usecase.event.single;

import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.event.cyclic.data.DeclareCyclicEventData;
import bookyourinstructor.usecase.event.single.data.DeclareSingleEventData;
import bookyourinstructor.usecase.event.single.result.DeclareSingleEventResult;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.SingleEvent;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkArgument;

@RequiredArgsConstructor
public class DeclareSingleEventUseCase {

    private final EventStore eventStore;
    private final EventRealizationStore eventRealizationStore;
    private final TimeUtils timeUtils;
    private final TransactionFacade transactionFacade;

    public DeclareSingleEventResult declareNewSingleEvent(final DeclareSingleEventData eventData) {
        final Instant now = timeUtils.nowInstant();
        validateEventTimesNotInThePast(eventData, now);
        return transactionFacade.executeInTransaction(() -> {
            SingleEvent event = buildSingleEvent(eventData, now);
            SingleEvent savedEvent = eventStore.saveSingleEvent(event);
            EventRealization eventRealization = buildEventRealization(savedEvent);
            EventRealization savedEventRealization = eventRealizationStore.saveEventRealization(eventRealization);
            return buildResult(savedEvent, savedEventRealization);
        });
    }

    private void validateEventTimesNotInThePast(DeclareSingleEventData data, Instant now) {
        Instant start = timeUtils.toInstantFromUTCZone(data.getStartDateTime());
        Instant end = timeUtils.toInstantFromUTCZone(data.getEndDateTime());
        checkArgument(!start.isBefore(now) && !end.isBefore(now), "Single event start / end date time cannot be in the past");
    }

    private static SingleEvent buildSingleEvent(final DeclareSingleEventData eventData, final Instant now) {
        return SingleEvent.newSingleEventDraft(eventData.getInstructorId(), eventData.getName(), eventData.getDescription(),
                eventData.getLocation(), eventData.getPrice(), now, eventData.getStartDateTime(), eventData.getEndDateTime());
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
