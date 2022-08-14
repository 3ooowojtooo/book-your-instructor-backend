package com.quary.bookyourinstructor.model.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@EqualsAndHashCode
@ToString
public class EventSchedule {

    private final Integer id;
    private final Integer eventId;
    private final Integer eventRealizationId;
    private final Integer studentId;
    private final Integer instructorId;
    private final EventScheduleStatus status;
    private final EventScheduleOwner owner;
    private final EventScheduleType type;
    private final String eventName;
    private final String eventDescription;
    private final String eventLocation;
    private final BigDecimal eventPrice;
    private final Instant start;
    private final Instant end;

    public EventSchedule(Integer id, Integer eventId, Integer eventRealizationId, Integer studentId, Integer instructorId, EventScheduleStatus status,
                         EventScheduleOwner owner, EventScheduleType type, String eventName, String eventDescription, String eventLocation,
                         BigDecimal eventPrice, Instant start, Instant end) {
        validateConstructorArgs();
        this.id = id;
        this.eventId = eventId;
        this.eventRealizationId = eventRealizationId;
        this.studentId = studentId;
        this.instructorId = instructorId;
        this.status = status;
        this.owner = owner;
        this.type = type;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventPrice = eventPrice;
        this.start = start;
        this.end = end;
    }

    private static void validateConstructorArgs() {

    }

    public static EventSchedule newDynamicEventSchedule(Integer eventId, Integer eventRealizationId, Integer studentId, Integer instructorId,
                                                        EventScheduleStatus status, EventScheduleOwner owner, Instant start,
                                                        Instant end) {
        return new EventSchedule(null, eventId, eventRealizationId, studentId, instructorId, status, owner, EventScheduleType.DYNAMIC,
                null, null, null, null, start, end);
    }
}
