package com.quary.bookyourinstructor.model.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class SingleEvent extends Event {

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public static SingleEvent newSingleEvent(int instructorId, String name, String description, String location, BigDecimal price,
                                             LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return new SingleEvent(null, null, EventType.SINGLE, instructorId, name, description, location,
                EventStatus.DRAFT, price, startDateTime, endDateTime);
    }

    public SingleEvent(Integer id, Integer version, EventType type, Integer instructorId, String name, String description,
                       String location, EventStatus status, BigDecimal price, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(id, version, type, instructorId, name, description, location, status, price);
        validateConstructorArgs(startDateTime, endDateTime);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    private static void validateConstructorArgs(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        checkNotNull(startDateTime, "Single event start date time cannot be null");
        checkNotNull(endDateTime, "Single event end date time cannot be null");
        checkArgument(endDateTime.isAfter(startDateTime), "Single event end date time must be after start date time");
    }
}
