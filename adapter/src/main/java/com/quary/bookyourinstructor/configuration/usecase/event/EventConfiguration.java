package com.quary.bookyourinstructor.configuration.usecase.event;

import bookyourinstructor.usecase.event.booklock.ConfirmEventBookLockUseCase;
import bookyourinstructor.usecase.event.booklock.CreateEventBookLockUseCase;
import bookyourinstructor.usecase.event.booklock.DeleteEventBookLockUseCase;
import bookyourinstructor.usecase.event.common.AcceptEventUseCase;
import bookyourinstructor.usecase.event.cyclic.DeclareCyclicEventUseCase;
import bookyourinstructor.usecase.event.cyclic.UpdateCyclicEventRealizationUseCase;
import bookyourinstructor.usecase.event.cyclic.helper.CyclicEventRealizationsFinder;
import bookyourinstructor.usecase.event.single.DeclareSingleEventUseCase;
import bookyourinstructor.usecase.event.common.store.EventLockStore;
import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
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
        final CyclicEventRealizationsFinder realizationsFinder = new CyclicEventRealizationsFinder(timeUtils);
        return new DeclareCyclicEventUseCase(eventStore, eventRealizationStore, transactionFacade, realizationsFinder);
    }

    @Bean
    CreateEventBookLockUseCase createEventBookLockUseCase(TransactionFacade transactionFacade, TimeUtils timeUtils, EventLockStore eventLockStore,
                                                          EventStore eventStore, @Value("${event.lock.validity-duration}") final Duration lockExpirationDuration) {
        return new CreateEventBookLockUseCase(transactionFacade, timeUtils, eventLockStore, eventStore, lockExpirationDuration);
    }

    @Bean
    ConfirmEventBookLockUseCase confirmEventBookLockUseCase(TransactionFacade transactionFacade, TimeUtils timeUtils, EventStore eventStore,
                                                            EventLockStore eventLockStore, EventRealizationStore eventRealizationStore) {
        return new ConfirmEventBookLockUseCase(transactionFacade, timeUtils, eventStore, eventLockStore, eventRealizationStore);
    }

    @Bean
    AcceptEventUseCase acceptEventUseCase(EventStore eventStore, EventRealizationStore eventRealizationStore,
                                          TransactionFacade transactionFacade) {
        return new AcceptEventUseCase(eventStore, eventRealizationStore, transactionFacade);
    }

    @Bean
    UpdateCyclicEventRealizationUseCase updateCyclicEventRealizationUseCase(EventStore eventStore, EventRealizationStore eventRealizationStore,
                                                                            TransactionFacade transactionFacade, TimeUtils timeUtils) {
        return new UpdateCyclicEventRealizationUseCase(eventStore, eventRealizationStore, transactionFacade, timeUtils);
    }

    @Bean
    DeleteEventBookLockUseCase deleteEventBookLockUseCase(EventLockStore eventLockStore, TransactionFacade transactionFacade) {
        return new DeleteEventBookLockUseCase(eventLockStore, transactionFacade);
    }
}
