package bookyourinstructor.usecase.event.cyclic.data;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
public class DeclareCyclicEventData {

    private static final long DURATION_SECONDS_MAXIMUM = Duration.ofDays(7).toSeconds();

    private final String name;
    private final String description;
    private final String location;
    private final BigDecimal price;
    private final LocalTime startTime;
    private final Duration duration;
    private final DayOfWeek dayOfWeek;
    private final LocalDateTime startBoundary;
    private final LocalDateTime endBoundary;
    private final Integer instructorId;
    private final Boolean absenceEvent;
    private final String absenceEventName;
    private final String absenceEventDescription;

    public DeclareCyclicEventData(String name, String description, String location, BigDecimal price, LocalTime startTime, Integer durationSeconds,
                                  DayOfWeek dayOfWeek, LocalDateTime startBoundary, LocalDateTime endBoundary, Integer instructorId,
                                  Boolean absenceEvent, String absenceEventName, String absenceEventDescription) {
        validateConstructorArgs(name, location, price, startTime, durationSeconds, dayOfWeek, startBoundary, endBoundary, instructorId,
                absenceEvent, absenceEventName, absenceEventDescription);
        this.name = name;
        this.description = description;
        this.location = location;
        this.price = price;
        this.startTime = startTime;
        this.duration = Duration.ofSeconds(durationSeconds);
        this.dayOfWeek = dayOfWeek;
        this.startBoundary = startBoundary;
        this.endBoundary = endBoundary;
        this.instructorId = instructorId;
        this.absenceEvent = absenceEvent;
        this.absenceEventName = absenceEventName;
        this.absenceEventDescription = absenceEventDescription;
    }

    private static void validateConstructorArgs(String name, String location, BigDecimal price, LocalTime startTime, Integer durationSeconds,
                                                DayOfWeek dayOfWeek, LocalDateTime startBoundary, LocalDateTime endBoundary,
                                                Integer instructorId, Boolean absenceEvent, String absenceEventName, String absenceEventDescription) {
        checkArgument(isNotBlank(name), "Cyclic event name cannot be blank");
        checkArgument(isNotBlank(location), "Cyclic event location cannot be blank");
        checkNotNull(price, "Cyclic event price cannot be null");
        checkArgument(price.compareTo(BigDecimal.ZERO) > 0, "Cyclic event price must be greater than 0");
        checkNotNull(startTime, "Cyclic event start time cannot be null");
        checkNotNull(durationSeconds, "Cyclic event duration seconds cannot be null");
        checkArgument(durationSeconds > 0, "Cyclic event duration seconds must be positive");
        checkArgument(durationSeconds < DURATION_SECONDS_MAXIMUM, "Cyclic event duration must be shorter than 1 week");
        checkNotNull(dayOfWeek, "Cyclic event day of week cannot be null");
        checkNotNull(startBoundary, "Cyclic event start boundary cannot be null");
        checkNotNull(endBoundary, "Cyclic event end boundary cannot be null");
        checkArgument(startBoundary.compareTo(endBoundary) <= 0, "Cyclic event end boundary must be after or equal start boundary");
        checkNotNull(instructorId, "Cyclic event instructor id cannot be null");
        checkNotNull(absenceEvent, "Cyclic event absence event flag cannot be null");
        if (absenceEvent) {
            checkArgument(isNotBlank(absenceEventName), "Cyclic event absence event name cannot be null");
        } else {
            checkArgument(isNull(absenceEventName), "Cyclic event absence event name must be null");
            checkArgument(isNull(absenceEventDescription), "Cyclic event absence event description must be null");
        }
    }
}
