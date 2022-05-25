package com.quary.bookyourinstructor.configuration.usecase.util;

import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.time.impl.TimeUtilsImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeUtilsConfiguration {

    @Bean
    public TimeUtils timeUtils() {
        return TimeUtilsImpl.INSTANCE;
    }
}
