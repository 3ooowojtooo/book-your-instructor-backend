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
        LocalDate firstRealizationDateStart = timeUtils.findDayOfWeekAtOrAfterDateTime(cyclicEvent.getDayOfWeek(), cyclicEvent.getStartBoundary());
        LocalDateTime firstRealizationDateTimeStart = LocalDateTime.of(firstRealizationDateStart, cyclicEvent.getStartTime());
        LocalDateTime firstRealizationDateTimeEnd = firstRealizationDateTimeStart.plus(cyclicEvent.getDuration());
        if (firstRealizationDateTimeStart.isAfter(cyclicEvent.getEndBoundary())) {
            throw new NoRealizationsOfCyclicEventFoundRuntimeException();
        }

        List<EventRealization> eventRealizations = new ArrayList<>();

        LocalDateTime currentRealizationDateTimeStart = firstRealizationDateTimeStart;
        LocalDateTime currentRealizationDateTimeEnd = firstRealizationDateTimeEnd;
        while (timeUtils.isBeforeOrEqual(currentRealizationDateTimeEnd, cyclicEvent.getEndBoundary())) {
            EventRealization eventRealization = buildEventRealization(currentRealizationDateTimeStart, currentRealizationDateTimeEnd, cyclicEvent);
            eventRealizations.add(eventRealization);
            currentRealizationDateTimeStart = currentRealizationDateTimeStart.plusWeeks(1);
            currentRealizationDateTimeEnd = currentRealizationDateTimeEnd.plusWeeks(1);
        }

        return eventRealizations;
    }

    private EventRealization buildEventRealization(LocalDateTime realizationStart, LocalDateTime realizationEnd, CyclicEvent event) {
        return EventRealization.newDraft(event.getId(), timeUtils.toInstantFromUTCZone(realizationStart), timeUtils.toInstantFromUTCZone(realizationEnd));
    }

}
