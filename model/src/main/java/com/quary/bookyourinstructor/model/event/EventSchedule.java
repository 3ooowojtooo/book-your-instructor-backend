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

    public EventSchedule(Integer id, Integer eventId, Integer eventRealizationId, Integer studentId, Integer instructorId, EventScheduleStatus status,
                         EventScheduleOwner owner) {
        validateConstructorArgs();
        this.id = id;
        this.eventId = eventId;
        this.eventRealizationId = eventRealizationId;
        this.studentId = studentId;
        this.instructorId = instructorId;
        this.status = status;
        this.owner = owner;
    }

    private static void validateConstructorArgs() {

    }

    public static EventSchedule newDynamicEventSchedule(Integer eventId, Integer eventRealizationId, Integer studentId, Integer instructorId,
                                                        EventScheduleStatus status, EventScheduleOwner owner, Instant start,
                                                        Instant end) {
        return new EventSchedule(null, eventId, eventRealizationId, studentId, instructorId, status, owner);
    }
}
