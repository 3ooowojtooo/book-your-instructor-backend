package com.quary.bookyourinstructor.controller.event.mapper;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.controller.event.request.DeclareCyclicEventRequest;
import com.quary.bookyourinstructor.controller.event.request.DeclareSingleEventRequest;
import com.quary.bookyourinstructor.controller.event.response.CreateEventBookLockResponse;
import com.quary.bookyourinstructor.controller.event.response.DeclareCyclicEventResponse;
import com.quary.bookyourinstructor.controller.event.response.DeclareSingleEventResponse;
import com.quary.bookyourinstructor.controller.event.response.EventRealizationTimeBoundaries;
import com.quary.bookyourinstructor.model.event.EventLock;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.NewCyclicEventData;
import com.quary.bookyourinstructor.model.event.NewSingleEventData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface EventMapper {

    NewSingleEventData mapToNewSingleEventData(DeclareSingleEventRequest request, Integer instructorId);

    DeclareSingleEventResponse mapToDeclareSingleEventResponse(EventRealization eventRealization);

    @Mapping(target = "eventRealizationId", source = "id")
    @Mapping(target = "eventStart", source = "start")
    @Mapping(target = "eventEnd", source = "end")
    EventRealizationTimeBoundaries mapToBoundaries(EventRealization eventRealization);

    NewCyclicEventData mapToNewCyclicEventData(DeclareCyclicEventRequest request, Integer instructorId);

    default DeclareCyclicEventResponse mapToDeclareCyclicEventResponse(List<EventRealization> eventRealizations) {
        List<EventRealizationTimeBoundaries> boundaries = eventRealizations.stream()
                .map(this::mapToBoundaries)
                .collect(Collectors.toList());
        return new DeclareCyclicEventResponse(boundaries);
    }

    CreateEventBookLockResponse mapToCreateEventBookLockResponse(EventLock eventLock);
}
