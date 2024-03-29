package com.quary.bookyourinstructor.model.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@EqualsAndHashCode
@ToString
public abstract class Event {

    private final Integer id;
    private final Integer version;
    private final EventType type;
    private final Integer instructorId;
    private final Integer studentId;
    private final String name;
    private final String description;
    private final String location;
    private final EventStatus status;
    private final BigDecimal price;
    private final Instant createdAt;

    protected Event(Integer id, Integer version, EventType type, Integer instructorId, Integer studentId,
                    String name, String description, String location, EventStatus status, BigDecimal price, Instant createdAt) {
        validateConstructorArgs(type, instructorId, name, location, status, price, createdAt);
        this.id = id;
        this.version = version;
        this.type = type;
        this.instructorId = instructorId;
        this.studentId = studentId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.status = status;
        this.price = price;
        this.createdAt = createdAt;
    }

    private static void validateConstructorArgs(EventType type, Integer instructorId, String name, String location,
                                                EventStatus eventStatus, BigDecimal price, Instant creationTime) {
        checkNotNull(type, "Event type cannot be null");
        checkNotNull(instructorId, "Event instructor id cannot be null");
        checkArgument(isNotBlank(name), "Event name cannot be blank");
        checkArgument(isNotBlank(location), "Event location cannot be blank");
        checkNotNull(eventStatus, "Event status cannot be null");
        checkNotNull(price, "Event price cannot be null");
        checkArgument(price.compareTo(BigDecimal.ZERO) > 0, "Event price must be greater than 0");
        checkNotNull(creationTime, "Event creation date time cannot be null");
    }
}