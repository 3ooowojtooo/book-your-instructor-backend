package com.quary.bookyourinstructor.configuration.usecase.event;

import bookyourinstructor.usecase.event.booklock.CreateEventBookLockUseCase;
import bookyourinstructor.usecase.event.store.EventLockStore;
import bookyourinstructor.usecase.event.store.EventRealizationStore;
import bookyourinstructor.usecase.event.store.EventStore;
import bookyourinstructor.usecase.event.cyclic.DeclareCyclicEventUseCase;
import bookyourinstructor.usecase.event.single.DeclareSingleEventUseCase;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class EventConfiguration {

    @Bean
    DeclareSingleEventUseCase declareSingleEventUseCase(EventStore eventStore, EventRealizationStore eventRealizationStore,
                                                        TimeUtils timeUtils, TransactionFacade transactionFacade) {
        return new DeclareSingleEventUseCase(eventStore, eventRealizationStore, timeUtils, transactionFacade);
    }

    @Bean
    DeclareCyclicEventUseCase declareCyclicEventUseCase(EventStore eventStore, EventRealizationStore eventRealizationStore,
                                                        TimeUtils timeUtils, TransactionFacade transactionFacade) {
        return new DeclareCyclicEventUseCase(eventStore, eventRealizationStore, timeUtils, transactionFacade);
    }

    @Bean
    CreateEventBookLockUseCase createEventBookLockUseCase(TransactionFacade transactionFacade, TimeUtils timeUtils, EventLockStore eventLockStore,
                                                          EventStore eventStore, @Value("${event.lock.validity-duration}") final Duration lockExpirationDuration) {
        return new CreateEventBookLockUseCase(transactionFacade, timeUtils, eventLockStore, eventStore, lockExpirationDuration);
    }
}
