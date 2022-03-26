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

    private final String jwtSecret;
    private final TimeUtils timeUtils;

    public JwtConfiguration(@Value("${jwt.secret}") final String jwtSecret,
                            final TimeUtils timeUtils) {
        this.jwtSecret = jwtSecret;
        this.timeUtils = timeUtils;
    }

    @Bean
    public JwtGenerator jwtGenerator() {
        return new JwtGeneratorImpl(jwtSecret, timeUtils);
    }

    @Bean
    public JwtClaimExtractor jwtClaimExtractor() {
        return new JwtClaimExtractorImpl(jwtSecret, timeUtils);
    }

    @Bean
    public JwtValidator jwtValidator() {
        return new JwtValidatorImpl(jwtClaimExtractor(), timeUtils);
    }
}
