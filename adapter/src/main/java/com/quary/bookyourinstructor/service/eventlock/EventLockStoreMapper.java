package com.quary.bookyourinstructor.service.eventlock;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventLockEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.EventLock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface EventLockStoreMapper {

    @Mapping(target = "id", source = "eventLock.id")
    EventLockEntity mapToEntity(EventLock eventLock, UserEntity user, EventEntity event);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "userId", source = "user.id")
    EventLock mapToEventLock(EventLockEntity entity);
}
