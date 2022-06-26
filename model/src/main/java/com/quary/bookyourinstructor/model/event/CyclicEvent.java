package com.quary.bookyourinstructor.model.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class CyclicEvent extends Event {

    private static final Duration MAX_CYCLIC_EVENT_DURATION = Duration.ofDays(7);

    private final LocalTime startTime;
    private final Duration duration;
    private final DayOfWeek dayOfWeek;
    private final LocalDate startBoundary;
    private final LocalDate endBoundary;

    public static CyclicEvent newCyclicEvent(int instructorId, String name, String description, String location, BigDecimal price,
                                             LocalTime startTime, Duration duration, DayOfWeek dayOfWeek, LocalDate startBoundary,
                                             LocalDate endBoundary) {
        return new CyclicEvent(null, null, EventType.CYCLIC, instructorId, name, description, location, EventStatus.DRAFT,
                price, startTime, duration, dayOfWeek, startBoundary, endBoundary);
    }

    public CyclicEvent(Integer id, Integer version, EventType type, Integer instructorId, String name, String description,
                       String location, EventStatus status, BigDecimal price, LocalTime startTime, Duration duration, DayOfWeek dayOfWeek,
                       LocalDate startBoundary, LocalDate endBoundary) {
        super(id, version, type, instructorId, name, description, location, status, price);
        validateConstructorArgs(startTime, duration, dayOfWeek, startBoundary, endBoundary);
        this.startTime = startTime;
        this.duration = duration;
        this.dayOfWeek = dayOfWeek;
        this.startBoundary = startBoundary;
        this.endBoundary = endBoundary;
    }

    private static void validateConstructorArgs(LocalTime startTime, Duration duration, DayOfWeek dayOfWeek,
                                                LocalDate startBoundary, LocalDate endBoundary) {
        checkNotNull(startTime, "Cyclic event start time cannot be null");
        checkNotNull(duration, "Cyclic event end time cannot be null");
        checkArgument(!duration.isZero(), "Cyclic event duration cannot be zero");
        checkArgument(duration.compareTo(MAX_CYCLIC_EVENT_DURATION) < 0, "Cyclic event duration must be shorter than 1 week");
        checkNotNull(dayOfWeek, "Cyclic event day of week cannot be null");
        checkNotNull(startBoundary, "Cyclic event start boundary cannot be null");
        checkNotNull(endBoundary, "Cyclic event end boundary cannot be null");
        checkArgument(endBoundary.isAfter(startBoundary), "Cyclic event end boundary must be after start boundary");
    }
}
