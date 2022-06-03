package bookyourinstructor.usecase.event.cyclic.data;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class UpdateCyclicEventRealizationData {

    private static final Duration DURATION_MAXIMUM = Duration.ofDays(7);

    private final Integer eventRealizationId;
    private final Integer instructorId;
    private final Instant start;
    private final Instant end;

    public UpdateCyclicEventRealizationData(Integer eventRealizationId, Integer instructorId, Instant start, Instant end) {
        validateConstructorArgs(eventRealizationId, instructorId, start, end);
        this.eventRealizationId = eventRealizationId;
        this.instructorId = instructorId;
        this.start = start;
        this.end = end;
    }

    private static void validateConstructorArgs(Integer eventRealizationId, Integer instructorId, Instant start, Instant end) {
        checkNotNull(eventRealizationId, "Event realization id cannot be null");
        checkNotNull(instructorId, "Instructor id cannot be null");
        checkNotNull(start, "Event realization start cannot be null");
        checkNotNull(end, "Event realization end cannot be null");
        checkArgument(start.isBefore(end), "Event realization start must be before end");
        final Duration eventRealizationDuration = Duration.between(start, end);
        checkArgument(eventRealizationDuration.compareTo(DURATION_MAXIMUM) < 0,
                "Event realization duration must be lower than 1 week");
    }
}
