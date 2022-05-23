package com.quary.bookyourinstructor.controller.event.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class DeclareCyclicEventResponse {

    private final List<EventRealizationTimeBoundaries> eventRealizations;
}
