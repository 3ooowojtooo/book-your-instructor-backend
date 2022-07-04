package bookyourinstructor.usecase.event.common.result;

import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.EventType;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
public class GetEventDetailsAsStudentResult {

    private final Integer id;
    private final Integer version;
    private final String name;
    private final String description;
    private final String location;
    private final String instructorName;
    private final EventType eventType;

    private final LocalDateTime singleEventStart;
    private final LocalDateTime singleEventEnd;

    private final DayOfWeek cyclicEventDayOfWeek;
    private final LocalTime cyclicEventStartTime;
    private final Duration cyclicEventDuration;

    private final boolean locked;

    private final List<EventRealization> realizations;

    public GetEventDetailsAsStudentResult(Integer id, Integer version, String name, String description, String location, String instructorName,
                                          EventType eventType, LocalDateTime singleEventStart, LocalDateTime singleEventEnd,
                                          DayOfWeek cyclicEventDayOfWeek, LocalTime cyclicEventStartTime, Duration cyclicEventDuration,
                                          boolean locked, List<EventRealization> realizations) {
        validateConstructorArgs(id, version, name, location, instructorName, eventType, singleEventStart, singleEventEnd,
                cyclicEventDayOfWeek, cyclicEventStartTime, cyclicEventDuration, realizations);
        this.id = id;
        this.version = version;
        this.name = name;
        this.description = description;
        this.location = location;
        this.instructorName = instructorName;
        this.eventType = eventType;
        this.singleEventStart = singleEventStart;
        this.singleEventEnd = singleEventEnd;
        this.cyclicEventDayOfWeek = cyclicEventDayOfWeek;
        this.cyclicEventStartTime = cyclicEventStartTime;
        this.cyclicEventDuration = cyclicEventDuration;
        this.locked = locked;
        this.realizations = realizations;
    }

    private static void validateConstructorArgs(Integer id, Integer version, String name, String location, String instructorName,
                                                EventType eventType, LocalDateTime singleEventStart, LocalDateTime singleEventEnd,
                                                DayOfWeek cyclicEventDayOfWeek, LocalTime cyclicEventStartTime, Duration cyclicEventDuration,
                                                List<EventRealization> realizations) {
        checkNotNull(id, "Event id cannot be null");
        checkNotNull(version, "Event version cannot be null");
        checkArgument(isNotBlank(name), "Event name cannot be blank");
        checkArgument(isNotBlank(location), "Event location cannot be blank");
        checkArgument(isNotBlank(instructorName), "Event instructor name cannot be blank");
        checkNotNull(eventType, "Event type cannot be null");
        if (eventType == EventType.SINGLE) {
            checkNotNull(singleEventStart, "Single event start cannot be null");
            checkNotNull(singleEventEnd, "Single event end cannot be null");
            checkArgument(singleEventStart.isBefore(singleEventEnd), "Single event start must be before end");
        } else if (eventType == EventType.CYCLIC) {
            checkNotNull(cyclicEventDayOfWeek, "Cyclic event day of week cannot be null");
            checkNotNull(cyclicEventStartTime, "Cyclic event start time cannot be null");
            checkNotNull(cyclicEventDuration, "Cyclic event duration cannot be null");
            checkArgument(cyclicEventDuration.compareTo(Duration.ZERO) > 0, "Cyclic event duration must be greater than 0");
        }
        checkNotNull(realizations, "Event realizations list cannot be null");
        checkArgument(!realizations.isEmpty(), "Event realizations list cannot be empty");
    }
}
