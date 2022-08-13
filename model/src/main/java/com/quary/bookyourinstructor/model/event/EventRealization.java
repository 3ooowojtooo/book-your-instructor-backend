package com.quary.bookyourinstructor.model.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Getter
@EqualsAndHashCode
@ToString
public class EventRealization {

    private final Integer id;
    private final Integer eventId;
    private final Integer studentId;
    private Instant start;
    private Instant end;
    private final EventRealizationStatus status;

    public static EventRealization newDraft(Integer eventId, Instant start, Instant end) {
        return new EventRealization(null, eventId, null, start, end, EventRealizationStatus.DRAFT);
    }

    public static EventRealization newAccepted(Integer eventId, Instant start, Instant end) {
        return new EventRealization(null, eventId, null, start, end, EventRealizationStatus.ACCEPTED);
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

    public void setNewTimeBoundaries(Instant start, Instant end) {
        checkNotNull(start, "Event realization start cannot be null");
        checkNotNull(end, "Event realization end cannot be null");
        checkArgument(start.isBefore(end), "Event realization start must be before end");
        this.start = start;
        this.end = end;
    }

    public boolean collidesWithTimeBoundaries(Instant start, Instant end) {
        boolean hasNoCollision = (start.isBefore(this.start) && (end.isBefore(this.start) || end.equals(this.start))) ||
                ((start.isAfter(this.end) || start.equals(this.end)) && end.isAfter(this.end));
        return !hasNoCollision;
    }
}
