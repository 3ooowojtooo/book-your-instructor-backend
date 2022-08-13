package com.quary.bookyourinstructor.controller.event.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quary.bookyourinstructor.model.event.EventScheduleStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class GetEventScheduleResponseItem {

    private final Integer eventId;
    private final Integer eventVersion;
    private final String eventName;
    private final String eventDescription;
    private final String eventLocation;
    private final BigDecimal eventPrice;
    private final String instructorName;
    private final String studentName;
    private final EventScheduleStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
    private final Instant eventStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
    private final Instant eventEnd;
}
