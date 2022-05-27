package com.quary.bookyourinstructor.model.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
@EqualsAndHashCode
@ToString
public class EventLock {

    private final Integer id;
    private final Integer eventId;
    private final Integer eventVersion;
    private final Integer userId;
    private final Instant expirationTime;

    public static EventLock newLock(Integer eventId, Integer eventVersion, Integer userId, Instant expirationTime) {
        return new EventLock(null, eventId, eventVersion, userId, expirationTime);
    }

    public EventLock(Integer id, Integer eventId, Integer eventVersion, Integer userId, Instant expirationTime) {
        validateConstructorArgs(eventId, eventVersion, userId, expirationTime);
        this.id = id;
        this.eventId = eventId;
        this.eventVersion = eventVersion;
        this.userId = userId;
        this.expirationTime = expirationTime;
    }

    private static void validateConstructorArgs(Integer eventId, Integer eventVersion, Integer userId, Instant expirationTime) {
        checkNotNull(eventId, "Event lock event id cannot be null");
        checkNotNull(eventVersion, "Event lock event version cannot be null");
        checkNotNull(userId, "Event lock user id cannot be null");
        checkNotNull(expirationTime, "Event lock expiration time cannot be null");
    }
}
