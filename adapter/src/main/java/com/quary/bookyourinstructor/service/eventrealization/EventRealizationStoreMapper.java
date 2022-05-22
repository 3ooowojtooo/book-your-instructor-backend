package com.quary.bookyourinstructor.service.eventrealization;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.model.event.EventRealization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface EventRealizationStoreMapper {

    @Mapping(target = "id", source = "eventRealization.id")
    @Mapping(target = "start", source = "eventRealization.start")
    @Mapping(target = "end", source = "eventRealization.end")
    @Mapping(target = "event", source = "eventEntity")
    @Mapping(target = "student", ignore = true)
    EventRealizationEntity mapToNewEntity(EventRealization eventRealization, EventEntity eventEntity);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "studentId", source = "student.id")
    EventRealization mapToEventRealization(EventRealizationEntity eventRealizationEntity);
}
