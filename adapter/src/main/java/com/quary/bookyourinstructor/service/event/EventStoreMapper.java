package com.quary.bookyourinstructor.service.event;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.SingleEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface EventStoreMapper {

    @Mapping(target = "singleEventStart", source = "startDateTime")
    @Mapping(target = "singleEventEnd", source = "endDateTime")
    @Mapping(target = "instructor.id", source = "instructorId")
    @Mapping(target = "cyclicEventStart", ignore = true)
    @Mapping(target = "cyclicEventEnd", ignore = true)
    @Mapping(target = "cyclicDayOfWeek", ignore = true)
    @Mapping(target = "cyclicStartBoundary", ignore = true)
    @Mapping(target = "cyclicEndBoundary", ignore = true)
    @Mapping(target = "realizations", ignore = true)
    EventEntity mapToEntity(SingleEvent singleEvent);

    @Mapping(target = "startDateTime", source = "singleEventStart")
    @Mapping(target = "endDateTime", source = "singleEventEnd")
    @Mapping(target = "instructorId", source = "instructor.id")
    SingleEvent mapToSingleEvent(EventEntity eventEntity);

    @Mapping(target = "instructor.id", source = "instructorId")
    @Mapping(target = "singleEventStart", ignore = true)
    @Mapping(target = "singleEventEnd", ignore = true)
    @Mapping(target = "realizations", ignore = true)
    @Mapping(target = "cyclicEventStart", source = "startTime")
    @Mapping(target = "cyclicEventEnd", source = "endTime")
    @Mapping(target = "cyclicDayOfWeek", source = "dayOfWeek")
    @Mapping(target = "cyclicStartBoundary", source = "startBoundary")
    @Mapping(target = "cyclicEndBoundary", source = "endBoundary")
    EventEntity mapToEntity(CyclicEvent cyclicEvent);

    @Mapping(target = "startTime", source = "cyclicEventStart")
    @Mapping(target = "endTime", source = "cyclicEventEnd")
    @Mapping(target = "dayOfWeek", source = "cyclicDayOfWeek")
    @Mapping(target = "startBoundary", source = "cyclicStartBoundary")
    @Mapping(target = "endBoundary", source = "cyclicEndBoundary")
    @Mapping(target = "instructorId", source = "instructor.id")
    CyclicEvent mapToCyclicEvent(EventEntity eventEntity);
}
