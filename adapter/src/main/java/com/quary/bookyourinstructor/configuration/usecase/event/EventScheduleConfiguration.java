package com.quary.bookyourinstructor.configuration.usecase.event;

import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventScheduleStore;
import bookyourinstructor.usecase.event.schedule.helper.ScheduleCreatingHelper;
import bookyourinstructor.usecase.event.schedule.helper.impl.ScheduleCreatingHelperImpl;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventScheduleConfiguration {

    @Bean
    ScheduleCreatingHelper scheduleCreatingHelper(EventRealizationStore eventRealizationStore, EventScheduleStore eventScheduleStore,
                                                  TransactionFacade transactionFacade) {
        return new ScheduleCreatingHelperImpl(eventRealizationStore, eventScheduleStore, transactionFacade);
    }
}
