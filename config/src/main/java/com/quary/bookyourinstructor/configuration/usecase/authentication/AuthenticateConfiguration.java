package com.quary.bookyourinstructor.configuration.usecase.authentication;

import bookyourinstructor.usecase.authentication.AuthenticateUseCase;
import bookyourinstructor.usecase.authentication.AuthenticationStore;
import bookyourinstructor.usecase.authentication.jwt.JwtGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AuthenticateConfiguration {

    @Bean
    public AuthenticateUseCase authenticateUseCase(final AuthenticationStore authenticationStore,
                                                   final JwtGenerator jwtGenerator,
                                                   @Value("${jwt.validity-duration}") final Duration tokenValidity) {
        return new AuthenticateUseCase(authenticationStore, jwtGenerator, tokenValidity);
    }
}
