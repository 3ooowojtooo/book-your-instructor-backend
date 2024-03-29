package bookyourinstructor.usecase.event.cyclic;

import bookyourinstructor.usecase.event.common.exception.ConcurrentDataModificationRuntimeException;
import bookyourinstructor.usecase.event.common.exception.EventChangedRuntimeException;
import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.event.cyclic.data.ResignCyclicEventData;
import bookyourinstructor.usecase.event.cyclic.exception.CyclicEventNoFutureRealizationsFoundRuntimeException;
import bookyourinstructor.usecase.event.schedule.helper.ScheduleCreatingHelper;
import bookyourinstructor.usecase.util.retry.RetryInstanceName;
import bookyourinstructor.usecase.util.retry.RetryManager;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import com.quary.bookyourinstructor.model.event.*;
import com.quary.bookyourinstructor.model.event.exception.ConcurrentDataModificationException;
import com.quary.bookyourinstructor.model.event.exception.CyclicEventNoFutureRealizationsFoundException;
import com.quary.bookyourinstructor.model.event.exception.EventChangedException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;
import static com.quary.bookyourinstructor.model.event.EventRealizationStatus.BOOKED;

@RequiredArgsConstructor
public class ResignCyclicEventUseCase {

    private final EventStore eventStore;
    private final EventRealizationStore eventRealizationStore;
    private final TimeUtils timeUtils;
    private final TransactionFacade transactionFacade;
    private final RetryManager retryManager;
    private final ScheduleCreatingHelper scheduleCreatingHelper;

    public void resignCyclicEvent(ResignCyclicEventData data) throws EventChangedException, CyclicEventNoFutureRealizationsFoundException,
            ConcurrentDataModificationException {
        final Instant now = timeUtils.nowInstant();
        try {
            retryManager.runInLockRetry(RetryInstanceName.RESIGN_CYCLIC_EVENT, () -> resignCyclicEventInternal(data, now));
        } catch (EventChangedRuntimeException e) {
            throw new EventChangedException();
        } catch (CyclicEventNoFutureRealizationsFoundRuntimeException e) {
            throw new CyclicEventNoFutureRealizationsFoundException();
        } catch (ConcurrentDataModificationRuntimeException e) {
            throw new ConcurrentDataModificationException();
        }
    }

    private void resignCyclicEventInternal(ResignCyclicEventData data, Instant now) {
        transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.REPEATABLE_READ, () -> {
            CyclicEvent cyclicEvent = findCyclicEventByIdWithLockForUpdate(data.getCyclicEventId());
            validateEventVersion(cyclicEvent, data.getCyclicEventVersion());
            validateEventBooked(cyclicEvent);
            validateEventOwner(cyclicEvent, data.getStudentId());

            List<EventRealization> resignedRealizations = findRemainingRealizationsSortedWithLockForUpdate(data.getCyclicEventId(), now);
            List<Integer> resignedRealizationsIds = mapToRealizationIds(resignedRealizations);

            eventStore.setStatusByIdAndIncrementVersion(data.getCyclicEventId(), EventStatus.STUDENT_RESIGNED);
            eventRealizationStore.setStatusForEventRealizations(EventRealizationStatus.STUDENT_RESIGNED, resignedRealizationsIds);
            CyclicEvent newCyclicEvent = buildEvent(cyclicEvent, resignedRealizations);
            CyclicEvent savedEvent = eventStore.saveCyclicEvent(newCyclicEvent);
            List<EventRealization> newRealizations = buildRealizations(resignedRealizations, savedEvent);
            eventRealizationStore.saveEventRealizations(newRealizations);

            scheduleCreatingHelper.handleCyclicEventResigned(cyclicEvent, resignedRealizationsIds);
        });
    }

    private CyclicEvent findCyclicEventByIdWithLockForUpdate(Integer cyclicEventId) {
        return eventStore.findByIdWithLockForUpdate(cyclicEventId)
                .filter(event -> event.getType() == EventType.CYCLIC)
                .map(event -> (CyclicEvent) event)
                .orElseThrow(() -> new IllegalArgumentException("Cyclic event with id " + cyclicEventId + " not found"));
    }

    private static void validateEventVersion(Event event, Integer version) throws EventChangedRuntimeException {
        if (!Objects.equals(event.getVersion(), version)) {
            throw new EventChangedRuntimeException();
        }
    }

    private static void validateEventBooked(Event event) {
        checkState(event.getStatus() == EventStatus.BOOKED, "You can only resign of booked event");
    }

    private List<EventRealization> findRemainingRealizationsSortedWithLockForUpdate(Integer eventId, Instant now) throws CyclicEventNoFutureRealizationsFoundRuntimeException {
        List<EventRealization> eventRealizations = eventRealizationStore.findAllByEventIdAndStatusStartingAfterSortedAscWithLockForUpdate(eventId, BOOKED, now).stream()
                .sorted(Comparator.comparing(EventRealization::getStart))
                .collect(Collectors.toList());
        if (eventRealizations.isEmpty()) {
            throw new CyclicEventNoFutureRealizationsFoundRuntimeException();
        }
        return eventRealizations;
    }


    private static List<Integer> mapToRealizationIds(List<EventRealization> realizations) {
        return realizations.stream()
                .map(EventRealization::getId)
                .collect(Collectors.toList());
    }

    private static void validateEventOwner(CyclicEvent event, Integer studentId) {
        checkState(Objects.equals(event.getStudentId(), studentId), "You can only resign from your own event");
    }

    private CyclicEvent buildEvent(CyclicEvent cyclicEvent, List<EventRealization> realizations) {
        EventRealization firstRealization = realizations.get(0);
        EventRealization lastRealization = realizations.get(realizations.size() - 1);
        LocalDateTime startBoundary = timeUtils.toLocalDateTimeUTCZone(firstRealization.getStart());
        LocalDateTime endBoundary = timeUtils.toLocalDateTimeUTCZone(lastRealization.getEnd());
        return CyclicEvent.newCyclicEventFree(cyclicEvent.getInstructorId(), cyclicEvent.getName(), cyclicEvent.getDescription(),
                cyclicEvent.getLocation(), cyclicEvent.getPrice(), cyclicEvent.getCreatedAt(), cyclicEvent.getStartTime(), cyclicEvent.getDuration(),
                cyclicEvent.getDayOfWeek(), startBoundary, endBoundary, cyclicEvent.isAbsenceEvent(), cyclicEvent.getAbsenceEventName(),
                cyclicEvent.getAbsenceEventDescription());
    }

    private List<EventRealization> buildRealizations(List<EventRealization> oldRealizations, CyclicEvent event) {
        return oldRealizations.stream()
                .map(realization -> EventRealization.newAccepted(event.getId(), realization.getStart(), realization.getEnd()))
                .collect(Collectors.toList());
    }
}
