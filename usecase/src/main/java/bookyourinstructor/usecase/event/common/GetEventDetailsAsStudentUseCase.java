package bookyourinstructor.usecase.event.common;

import bookyourinstructor.usecase.authentication.user.UserStore;
import bookyourinstructor.usecase.event.common.data.GetEventDetailsAsStudentData;
import bookyourinstructor.usecase.event.common.result.GetEventDetailsAsStudentResult;
import bookyourinstructor.usecase.event.common.store.EventLockStore;
import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import com.quary.bookyourinstructor.model.event.*;
import com.quary.bookyourinstructor.model.user.User;
import lombok.RequiredArgsConstructor;

import java.time.*;
import java.util.List;

@RequiredArgsConstructor
public class GetEventDetailsAsStudentUseCase {

    private final EventStore eventStore;
    private final UserStore userStore;
    private final EventRealizationStore eventRealizationStore;
    private final EventLockStore eventLockStore;
    private final TransactionFacade transactionFacade;
    private final TimeUtils timeUtils;

    public GetEventDetailsAsStudentResult getDetails(GetEventDetailsAsStudentData data) {
        final Instant now = timeUtils.nowInstant();
        return transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.READ_COMMITTED, () -> {
            Event event = findEventOrThrow(data.getEventId());
            User instructor = findInstructorOrThrow(event.getInstructorId());
            List<EventRealization> realizations = eventRealizationStore.findAllRealizations(data.getEventId());
            boolean eventLocked = eventLockStore.existsByEventIdAndFutureExpirationTime(data.getEventId(), now);
            return buildResult(event, instructor, realizations, eventLocked);
        });
    }

    private Event findEventOrThrow(Integer eventId) {
        return eventStore.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event with id " + eventId + " not found"));
    }

    private User findInstructorOrThrow(Integer instructorId) {
        return userStore.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + instructorId + " not found"));
    }

    private GetEventDetailsAsStudentResult buildResult(Event event, User instructor, List<EventRealization> realizations,
                                                       boolean eventLocked) {
        LocalDateTime singleEventStart = null;
        LocalDateTime singleEventEnd = null;
        DayOfWeek cyclicEventDayOfWeek = null;
        LocalTime cyclicEventStartTime = null;
        Duration cyclicEventDuration = null;

        if (event.getType() == EventType.SINGLE) {
            SingleEvent singleEvent = (SingleEvent) event;
            singleEventStart = singleEvent.getStartDateTime();
            singleEventEnd = singleEvent.getEndDateTime();
        } else if (event.getType() == EventType.CYCLIC) {
            CyclicEvent cyclicEvent = (CyclicEvent) event;
            cyclicEventDayOfWeek = cyclicEvent.getDayOfWeek();
            cyclicEventStartTime = cyclicEvent.getStartTime();
            cyclicEventDuration = cyclicEvent.getDuration();
        }

        String instructorName = instructor.getName() + " " + instructor.getSurname();
        return new GetEventDetailsAsStudentResult(event.getId(), event.getVersion(), event.getName(), event.getDescription(),
                event.getLocation(), instructorName, event.getType(), singleEventStart, singleEventEnd, cyclicEventDayOfWeek,
                cyclicEventStartTime, cyclicEventDuration, eventLocked, realizations);
    }

}
