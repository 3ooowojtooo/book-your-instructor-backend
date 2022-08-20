package com.quary.bookyourinstructor.service.event;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.EventType;
import com.quary.bookyourinstructor.model.event.SingleEvent;
import com.quary.bookyourinstructor.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface EventStoreMapper {

    @Mapping(target = "id", source = "singleEvent.id")
    @Mapping(target = "version", source = "singleEvent.version")
    @Mapping(target = "type", source = "singleEvent.type")
    @Mapping(target = "name", source = "singleEvent.name")
    @Mapping(target = "description", source = "singleEvent.description")
    @Mapping(target = "location", source = "singleEvent.location")
    @Mapping(target = "status", source = "singleEvent.status")
    @Mapping(target = "price", source = "singleEvent.price")
    @Mapping(target = "createdAt", source = "singleEvent.createdAt")
    @Mapping(target = "singleEventStart", source = "singleEvent.startDateTime")
    @Mapping(target = "singleEventEnd", source = "singleEvent.endDateTime")
    @Mapping(target = "instructor", source = "instructor")
    @Mapping(target = "student", source = "student")
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
    EventEntity mapToEntity(SingleEvent singleEvent, UserEntity student, UserEntity instructor);

    @Mapping(target = "startDateTime", source = "singleEventStart")
    @Mapping(target = "endDateTime", source = "singleEventEnd")
    @Mapping(target = "instructorId", source = "instructor.id")
    @Mapping(target = "studentId", source = "student.id")
    SingleEvent mapToSingleEvent(EventEntity eventEntity);

    @Mapping(target = "id", source = "cyclicEvent.id")
    @Mapping(target = "version", source = "cyclicEvent.version")
    @Mapping(target = "type", source = "cyclicEvent.type")
    @Mapping(target = "name", source = "cyclicEvent.name")
    @Mapping(target = "description", source = "cyclicEvent.description")
    @Mapping(target = "location", source = "cyclicEvent.location")
    @Mapping(target = "status", source = "cyclicEvent.status")
    @Mapping(target = "price", source = "cyclicEvent.price")
    @Mapping(target = "createdAt", source = "cyclicEvent.createdAt")
    @Mapping(target = "instructor", source = "instructor")
    @Mapping(target = "student", source = "student")
    @Mapping(target = "singleEventStart", ignore = true)
    @Mapping(target = "singleEventEnd", ignore = true)
    @Mapping(target = "realizations", ignore = true)
    @Mapping(target = "cyclicEventStart", source = "cyclicEvent.startTime")
    @Mapping(target = "cyclicEventDuration", source = "cyclicEvent.duration")
    @Mapping(target = "cyclicDayOfWeek", source = "cyclicEvent.dayOfWeek")
    @Mapping(target = "cyclicStartBoundary", source = "cyclicEvent.startBoundary")
    @Mapping(target = "cyclicEndBoundary", source = "cyclicEvent.endBoundary")
    @Mapping(target = "locks", ignore = true)
    @Mapping(target = "cyclicAbsenceEvent", source = "cyclicEvent.absenceEvent")
    @Mapping(target = "cyclicAbsenceEventName", source = "cyclicEvent.absenceEventName")
    @Mapping(target = "cyclicAbsenceEventDescription", source = "cyclicEvent.absenceEventDescription")
    EventEntity mapToEntity(CyclicEvent cyclicEvent, UserEntity student, UserEntity instructor);

    @Mapping(target = "startTime", source = "cyclicEventStart")
    @Mapping(target = "duration", source = "cyclicEventDuration")
    @Mapping(target = "dayOfWeek", source = "cyclicDayOfWeek")
    @Mapping(target = "startBoundary", source = "cyclicStartBoundary")
    @Mapping(target = "endBoundary", source = "cyclicEndBoundary")
    @Mapping(target = "instructorId", source = "instructor.id")
    @Mapping(target = "studentId", source = "student.id")
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
