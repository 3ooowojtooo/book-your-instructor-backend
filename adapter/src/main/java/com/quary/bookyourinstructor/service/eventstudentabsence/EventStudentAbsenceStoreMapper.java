package com.quary.bookyourinstructor.service.eventstudentabsence;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.entity.EventStudentAbsenceEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.EventStudentAbsence;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface EventStudentAbsenceStoreMapper {

    @Mapping(target = "id", source = "absence.id")
    @Mapping(target = "eventName", source = "absence.eventName")
    @Mapping(target = "eventDescription", source = "absence.eventDescription")
    @Mapping(target = "eventLocation", source = "absence.eventLocation")
    @Mapping(target = "eventStart", source = "absence.eventStart")
    @Mapping(target = "eventEnd", source = "absence.eventEnd")
    @Mapping(target = "eventRealization", source = "realization")
    @Mapping(target = "student", source = "student")
    EventStudentAbsenceEntity mapToEntity(EventStudentAbsence absence, EventRealizationEntity realization,
                                          UserEntity student);
}
