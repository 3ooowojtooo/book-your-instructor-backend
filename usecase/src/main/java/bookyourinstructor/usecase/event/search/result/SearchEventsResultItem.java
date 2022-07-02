package bookyourinstructor.usecase.event.search.result;

import com.quary.bookyourinstructor.model.event.EventType;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
public class SearchEventsResultItem {

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

    public SearchEventsResultItem(Integer id, Integer version, String name, String description, String location, String instructorName,
                                  EventType eventType, LocalDateTime singleEventStart, LocalDateTime singleEventEnd,
                                  DayOfWeek cyclicEventDayOfWeek, LocalTime cyclicEventStartTime, Duration cyclicEventDuration) {
        validateConstructorArgs(id, version, name, location, instructorName, eventType, singleEventStart, singleEventEnd,
                cyclicEventDayOfWeek, cyclicEventStartTime, cyclicEventDuration);
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
    }

    private static void validateConstructorArgs(Integer id, Integer version, String name, String location, String instructorName,
                                                EventType eventType, LocalDateTime singleEventStart, LocalDateTime singleEventEnd,
                                                DayOfWeek cyclicEventDayOfWeek, LocalTime cyclicEventStartTime, Duration cyclicEventDuration) {
        checkNotNull(id, "Searched event id cannot be null");
        checkNotNull(version, "Searched event version cannot be null");
        checkArgument(isNotBlank(name), "Searched event name cannot be blank");
        checkArgument(isNotBlank(location), "Searched event location cannot be blank");
        checkArgument(isNotBlank(instructorName), "Searched event instructor name cannot be blank");
        checkNotNull(eventType, "Searched event type cannot be null");
        if (eventType == EventType.SINGLE) {
            checkNotNull(singleEventStart, "Searched single event start cannot be null");
            checkNotNull(singleEventEnd, "Searched single event end cannot be null");
            checkArgument(singleEventStart.isBefore(singleEventEnd), "Searched single event start must be before end");
        } else if (eventType == EventType.CYCLIC) {
            checkNotNull(cyclicEventDayOfWeek, "Searched cyclic event day of week cannot be null");
            checkNotNull(cyclicEventStartTime, "Searched cyclic event start time cannot be null");
            checkNotNull(cyclicEventDuration, "Searched cyclic event duration cannot be null");
            checkArgument(cyclicEventDuration.compareTo(Duration.ZERO) > 0, "Searched cyclic event duration must be greater than 0");
        }
    }
}
