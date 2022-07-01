package com.quary.bookyourinstructor.configuration.retry;

import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.PessimisticLockingFailureException;

import java.time.Duration;

@Configuration
public class RetryConfiguration {

    @Bean
    RetryRegistry retryRegistry(@Value("${event.lock.retry-wait-millis}") Integer lockRetryWaitMillis,
                                @Value("${event.lock.retry-max-attempts}") Integer lockRetryMaxAttempts) {
        RetryConfig defaultConfig = RetryConfig.custom()
                .maxAttempts(lockRetryMaxAttempts)
                .waitDuration(Duration.ofMillis(lockRetryWaitMillis))
                .retryExceptions(PessimisticLockingFailureException.class)
                .build();

        return RetryRegistry.of(defaultConfig);
    }
}
