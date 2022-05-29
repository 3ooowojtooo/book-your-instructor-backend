package bookyourinstructor.usecase.event.cyclic.helper;

import bookyourinstructor.usecase.event.cyclic.exception.NoRealizationsOfCyclicEventFoundRuntimeException;
import bookyourinstructor.usecase.util.time.TimeUtils;
import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.EventRealization;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CyclicEventRealizationsFinder {

    private final TimeUtils timeUtils;

    public List<EventRealization> findCyclicEventRealizations(final CyclicEvent cyclicEvent) throws NoRealizationsOfCyclicEventFoundRuntimeException {
        LocalDate firstRealizationDate = timeUtils.findDayOfWeekAtOrAfterDate(cyclicEvent.getDayOfWeek(), cyclicEvent.getStartBoundary());
        if (firstRealizationDate.isAfter(cyclicEvent.getEndBoundary())) {
            throw new NoRealizationsOfCyclicEventFoundRuntimeException();
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
