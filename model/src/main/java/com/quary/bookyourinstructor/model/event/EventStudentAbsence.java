package com.quary.bookyourinstructor.model.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@EqualsAndHashCode
@ToString
public class EventStudentAbsence {

    private final Integer id;
    private final Integer eventRealizationId;
    private final Integer studentId;
    private final String eventName;
    private final String description;
    private final String eventLocation;
    private final Instant eventStart;
    private final Instant eventEnd;

    public EventStudentAbsence(Integer id, Integer eventRealizationId, Integer studentId, String eventName, String description,
                               String eventLocation, Instant eventStart, Instant eventEnd) {
        validateConstructorArgs(id, eventRealizationId, studentId, eventName, eventLocation, eventStart, eventEnd);
        this.id = id;
        this.eventRealizationId = eventRealizationId;
        this.studentId = studentId;
        this.eventName = eventName;
        this.description = description;
        this.eventLocation = eventLocation;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }

    private static void validateConstructorArgs(Integer id, Integer eventRealizationId, Integer studentId, String eventName,
                                                String eventLocation, Instant eventStart, Instant eventEnd) {
        checkNotNull(id, "Id cannot be null");
        checkNotNull(eventRealizationId, "Event realization id cannot be null");
        checkNotNull(studentId, "Student id cannot be null");
        checkArgument(isNotBlank(eventName), "Event name cannot be blank");
        checkArgument(isNotBlank(eventLocation), "Event location cannot be blank");
        checkNotNull(eventStart, "Event start cannot be null");
        checkNotNull(eventEnd, "Event end cannot be null");
        checkArgument(eventStart.isBefore(eventEnd), "Event start must be before event end");
    }
}
