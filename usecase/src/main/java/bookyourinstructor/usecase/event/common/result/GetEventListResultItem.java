package bookyourinstructor.usecase.event.common.result;

import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.EventStatus;
import com.quary.bookyourinstructor.model.event.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class GetEventListResultItem {

    private final Integer id;
    private final Integer version;
    private final String name;
    private final String description;
    private final String location;
    private final String instructorName;
    private final String studentName;
    private final EventType eventType;
    private final EventStatus eventStatus;
    private final BigDecimal price;
    private final Instant createdAt;

    private final LocalDateTime singleEventStart;
    private final LocalDateTime singleEventEnd;

    private final DayOfWeek cyclicEventDayOfWeek;
    private final LocalTime cyclicEventStartTime;
    private final Duration cyclicEventDuration;
    private final LocalDateTime cyclicEventStartBoundary;
    private final LocalDateTime cyclicEventEndBoundary;

    private final Boolean cyclicAbsenceEvent;
    private final String cyclicAbsenceEventName;
    private final String cyclicAbsenceEventDescription;

    private final List<EventRealization> realizations;
}
