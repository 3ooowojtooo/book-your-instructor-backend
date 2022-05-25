package com.quary.bookyourinstructor.model.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.OffsetDateTime;

import static com.google.common.base.Preconditions.*;

@Getter
@EqualsAndHashCode
@ToString
public class EventRealization {

    private final Integer id;
    private final Integer eventId;
    @Setter
    private Integer studentId;
    private final Instant start;
    private final Instant end;
    private EventRealizationStatus status;

    public static EventRealization newDraft(Integer eventId, Instant start, Instant end) {
        return new EventRealization(null, eventId, null, start, end, EventRealizationStatus.DRAFT);
    }

    public EventRealization(Integer id, Integer eventId, Integer studentId, Instant start, Instant end, EventRealizationStatus status) {
        validateConstructorArgs(eventId, start, end, status);
        this.id = id;
        this.eventId = eventId;
        this.studentId = studentId;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    private static void validateConstructorArgs(Integer eventId, Instant start, Instant end, EventRealizationStatus status) {
        checkNotNull(eventId, "Event realization id cannot be null");
        checkNotNull(start, "Event realization start timestamp cannot be null");
        checkNotNull(end, "Event realization end timestamp cannot be null");
        checkArgument(end.isAfter(start), "Event realization end timestamp must be after start timestamp");
        checkNotNull(status, "Event realization status cannot be null");
    }
}
