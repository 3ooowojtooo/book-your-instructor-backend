package com.quary.bookyourinstructor.service.eventschedule;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.entity.EventScheduleEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.EventSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface EventScheduleStoreMapper {

    @Mapping(target = "id", source = "eventSchedule.id")
    @Mapping(target = "status", source = "eventSchedule.status")
    @Mapping(target = "owner", source = "eventSchedule.owner")
    @Mapping(target = "type", source = "eventSchedule.type")
    @Mapping(target = "eventName", source = "eventSchedule.eventName")
    @Mapping(target = "eventDescription", source = "eventSchedule.eventDescription")
    @Mapping(target = "eventLocation", source = "eventSchedule.eventLocation")
    @Mapping(target = "eventPrice", source = "eventSchedule.eventPrice")
    @Mapping(target = "start", source = "eventSchedule.start")
    @Mapping(target = "end", source = "eventSchedule.end")
    @Mapping(target = "event", source = "event")
    @Mapping(target = "eventRealization", source = "eventRealization")
    @Mapping(target = "student", source = "student")
    @Mapping(target = "instructor", source = "instructor")
    EventScheduleEntity mapToEntity(EventSchedule eventSchedule, EventEntity event, EventRealizationEntity eventRealization,
                                    UserEntity student, UserEntity instructor);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "eventRealizationId", source = "eventRealization.id")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "instructorId", source = "instructor.id")
    EventSchedule mapToEventSchedule(EventScheduleEntity entity);
}
