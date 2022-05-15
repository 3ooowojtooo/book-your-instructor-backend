package com.quary.bookyourinstructor.configuration.usecase.authentication;

import bookyourinstructor.usecase.authentication.credentials.CredentialsAuthenticateUseCase;
import bookyourinstructor.usecase.authentication.credentials.CredentialsAuthenticationStore;
import bookyourinstructor.usecase.authentication.credentials.NewUserRegistrationCredentialsUseCase;
import bookyourinstructor.usecase.authentication.credentials.PasswordEncoderHelper;
import bookyourinstructor.usecase.authentication.facebook.FacebookAuthenticateUseCase;
import bookyourinstructor.usecase.authentication.facebook.FacebookProfileDataFetcher;
import bookyourinstructor.usecase.authentication.jwt.JwtGenerator;
import bookyourinstructor.usecase.authentication.user.UserStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AuthenticateConfiguration {

    @Bean
    public CredentialsAuthenticateUseCase credentialsAuthenticateUseCase(final CredentialsAuthenticationStore credentialsAuthenticationStore,
                                                                         final JwtGenerator jwtGenerator,
                                                                         @Value("${jwt.validity-duration}") final Duration tokenValidity) {
        return new CredentialsAuthenticateUseCase(credentialsAuthenticationStore, jwtGenerator, tokenValidity);
    }

    @Bean
    public FacebookAuthenticateUseCase facebookAuthenticateUseCase(final FacebookProfileDataFetcher emailFetcher,
                                                                   final UserStore userStore,
                                                                   final JwtGenerator jwtGenerator,
                                                                   @Value("${jwt.validity-duration}") final Duration tokenValidity) {
        return new FacebookAuthenticateUseCase(emailFetcher, userStore, jwtGenerator, tokenValidity);
    }

    @Bean
    public NewUserRegistrationCredentialsUseCase newUserRegistrationCredentialsUseCase(final UserStore userStore,
                                                                                       final PasswordEncoderHelper passwordEncoderHelper) {
        return new NewUserRegistrationCredentialsUseCase(userStore, passwordEncoderHelper);
    }
}
