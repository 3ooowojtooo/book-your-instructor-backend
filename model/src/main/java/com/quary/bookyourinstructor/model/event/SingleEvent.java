package com.quary.bookyourinstructor.model.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class SingleEvent extends Event {

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final Integer absenceEventParent;

    public static SingleEvent newSingleEventDraft(int instructorId, String name, String description, String location, BigDecimal price,
                                                  Instant createdAt, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return new SingleEvent(null, null, EventType.SINGLE, instructorId, name, description, location,
                EventStatus.DRAFT, price, createdAt, startDateTime, endDateTime, null);
    }

    public static SingleEvent newAbsenceEvent(int instructorId, String name, String description, String location, BigDecimal price,
                                              Instant createdAt, LocalDateTime startDateTime, LocalDateTime endDateTime,
                                              Integer absenceEventParent) {
        return new SingleEvent(null, null, EventType.SINGLE, instructorId, name, description, location,
                EventStatus.FREE, price, createdAt, startDateTime, endDateTime, absenceEventParent);
    }

    public SingleEvent(Integer id, Integer version, EventType type, Integer instructorId, String name, String description,
                       String location, EventStatus status, BigDecimal price, Instant createdAt, LocalDateTime startDateTime,
                       LocalDateTime endDateTime, Integer absenceEventParent) {
        super(id, version, type, instructorId, name, description, location, status, price, createdAt);
        validateConstructorArgs(startDateTime, endDateTime);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.absenceEventParent = absenceEventParent;
    }

    private static void validateConstructorArgs(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        checkNotNull(startDateTime, "Single event start date time cannot be null");
        checkNotNull(endDateTime, "Single event end date time cannot be null");
        checkArgument(endDateTime.isAfter(startDateTime), "Single event end date time must be after start date time");
    }
}
