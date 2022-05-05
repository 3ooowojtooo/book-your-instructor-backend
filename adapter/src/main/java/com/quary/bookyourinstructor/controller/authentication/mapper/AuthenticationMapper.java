package com.quary.bookyourinstructor.controller.authentication.mapper;

import com.quary.bookyourinstructor.configuration.mapper.DependencyInjectionMapperConfig;
import com.quary.bookyourinstructor.controller.authentication.request.CredentialsAuthenticationRequest;
import com.quary.bookyourinstructor.controller.authentication.request.NewUserRegistrationCredentialsRequest;
import com.quary.bookyourinstructor.model.authentication.EmailAndPassword;
import com.quary.bookyourinstructor.model.authentication.NewUserData;
import org.mapstruct.Mapper;

@Mapper(config = DependencyInjectionMapperConfig.class)
public interface AuthenticationMapper {

    EmailAndPassword mapToEmailAndPassword(CredentialsAuthenticationRequest request);

    NewUserData mapToNewUserData(NewUserRegistrationCredentialsRequest request);
}
