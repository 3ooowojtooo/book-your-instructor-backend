package com.quary.bookyourinstructor.controller.event.mapper;

import bookyourinstructor.usecase.event.common.result.GetEventDetailsAsStudentResult;
import bookyourinstructor.usecase.event.cyclic.data.NewCyclicEventData;
import bookyourinstructor.usecase.event.cyclic.data.UpdateCyclicEventRealizationData;
import bookyourinstructor.usecase.event.cyclic.result.DeclareCyclicEventResult;
import bookyourinstructor.usecase.event.search.data.DateRangeFilter;
import bookyourinstructor.usecase.event.search.data.SearchEventsData;
import bookyourinstructor.usecase.event.search.data.TextSearchFilter;
import bookyourinstructor.usecase.event.search.result.SearchEventsResult;
import bookyourinstructor.usecase.event.search.result.SearchEventsResultItem;
import bookyourinstructor.usecase.event.single.data.NewSingleEventData;
import bookyourinstructor.usecase.event.single.result.DeclareSingleEventResult;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.time.impl.TimeUtilsImpl;
import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.controller.event.request.DeclareCyclicEventRequest;
import com.quary.bookyourinstructor.controller.event.request.DeclareSingleEventRequest;
import com.quary.bookyourinstructor.controller.event.request.SearchEventsRequest;
import com.quary.bookyourinstructor.controller.event.request.UpdateCyclicEventRealizationRequest;
import com.quary.bookyourinstructor.controller.event.response.*;
import com.quary.bookyourinstructor.model.event.EventLock;
import com.quary.bookyourinstructor.model.event.EventRealization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.temporal.ChronoField;
import java.util.List;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface EventMapper {

    TimeUtils timeUtils = TimeUtilsImpl.INSTANCE;

    NewSingleEventData mapToNewSingleEventData(DeclareSingleEventRequest request, Integer instructorId);

    DeclareSingleEventResponse mapToDeclareSingleEventResponse(DeclareSingleEventResult result);

    @Mapping(target = "eventRealizationId", source = "id")
    @Mapping(target = "eventStart", source = "start")
    @Mapping(target = "eventEnd", source = "end")
    EventRealizationTimeBoundaries mapToBoundaries(EventRealization eventRealization);

    List<EventRealizationTimeBoundaries> mapToBoundariesList(List<EventRealization> eventRealizations);

    NewCyclicEventData mapToNewCyclicEventData(DeclareCyclicEventRequest request, Integer instructorId);

    DeclareCyclicEventResponse mapToDeclareCyclicEventResponse(DeclareCyclicEventResult result);

    CreateEventBookLockResponse mapToCreateEventBookLockResponse(EventLock eventLock);

    @Mapping(target = "start", source = "request.eventStart")
    @Mapping(target = "end", source = "request.eventEnd")
    UpdateCyclicEventRealizationData mapToUpdateCyclicEventRealizationData(UpdateCyclicEventRealizationRequest request,
                                                                           Integer eventRealizationId, Integer instructorId);

    SearchEventsData mapToSearchEventsData(SearchEventsRequest request);

    @Mapping(target = "searchTokens", ignore = true)
    TextSearchFilter mapTextSearchFilter(SearchEventsRequest.TextSearchFilter filter);

    default DateRangeFilter mapToDateRangeFilter(SearchEventsRequest.DateRangeFilter filter) {
        if (filter == null) return null;

        return new DateRangeFilter(
                timeUtils.toInstantFromUTCZone(filter.getFrom()).with(ChronoField.NANO_OF_SECOND, 0),
                timeUtils.toInstantFromUTCZone(filter.getTo()).with(ChronoField.NANO_OF_SECOND, 999999999)
        );
    }

    SearchEventsResponse mapToSearchEventsResponse(SearchEventsResult result);

    @Mapping(target = "cyclicEventDurationSeconds", expression = "java(resultItem.getCyclicEventDuration() == null ? null : resultItem.getCyclicEventDuration().toSeconds())")
    SearchEventsResponseItem mapToSearchEventsResponseItem(SearchEventsResultItem resultItem);

    @Mapping(target = "cyclicEventDurationSeconds", expression = "java(result.getCyclicEventDuration() == null ? null : result.getCyclicEventDuration().toSeconds())")
    GetEventDetailsAsStudentResponse mapToGetEventDetailsAsStudentResponse(GetEventDetailsAsStudentResult result);
}
