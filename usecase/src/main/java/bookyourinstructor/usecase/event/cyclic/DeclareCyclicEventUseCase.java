package bookyourinstructor.usecase.event.cyclic;

import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.event.cyclic.data.DeclareCyclicEventData;
import bookyourinstructor.usecase.event.cyclic.exception.NoRealizationsOfCyclicEventFoundRuntimeException;
import bookyourinstructor.usecase.event.cyclic.helper.CyclicEventRealizationsFinder;
import bookyourinstructor.usecase.event.cyclic.result.DeclareCyclicEventResult;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.exception.InvalidCyclicEventBoundariesException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@RequiredArgsConstructor
public class DeclareCyclicEventUseCase {

    private final EventStore eventStore;
    private final EventRealizationStore eventRealizationStore;
    private final TransactionFacade transactionFacade;
    private final CyclicEventRealizationsFinder realizationsFinder;
    private final TimeUtils timeUtils;

    public DeclareCyclicEventResult declareNewCyclicEvent(final DeclareCyclicEventData eventData) throws InvalidCyclicEventBoundariesException {
        final Instant now = timeUtils.nowInstant();
        validateBoundariesNotInThePast(eventData, now);
        try {
            return transactionFacade.executeInTransaction(() -> {
                CyclicEvent event = buildCyclicEvent(eventData, now);
                CyclicEvent savedEvent = eventStore.saveCyclicEvent(event);
                List<EventRealization> eventRealizations = realizationsFinder.findCyclicEventRealizations(savedEvent);
                List<EventRealization> savedEventRealizations = eventRealizationStore.saveEventRealizations(eventRealizations);
                updateCyclicEventBoundaries(savedEvent.getId(), savedEventRealizations);
                return buildResult(savedEvent, savedEventRealizations);
            });
        } catch (NoRealizationsOfCyclicEventFoundRuntimeException ex) {
            throw new InvalidCyclicEventBoundariesException();
        }
    }

    private void validateBoundariesNotInThePast(DeclareCyclicEventData data, Instant now) {
        Instant startBoundary = timeUtils.toInstantFromUTCZone(data.getStartBoundary());
        Instant endBoundary = timeUtils.toInstantFromUTCZone(data.getEndBoundary());
        checkArgument(!startBoundary.isBefore(now) && !endBoundary.isBefore(now), "Cyclic event boundaries must not be in the past");
    }

    private void updateCyclicEventBoundaries(Integer eventId, List<EventRealization> realizations) {
        EventRealization firstRealization = realizations.get(0);
        EventRealization lastRealization = realizations.get(realizations.size() - 1);
        LocalDate startBoundary = timeUtils.toLocalDateUTCZone(firstRealization.getStart());
        LocalDate endBoundary = timeUtils.toLocalDateUTCZone(lastRealization.getStart());
        eventStore.updateCyclicEventBoundaries(eventId, startBoundary, endBoundary);
    }

    private static CyclicEvent buildCyclicEvent(final DeclareCyclicEventData eventData, Instant now) {
        return CyclicEvent.newCyclicEventDraft(eventData.getInstructorId(), eventData.getName(), eventData.getDescription(),
                eventData.getLocation(), eventData.getPrice(), now, eventData.getStartTime(), eventData.getDuration(), eventData.getDayOfWeek(),
                eventData.getStartBoundary(), eventData.getEndBoundary(), eventData.getAbsenceEvent(), eventData.getAbsenceEventName(),
                eventData.getAbsenceEventDescription());
    }

    private static DeclareCyclicEventResult buildResult(CyclicEvent event, List<EventRealization> eventRealizations) {
        return new DeclareCyclicEventResult(event.getId(), event.getCreatedAt(), eventRealizations);
    }
}
