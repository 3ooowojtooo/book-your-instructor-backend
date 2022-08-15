package com.quary.bookyourinstructor.configuration.usecase.event;

import bookyourinstructor.usecase.authentication.user.UserStore;
import bookyourinstructor.usecase.event.booklock.ConfirmEventBookLockUseCase;
import bookyourinstructor.usecase.event.booklock.CreateEventBookLockUseCase;
import bookyourinstructor.usecase.event.booklock.DeleteEventBookLockUseCase;
import bookyourinstructor.usecase.event.common.*;
import bookyourinstructor.usecase.event.common.helper.InstructorAbsenceReporter;
import bookyourinstructor.usecase.event.common.helper.StudentAbsenceReporter;
import bookyourinstructor.usecase.event.common.store.EventLockStore;
import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventScheduleStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.event.cyclic.DeclareCyclicEventUseCase;
import bookyourinstructor.usecase.event.cyclic.ResignCyclicEventUseCase;
import bookyourinstructor.usecase.event.cyclic.UpdateCyclicEventRealizationUseCase;
import bookyourinstructor.usecase.event.cyclic.helper.CyclicEventRealizationsFinder;
import bookyourinstructor.usecase.event.schedule.GetEventScheduleUseCase;
import bookyourinstructor.usecase.event.schedule.helper.ScheduleCreatingHelper;
import bookyourinstructor.usecase.event.search.SearchEventsUseCase;
import bookyourinstructor.usecase.event.single.DeclareSingleEventUseCase;
import bookyourinstructor.usecase.util.retry.RetryManager;
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
        return new DeclareCyclicEventUseCase(eventStore, eventRealizationStore, transactionFacade, realizationsFinder, timeUtils);
    }

    @Bean
    CreateEventBookLockUseCase createEventBookLockUseCase(TransactionFacade transactionFacade, TimeUtils timeUtils, RetryManager retryManager,
                                                          EventLockStore eventLockStore, EventStore eventStore,
                                                          @Value("${event.lock.validity-duration}") final Duration lockExpirationDuration) {
        return new CreateEventBookLockUseCase(transactionFacade, timeUtils, retryManager, eventLockStore, eventStore, lockExpirationDuration);
    }

    @Bean
    ConfirmEventBookLockUseCase confirmEventBookLockUseCase(TransactionFacade transactionFacade, TimeUtils timeUtils, RetryManager retryManager,
                                                            EventStore eventStore, EventLockStore eventLockStore,
                                                            EventRealizationStore eventRealizationStore, ScheduleCreatingHelper scheduleCreatingHelper) {
        return new ConfirmEventBookLockUseCase(transactionFacade, timeUtils, retryManager, eventStore, eventLockStore,
                eventRealizationStore, scheduleCreatingHelper);
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

    @Bean
    DeleteDraftEventUseCase deleteDraftEventUseCase(EventStore eventStore, EventRealizationStore eventRealizationStore,
                                                    TransactionFacade transactionFacade) {
        return new DeleteDraftEventUseCase(eventStore, eventRealizationStore, transactionFacade);
    }

    @Bean
    ReportAbsenceUseCase reportAbsenceUseCase(EventStore eventStore, EventRealizationStore eventRealizationStore,
                                              TimeUtils timeUtils, TransactionFacade transactionFacade, RetryManager retryManager,
                                              ScheduleCreatingHelper scheduleCreatingHelper) {
        InstructorAbsenceReporter instructorAbsenceReporter = new InstructorAbsenceReporter(eventStore, eventRealizationStore,
                timeUtils, transactionFacade, retryManager, scheduleCreatingHelper);
        StudentAbsenceReporter studentAbsenceReporter = new StudentAbsenceReporter(eventStore, eventRealizationStore,
                transactionFacade, timeUtils, retryManager, scheduleCreatingHelper);
        return new ReportAbsenceUseCase(instructorAbsenceReporter, studentAbsenceReporter);
    }

    @Bean
    ResignCyclicEventUseCase resignCyclicEventUseCase(EventStore eventStore, EventRealizationStore eventRealizationStore,
                                                      TimeUtils timeUtils, TransactionFacade transactionFacade,
                                                      RetryManager retryManager, ScheduleCreatingHelper scheduleCreatingHelper) {
        return new ResignCyclicEventUseCase(eventStore, eventRealizationStore, timeUtils, transactionFacade, retryManager,
                scheduleCreatingHelper);
    }

    @Bean
    SearchEventsUseCase searchEventsUseCase(EventStore eventStore, TimeUtils timeUtils) {
        return new SearchEventsUseCase(eventStore, timeUtils);
    }

    @Bean
    GetEventDetailsAsStudentUseCase getEventDetailsAsStudentUseCase(EventStore eventStore, UserStore userStore,
                                                                    EventRealizationStore eventRealizationStore,
                                                                    EventLockStore eventLockStore,
                                                                    TransactionFacade transactionFacade,
                                                                    TimeUtils timeUtils) {
        return new GetEventDetailsAsStudentUseCase(eventStore, userStore, eventRealizationStore, eventLockStore,
                transactionFacade, timeUtils);
    }

    @Bean
    GetEventScheduleUseCase getEventScheduleUseCase(EventScheduleStore eventScheduleStore, TransactionFacade transactionFacade,
                                                    TimeUtils timeUtils) {
        return new GetEventScheduleUseCase(eventScheduleStore, transactionFacade, timeUtils);
    }

    @Bean
    GetEventListUseCase getEventListUseCase(EventStore eventStore, TransactionFacade transactionFacade, TimeUtils timeUtils) {
        return new GetEventListUseCase(eventStore, transactionFacade, timeUtils);
    }
}
