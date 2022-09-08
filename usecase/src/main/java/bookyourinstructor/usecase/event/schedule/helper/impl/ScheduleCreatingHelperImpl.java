package bookyourinstructor.usecase.event.schedule.helper.impl;

import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventScheduleStore;
import bookyourinstructor.usecase.event.schedule.helper.ScheduleCreatingHelper;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import com.quary.bookyourinstructor.model.event.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ScheduleCreatingHelperImpl implements ScheduleCreatingHelper {

    private final EventRealizationStore eventRealizationStore;
    private final EventScheduleStore eventScheduleStore;
    private final TransactionFacade transactionFacade;

    @Override
    public void handleEventBooked(Event event, Integer studentId) {
        transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.READ_COMMITTED, () -> {
            List<EventRealization> realizations = eventRealizationStore.findAllRealizationWithStatusAndStudentId(event.getId(),
                    EventRealizationStatus.BOOKED, studentId);
            List<EventSchedule> schedules = realizations.stream()
                    .flatMap(realization -> Stream.of(
                            EventSchedule.newDynamicEventSchedule(event.getId(), realization.getId(), studentId,
                                    event.getInstructorId(), EventScheduleStatus.BOOKED, EventScheduleOwner.STUDENT,
                                    realization.getStart(), realization.getEnd()),
                            EventSchedule.newDynamicEventSchedule(event.getId(), realization.getId(), studentId,
                                    event.getInstructorId(), EventScheduleStatus.BOOKED, EventScheduleOwner.INSTRUCTOR,
                                    realization.getStart(), realization.getEnd())
                    ))
                    .collect(Collectors.toList());
            eventScheduleStore.saveSchedules(schedules);
        });
    }

    @Override
    public void handleStudentAbsence(Event event, EventRealization eventRealization) {
        transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.READ_COMMITTED, () -> {
            eventScheduleStore.update(event.getId(), eventRealization.getId(), EventScheduleStatus.BOOKED, EventScheduleOwner.INSTRUCTOR,
                    EventScheduleStatus.STUDENT_ABSENT);
            eventScheduleStore.update(event.getId(), eventRealization.getId(), EventScheduleStatus.BOOKED, EventScheduleOwner.STUDENT,
                    EventScheduleStatus.STUDENT_ABSENT);
        });
    }

    @Override
    public void handleInstructorAbsence(Event event, EventRealization eventRealization) {
        transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.READ_COMMITTED, () -> {
            eventScheduleStore.update(event.getId(), eventRealization.getId(), EventScheduleStatus.BOOKED, EventScheduleOwner.INSTRUCTOR,
                    EventScheduleStatus.INSTRUCTOR_ABSENT);
            eventScheduleStore.update(event.getId(), eventRealization.getId(), EventScheduleStatus.BOOKED, EventScheduleOwner.STUDENT,
                    EventScheduleStatus.INSTRUCTOR_ABSENT);
        });
    }

    @Override
    public void handleCyclicEventResigned(CyclicEvent event, List<Integer> resignedRealizationsIds) {
        transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.READ_COMMITTED, () ->
                eventScheduleStore.delete(event.getId(), resignedRealizationsIds, EventScheduleStatus.BOOKED)
        );
    }
}
