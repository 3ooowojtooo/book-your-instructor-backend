package com.quary.bookyourinstructor.controller.event.mapper;

import bookyourinstructor.usecase.event.cyclic.data.UpdateCyclicEventRealizationData;
import bookyourinstructor.usecase.event.cyclic.result.DeclareCyclicEventResult;
import bookyourinstructor.usecase.event.single.result.DeclareSingleEventResult;
import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.controller.event.request.DeclareCyclicEventRequest;
import com.quary.bookyourinstructor.controller.event.request.DeclareSingleEventRequest;
import com.quary.bookyourinstructor.controller.event.request.UpdateCyclicEventRealizationRequest;
import com.quary.bookyourinstructor.controller.event.response.CreateEventBookLockResponse;
import com.quary.bookyourinstructor.controller.event.response.DeclareCyclicEventResponse;
import com.quary.bookyourinstructor.controller.event.response.DeclareSingleEventResponse;
import com.quary.bookyourinstructor.controller.event.response.EventRealizationTimeBoundaries;
import com.quary.bookyourinstructor.model.event.EventLock;
import com.quary.bookyourinstructor.model.event.EventRealization;
import bookyourinstructor.usecase.event.cyclic.data.NewCyclicEventData;
import bookyourinstructor.usecase.event.single.data.NewSingleEventData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface EventMapper {

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
}
