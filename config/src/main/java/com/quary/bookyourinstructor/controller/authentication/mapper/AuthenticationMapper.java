package com.quary.bookyourinstructor.controller.authentication.mapper;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.controller.authentication.request.AuthenticationRequest;
import com.quary.bookyourinstructor.model.user.EmailAndPassword;
import org.mapstruct.Mapper;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface AuthenticationMapper {

    EmailAndPassword mapToEmailAndPassword(AuthenticationRequest request);
}
