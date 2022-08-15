package com.quary.bookyourinstructor.controller.event.response;

import bookyourinstructor.usecase.event.common.result.GetEventListResultItem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class GetEventListResponse {

    private final List<GetEventListResponseItem> events;
}
