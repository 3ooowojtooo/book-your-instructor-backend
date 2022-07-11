package com.quary.bookyourinstructor.controller.event.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class DeclareSingleEventResponse {

    private final Integer eventId;
    private final EventRealizationTimeBoundaries eventRealization;
}
