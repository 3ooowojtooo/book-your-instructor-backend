package com.quary.bookyourinstructor.configuration.usecase.event;

import bookyourinstructor.usecase.event.EventRealizationStore;
import bookyourinstructor.usecase.event.EventStore;
import bookyourinstructor.usecase.event.single.DeclareSingleEventUseCase;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfiguration {

    @Bean
    DeclareSingleEventUseCase declareSingleEventUseCase(EventStore eventStore, EventRealizationStore eventRealizationStore,
                                                        TimeUtils timeUtils, TransactionFacade transactionFacade) {
        return new DeclareSingleEventUseCase(eventStore, eventRealizationStore, timeUtils, transactionFacade);
    }
}
