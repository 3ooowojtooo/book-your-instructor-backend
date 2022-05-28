package com.quary.bookyourinstructor.model.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
    private final String name;
    private final String description;
    private final String location;
    private EventStatus status;

    protected Event(Integer id, Integer version, EventType type, Integer instructorId, String name, String description,
                    String location, EventStatus status) {
        validateConstructorArgs(type, instructorId, name, location, status);
        this.id = id;
        this.version = version;
        this.type = type;
        this.instructorId = instructorId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.status = status;
    }

    private static void validateConstructorArgs(EventType type, Integer instructorId, String name, String location, EventStatus eventStatus) {
        checkNotNull(type, "Event type cannot be null");
        checkNotNull(instructorId, "Event instructor id cannot be null");
        checkArgument(isNotBlank(name), "Event name cannot be blank");
        checkArgument(isNotBlank(location), "Event location cannot be blank");
        checkNotNull(eventStatus, "Event status cannot be null");
    }

    public void markAsBooked() {
        if (status != EventStatus.FREE) {
            throw new IllegalStateException("Only free event can be marked as booked");
        }
        this.status = EventStatus.BOOKED;
    }
}