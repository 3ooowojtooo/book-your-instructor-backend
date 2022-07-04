package com.quary.bookyourinstructor.configuration.usecase.event;

import bookyourinstructor.usecase.authentication.user.UserStore;
import bookyourinstructor.usecase.event.booklock.ConfirmEventBookLockUseCase;
import bookyourinstructor.usecase.event.booklock.CreateEventBookLockUseCase;
import bookyourinstructor.usecase.event.booklock.DeleteEventBookLockUseCase;
import bookyourinstructor.usecase.event.common.AcceptEventUseCase;
import bookyourinstructor.usecase.event.common.DeleteDraftEventUseCase;
import bookyourinstructor.usecase.event.common.GetEventDetailsAsStudentUseCase;
import bookyourinstructor.usecase.event.common.ReportAbsenceUseCase;
import bookyourinstructor.usecase.event.common.helper.InstructorAbsenceReporter;
import bookyourinstructor.usecase.event.common.helper.StudentAbsenceReporter;
import bookyourinstructor.usecase.event.common.store.EventLockStore;
import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.event.common.store.EventStudentAbsenceStore;
import bookyourinstructor.usecase.event.cyclic.DeclareCyclicEventUseCase;
import bookyourinstructor.usecase.event.cyclic.ResignCyclicEventUseCase;
import bookyourinstructor.usecase.event.cyclic.UpdateCyclicEventRealizationUseCase;
import bookyourinstructor.usecase.event.cyclic.helper.CyclicEventRealizationsFinder;
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
        return new DeclareCyclicEventUseCase(eventStore, eventRealizationStore, transactionFacade, realizationsFinder);
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
                                                            EventRealizationStore eventRealizationStore) {
        return new ConfirmEventBookLockUseCase(transactionFacade, timeUtils, retryManager, eventStore, eventLockStore, eventRealizationStore);
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
                                              EventStudentAbsenceStore eventStudentAbsenceStore, TimeUtils timeUtils,
                                              TransactionFacade transactionFacade, RetryManager retryManager) {
        InstructorAbsenceReporter instructorAbsenceReporter = new InstructorAbsenceReporter(eventStore, eventRealizationStore,
                timeUtils, transactionFacade, retryManager);
        StudentAbsenceReporter studentAbsenceReporter = new StudentAbsenceReporter(eventStore, eventRealizationStore,
                eventStudentAbsenceStore, transactionFacade, timeUtils, retryManager);
        return new ReportAbsenceUseCase(instructorAbsenceReporter, studentAbsenceReporter);
    }

    @Bean
    ResignCyclicEventUseCase resignCyclicEventUseCase(EventStore eventStore, EventRealizationStore eventRealizationStore,
                                                      TimeUtils timeUtils, TransactionFacade transactionFacade, RetryManager retryManager) {
        return new ResignCyclicEventUseCase(eventStore, eventRealizationStore, timeUtils, transactionFacade, retryManager);
    }

    @Bean
    SearchEventsUseCase searchEventsUseCase(EventStore eventStore) {
        return new SearchEventsUseCase(eventStore);
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
}
