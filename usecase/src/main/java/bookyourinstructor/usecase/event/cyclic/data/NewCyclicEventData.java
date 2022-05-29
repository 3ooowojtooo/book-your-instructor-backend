package bookyourinstructor.usecase.event.cyclic.data;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
public class NewCyclicEventData {

    private final String name;
    private final String description;
    private final String location;
    private final LocalTime startTime;
    private final Duration duration;
    private final DayOfWeek dayOfWeek;
    private final LocalDate startBoundary;
    private final LocalDate endBoundary;
    private final Integer instructorId;

    public NewCyclicEventData(String name, String description, String location, LocalTime startTime, Duration duration,
                              DayOfWeek dayOfWeek, LocalDate startBoundary, LocalDate endBoundary, Integer instructorId) {
        validateConstructorArgs(name, location, startTime, duration, dayOfWeek, startBoundary, endBoundary, instructorId);
        this.name = name;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.duration = duration;
        this.dayOfWeek = dayOfWeek;
        this.startBoundary = startBoundary;
        this.endBoundary = endBoundary;
        this.instructorId = instructorId;
    }

    private static void validateConstructorArgs(String name, String location, LocalTime startTime, Duration duration,
                                                DayOfWeek dayOfWeek, LocalDate startBoundary, LocalDate endBoundary,
                                                Integer instructorId) {
        checkArgument(isNotBlank(name), "Cyclic event name cannot be blank");
        checkArgument(isNotBlank(location), "Cyclic event location cannot be blank");
        checkNotNull(startTime, "Cyclic event start time cannot be null");
        checkNotNull(duration, "Cyclic event duration cannot be null");
        checkNotNull(dayOfWeek, "Cyclic event day of week cannot be null");
        checkNotNull(startBoundary, "Cyclic event start boundary cannot be null");
        checkNotNull(endBoundary, "Cyclic event end boundary cannot be null");
        checkArgument(!duration.isZero(), "Cyclic event duration cannot be zero");
        checkArgument(endBoundary.isAfter(startBoundary), "Cyclic event end boundary must be after start boundary");
        checkNotNull(instructorId, "Cyclic event instructor id cannot be null");
    }
}
