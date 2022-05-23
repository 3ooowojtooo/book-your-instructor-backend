package com.quary.bookyourinstructor.controller.event.mapper;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.controller.event.request.DeclareSingleEventRequest;
import com.quary.bookyourinstructor.controller.event.response.DeclareSingleEventResponse;
import com.quary.bookyourinstructor.controller.event.response.EventRealizationTimeBoundaries;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.NewSingleEventData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface EventMapper {

    NewSingleEventData mapToNewSingleEventData(DeclareSingleEventRequest request, Integer instructorId);

    DeclareSingleEventResponse mapToDeclareSingleEventResponse(EventRealization eventRealization);

    @Mapping(target = "eventRealizationId", source = "id")
    @Mapping(target = "eventStart", source = "start")
    @Mapping(target = "eventEnd", source = "end")
    EventRealizationTimeBoundaries mapToBoundaries(EventRealization eventRealization);
}
