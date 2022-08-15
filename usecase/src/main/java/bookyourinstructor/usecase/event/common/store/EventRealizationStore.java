package bookyourinstructor.usecase.event.common.store;

import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.EventRealizationStatus;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRealizationStore {

    EventRealization saveEventRealization(final EventRealization eventRealization);

    List<EventRealization> saveEventRealizations(final List<EventRealization> eventRealizations);

    Optional<EventRealization> findById(Integer eventRealizationId);

    Optional<EventRealization> findByIdWithLockForUpdate(Integer eventRealizationId);

    void setStudentIdForEventRealizations(final Integer studentId, final Integer eventId);

    void setStudentIdForEventRealization(Integer studentId, Integer id);

    void setStatusForEventRealization(EventRealizationStatus status, Integer id);

    void setStatusForEventRealizations(final EventRealizationStatus status, final Integer eventId);

    void setStatusForEventRealizations(final EventRealizationStatus status, final Collection<Integer> ids);

    void setStatusForEventRealizationsWithStatus(final EventRealizationStatus currentStatus, final EventRealizationStatus newStatus,
                                                 final Integer eventId);

    List<EventRealization> findAllRealizations(final Integer eventId);

    List<EventRealization> findAllFutureRealizationsWithStatus(final Integer eventId, final Instant now, final EventRealizationStatus status);

    List<EventRealization> findAllByEventIdAndStatusStartingAfterSortedAscWithLockForUpdate(final Integer eventId, EventRealizationStatus status, final Instant now);

    List<EventRealization> findAllRealizationWithStatusAndStudentId(final Integer eventId, EventRealizationStatus status, Integer studentId);

    void deleteRealizationsByEventId(final Integer eventId);
}
