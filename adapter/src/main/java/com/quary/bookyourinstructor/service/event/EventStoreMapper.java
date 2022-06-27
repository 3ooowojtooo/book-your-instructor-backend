package com.quary.bookyourinstructor.service.event;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.EventType;
import com.quary.bookyourinstructor.model.event.SingleEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface EventStoreMapper {

    @Mapping(target = "singleEventStart", source = "startDateTime")
    @Mapping(target = "singleEventEnd", source = "endDateTime")
    @Mapping(target = "instructor.id", source = "instructorId")
    @Mapping(target = "cyclicEventStart", ignore = true)
    @Mapping(target = "cyclicEventDuration", ignore = true)
    @Mapping(target = "cyclicDayOfWeek", ignore = true)
    @Mapping(target = "cyclicStartBoundary", ignore = true)
    @Mapping(target = "cyclicEndBoundary", ignore = true)
    @Mapping(target = "cyclicAbsenceEvent", ignore = true)
    @Mapping(target = "cyclicAbsenceEventName", ignore = true)
    @Mapping(target = "cyclicAbsenceEventDescription", ignore = true)
    @Mapping(target = "realizations", ignore = true)
    @Mapping(target = "locks", ignore = true)
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
    @Mapping(target = "cyclicEventDuration", source = "duration")
    @Mapping(target = "cyclicDayOfWeek", source = "dayOfWeek")
    @Mapping(target = "cyclicStartBoundary", source = "startBoundary")
    @Mapping(target = "cyclicEndBoundary", source = "endBoundary")
    @Mapping(target = "locks", ignore = true)
    @Mapping(target = "cyclicAbsenceEvent", source = "absenceEvent")
    @Mapping(target = "cyclicAbsenceEventName", source = "absenceEventName")
    @Mapping(target = "cyclicAbsenceEventDescription", source = "absenceEventDescription")
    EventEntity mapToEntity(CyclicEvent cyclicEvent);

    @Mapping(target = "startTime", source = "cyclicEventStart")
    @Mapping(target = "duration", source = "cyclicEventDuration")
    @Mapping(target = "dayOfWeek", source = "cyclicDayOfWeek")
    @Mapping(target = "startBoundary", source = "cyclicStartBoundary")
    @Mapping(target = "endBoundary", source = "cyclicEndBoundary")
    @Mapping(target = "instructorId", source = "instructor.id")
    @Mapping(target = "absenceEvent", source = "cyclicAbsenceEvent")
    @Mapping(target = "absenceEventName", source = "cyclicAbsenceEventName")
    @Mapping(target = "absenceEventDescription", source = "cyclicAbsenceEventDescription")
    CyclicEvent mapToCyclicEvent(EventEntity eventEntity);

    default Event mapToEvent(EventEntity eventEntity) {
        if (eventEntity.getType() == EventType.SINGLE) {
            return mapToSingleEvent(eventEntity);
        } else {
            return mapToCyclicEvent(eventEntity);
        }
    }
}
