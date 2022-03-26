package com.quary.bookyourinstructor.configuration.usecase.authentication;

import bookyourinstructor.usecase.authentication.jwt.JwtClaimExtractor;
import bookyourinstructor.usecase.authentication.jwt.JwtGenerator;
import bookyourinstructor.usecase.authentication.jwt.JwtValidator;
import bookyourinstructor.usecase.authentication.jwt.impl.JwtClaimExtractorImpl;
import bookyourinstructor.usecase.authentication.jwt.impl.JwtGeneratorImpl;
import bookyourinstructor.usecase.authentication.jwt.impl.JwtValidatorImpl;
import bookyourinstructor.usecase.util.time.TimeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

    @Bean
    public JwtGenerator jwtGenerator(@Value("${jwt.secret}") final String jwtSecret,
                                     final TimeUtils timeUtils) {
        return new JwtGeneratorImpl(jwtSecret, timeUtils);
    }

    @Bean
    public JwtClaimExtractor jwtClaimExtractor(@Value("${jwt.secret}") final String jwtSecret,
                                               final TimeUtils timeUtils) {
        return new JwtClaimExtractorImpl(jwtSecret, timeUtils);
    }

    @Bean
    public JwtValidator jwtValidator(final JwtClaimExtractor jwtClaimExtractor,
                                     final TimeUtils timeUtils) {
        return new JwtValidatorImpl(jwtClaimExtractor, timeUtils);
    }
}
