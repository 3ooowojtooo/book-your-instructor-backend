package com.quary.bookyourinstructor.controller.booklock.mapper;

import bookyourinstructor.usecase.event.booklock.data.CreateEventBookLockData;
import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.controller.booklock.request.CreateEventBookLockRequest;
import com.quary.bookyourinstructor.controller.booklock.response.CreateEventBookLockResponse;
import com.quary.bookyourinstructor.model.event.EventLock;
import org.mapstruct.Mapper;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface BookLockMapper {

    CreateEventBookLockResponse mapToCreateEventBookLockResponse(EventLock eventLock);

    CreateEventBookLockData mapToCreateEventBookLockData(CreateEventBookLockRequest request, Integer studentId);
}
