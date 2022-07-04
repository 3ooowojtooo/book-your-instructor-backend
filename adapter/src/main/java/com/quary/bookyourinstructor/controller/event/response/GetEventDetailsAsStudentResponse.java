package com.quary.bookyourinstructor.controller.event.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quary.bookyourinstructor.model.event.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class GetEventDetailsAsStudentResponse {

    private final Integer id;
    private final Integer version;
    private final String name;
    private final String description;
    private final String location;
    private final String instructorName;
    private final EventType eventType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private final LocalDateTime singleEventStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private final LocalDateTime singleEventEnd;

    private final DayOfWeek cyclicEventDayOfWeek;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private final LocalTime cyclicEventStartTime;
    private final Long cyclicEventDurationSeconds;

    private final boolean locked;

    private final List<EventRealizationTimeBoundaries> realizations;
}
