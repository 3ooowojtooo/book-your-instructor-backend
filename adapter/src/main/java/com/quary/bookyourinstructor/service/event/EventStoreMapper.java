package com.quary.bookyourinstructor.service.event;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.model.event.SingleEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface EventStoreMapper {

    @Mapping(target = "start", source = "startDateTime")
    @Mapping(target = "end", source = "endDateTime")
    @Mapping(target = "instructor.id", source = "instructorId")
    EventEntity mapToEntity(SingleEvent singleEvent);

    @Mapping(target = "startDateTime", source = "start")
    @Mapping(target = "endDateTime", source = "end")
    @Mapping(target = "instructorId", source = "instructor.id")
    SingleEvent mapToSingleEvent(EventEntity eventEntity);
}
