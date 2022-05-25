package bookyourinstructor.usecase.event.cyclic;

import bookyourinstructor.usecase.event.EventRealizationStore;
import bookyourinstructor.usecase.event.EventStore;
import bookyourinstructor.usecase.event.cyclic.exception.NoDayOfWeekFoundWithinBoundaries;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.NewCyclicEventData;
import com.quary.bookyourinstructor.model.event.exception.InvalidCyclicEventBoundariesException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class DeclareCyclicEventUseCase {

    private final EventStore eventStore;
    private final EventRealizationStore eventRealizationStore;
    private final TimeUtils timeUtils;
    private final TransactionFacade transactionFacade;

    public List<EventRealization> declareNewCyclicEvent(final NewCyclicEventData eventData) throws InvalidCyclicEventBoundariesException {
        try {
            return transactionFacade.executeInTransaction(() -> {
                CyclicEvent event = buildCyclicEvent(eventData);
                CyclicEvent savedEvent = eventStore.saveCyclicEvent(event);
                List<EventRealization> eventRealizations = findEventRealizations(savedEvent);
                return eventRealizationStore.saveEventRealizations(eventRealizations);
            });
        } catch (NoDayOfWeekFoundWithinBoundaries ex) {
            throw new InvalidCyclicEventBoundariesException();
        }
    }

    private static CyclicEvent buildCyclicEvent(final NewCyclicEventData eventData) {
        return CyclicEvent.newCyclicEvent(eventData.getInstructorId(), eventData.getName(), eventData.getDescription(),
                eventData.getLocation(), eventData.getStartTime(), eventData.getEndTime(), eventData.getDayOfWeek(),
                eventData.getStartBoundary(), eventData.getEndBoundary());
    }

    private List<EventRealization> findEventRealizations(final CyclicEvent cyclicEvent) throws NoDayOfWeekFoundWithinBoundaries {
        LocalDate firstRealizationDate = timeUtils.findDayOfWeekAtOrAfterDate(cyclicEvent.getDayOfWeek(), cyclicEvent.getStartBoundary());
        if (firstRealizationDate.isAfter(cyclicEvent.getEndBoundary())) {
            throw new NoDayOfWeekFoundWithinBoundaries(cyclicEvent.getDayOfWeek(), cyclicEvent.getStartBoundary(), cyclicEvent.getEndBoundary());
        }

        List<EventRealization> eventRealizations = new ArrayList<>();

        LocalDate currentRealizationDate = firstRealizationDate;
        while (currentRealizationDate.isBefore(cyclicEvent.getEndBoundary()) || currentRealizationDate.isEqual(cyclicEvent.getEndBoundary())) {
            EventRealization eventRealization = buildEventRealization(currentRealizationDate, cyclicEvent);
            eventRealizations.add(eventRealization);
            currentRealizationDate = currentRealizationDate.plusWeeks(1);
        }

        return eventRealizations;
    }

    private EventRealization buildEventRealization(LocalDate realizationDate, CyclicEvent event) {
        LocalDateTime startDateTime = LocalDateTime.of(realizationDate, event.getStartTime());
        LocalDateTime endDateTime = LocalDateTime.of(realizationDate, event.getEndTime());
        return EventRealization.newDraft(event.getId(), timeUtils.toInstantFromUTCZone(startDateTime), timeUtils.toInstantFromUTCZone(endDateTime));
    }
}
