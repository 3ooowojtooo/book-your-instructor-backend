package com.quary.bookyourinstructor.controller.event.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quary.bookyourinstructor.model.event.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class SearchEventsResponseItem {

    private final Integer id;
    private final Integer version;
    private final String name;
    private final String description;
    private final String location;
    private final String instructorName;
    private final EventType eventType;
    private final long futureRealizations;
    private final BigDecimal price;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
    private final Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private final LocalDateTime singleEventStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private final LocalDateTime singleEventEnd;

    private final DayOfWeek cyclicEventDayOfWeek;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private final LocalTime cyclicEventStartTime;
    private final Long cyclicEventDurationSeconds;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private final LocalDateTime cyclicEventStartBoundary;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private final LocalDateTime cyclicEventEndBoundary;
}
