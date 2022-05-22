package com.quary.bookyourinstructor.controller.event.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class DeclareSingleEventResponse {

    private final Integer eventRealizationId;
    private final Instant eventStart;
    private final Instant eventEnd;
}
